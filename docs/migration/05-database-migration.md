# 05 — Database Migration: Cloud SQL → Firestore

## Goal

Migrate all data from Google Cloud SQL (relational) to Firestore (document) with:
- Zero data loss
- Minimal downtime (target: zero downtime)
- Per-service rollout aligned with the Spring microservice migration
- Verified data integrity before decommissioning SQL

---

## Strategy: Dual-Write + Gradual Cutover (per service)

This migration does **not** happen in one big operation. Each microservice migrates its own data when it goes live. The overall flow per service is:

```
Phase A       Phase B            Phase C            Phase D
────────      ─────────────      ────────────────   ─────────────
Write SQL     Write SQL +        Write Firestore     Write Firestore
Read SQL      Write Firestore    Read Firestore      only
              Read SQL           (validate 2 weeks)  SQL retired
```

---

## Per-Service Migration Playbook

### Phase A — Before migration (Java EE writes to SQL)

No changes. This is the current state.

### Phase B — Dual-write (Java EE or new Spring service writes to both)

The new Spring service is deployed but writes to **both** Cloud SQL (via JDBC) and Firestore simultaneously. Reads still come from SQL (authoritative source).

```java
// Spring service in dual-write mode
public Anime createAnime(CreateAnimeRequest request) {
    Anime anime = mapper.toAnime(request);
    sqlRepository.save(anime);         // primary write
    firestoreRepository.save(anime);   // shadow write
    return anime;
}
```

This phase runs for **at minimum 1 week** with active monitoring to catch write failures to either database.

### Phase C — Validate and cut reads to Firestore

Run the validation tool (see below) to confirm Firestore data matches SQL. Once validated:
- Switch reads to Firestore
- Keep writing to both for a 2-week safety window

### Phase D — Retire SQL tables

After 2 weeks of Firestore-only reads with no incidents, remove the SQL write path and mark SQL tables as read-only, then archived.

---

## Initial Data Migration Tool

Before Phase B begins, backfill Firestore with all existing SQL data using a one-time migration job.

### Example: Cloud Dataflow (recommended for large datasets)

```java
// Apache Beam pipeline (runs on Cloud Dataflow)
public class SqlToFirestorePipeline {

    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
        Pipeline p = Pipeline.create(options);

        p.apply("ReadFromSQL", JdbcIO.<AnimeRow>read()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                    "com.mysql.cj.jdbc.Driver", "jdbc:mysql://..."))
                .withQuery("SELECT * FROM animes")
                .withRowMapper(new AnimeRowMapper()))

         .apply("TransformToDocument", MapElements.into(TypeDescriptor.of(Document.class))
                .via(AnimeTransformer::toFirestoreDocument))

         .apply("WriteToFirestore", FirestoreIO.v1().write().batchWriteDocuments());

        p.run().waitUntilFinish();
    }
}
```

For smaller datasets, a simple Spring Boot job with paging is sufficient:

```java
@Component
public class AnimeMigrationJob implements CommandLineRunner {

    @Override
    public void run(String... args) {
        int page = 0;
        List<Anime> batch;
        do {
            batch = sqlRepo.findAll(PageRequest.of(page++, 500)).getContent();
            batch.forEach(firestoreRepo::save);
            log.info("Migrated page {}, {} records", page, batch.size());
        } while (!batch.isEmpty());
    }
}
```

---

## Data Transformation: SQL Row → Firestore Document

The relational schema has foreign keys and normalised tables. The Firestore model is denormalised. Transformation happens during migration.

Example: `animes` table with a foreign key to `genres` and `studios`:

```sql
-- SQL (simplified)
animes: id, title_en, title_ja, synopsis, studio_id, episode_count
anime_genres: anime_id, genre_id
genres: id, name
studios: id, name
```

Transformer joins and embeds:

```java
public Document toFirestoreDocument(AnimeRow anime, List<String> genres, String studioName) {
    return Document.newBuilder()
        .putFields("id", stringValue(anime.id))
        .putFields("title", mapValue(Map.of(
            "en", stringValue(anime.titleEn),
            "ja", stringValue(anime.titleJa)
        )))
        .putFields("synopsis", stringValue(anime.synopsis))
        .putFields("studio", stringValue(studioName))      // denormalised
        .putFields("genres", arrayValue(genres))            // denormalised
        .putFields("episodeCount", intValue(anime.episodeCount))
        .build();
}
```

---

## Validation Tool

After the backfill and before cutting reads to Firestore, run a reconciliation job:

```java
@Component
public class DataValidator {

    public ValidationReport validate() {
        List<Anime> sqlAnimes = sqlRepo.findAll();
        int matched = 0, mismatched = 0;

        for (Anime sqlAnime : sqlAnimes) {
            Optional<AnimeDocument> fsDoc = firestoreRepo.findById(sqlAnime.getId());
            if (fsDoc.isEmpty()) {
                log.error("Missing in Firestore: {}", sqlAnime.getId());
                mismatched++;
            } else if (!matches(sqlAnime, fsDoc.get())) {
                log.error("Mismatch for id {}: SQL={}, FS={}", sqlAnime.getId(), sqlAnime, fsDoc.get());
                mismatched++;
            } else {
                matched++;
            }
        }

        return new ValidationReport(matched, mismatched, sqlAnimes.size());
    }
}
```

**Do not proceed to Phase C until mismatches = 0.**

---

## Migration Tool: `migration-sql-firestore`

A **Spring Batch migration tool** already exists in `migration-sql-firestore/`. Use it instead of writing a Dataflow pipeline from scratch.

### Current status of the tool

| Component | Status |
|-----------|--------|
| `FirestoreConfig.java` | ✅ Complete — flexible credential loading |
| `FirestoreItemWriter.java` | ✅ Complete — batch writes, retry with backoff |
| `MigrationApplication.java` | ✅ Complete — sequential job runner |
| `BatchConfig.java` | ⚠️ Partial — readers/writers configured, job/step wiring incomplete |
| `VerificationConfig.java` | ⚠️ Partial — structure exists, job logic needs review |
| Model classes (18) | ✅ Complete — all key entities mapped |
| Readers (12) | ✅ Complete for: Product, Admin, AdminRole, AdminDroit, Categorie, Theme, Client, Commande, AchatStock, SKU, Attribut, Fournisseur |
| Missing readers | ❌ CommandeItem, AchatStockItem, VariationOption, ClientAddress, ClientLogin, ClientPending, Avis, Casse, Perte |

### What needs to be done to complete the tool

1. **Complete `BatchConfig.java`** — define the job steps wiring each reader to the Firestore writer
2. **Add missing readers** for the 9 tables above
3. **Add skip/fault-tolerance policies** in batch steps
4. **Document Firestore collection names** — use same names as defined in `04-database-architecture.md`
5. **Test with a staging DB** before running on production data

### Running the tool

```bash
cd migration-sql-firestore
cp .env.template .env
# fill in .env with your DB and Firestore credentials

# Run migration
./run.sh
# or on Windows:
./run.bat
```

---

## Service-by-Service Migration Order

Based on the real otaserver domain (35 SQL tables):

| Order | Service | Firestore Collections | Source Tables |
|-------|---------|----------------------|---------------|
| 1 | `auth-service` | `admins`, `admin_roles`, `admin_droits`, `admin_tokens` | `admin`, `admin_role`, `admin_droit`, `admin_roledroit`, `token` |
| 2 | `catalog-service` | `categories`, `themes`, `products`, `attributes` | `categorie`, `theme`, `produit`, `variation`, `variationoption`, `attribut`, `optionattribut` |
| 3 | `user-service` | `clients`, `client_addresses`, `client_pending` | `client`, `client_login`, `client_adresse`, `client_pending`, `client_session` |
| 4 | `order-service` | `orders`, `couriers` | `commande`, `commandeitem`, `coursier` |
| 5 | `inventory-service` | `purchase_orders`, `skus`, `suppliers` | `achatstock`, `achatstockitem`, `sku`, `fournisseur`, `casse`, `perte` |
| 6 | `review-service` | `reviews` | `avis` |

---

## Rollback Plan

At each phase, rollback is available:

| Phase | Rollback action |
|-------|----------------|
| B (dual-write) | Disable Firestore writes, reads already on SQL |
| C (reading Firestore) | Switch reads back to SQL (one config flag) |
| D (decommission) | Restore from Cloud SQL backup (kept for 30 days post-decommission) |

Never delete the Cloud SQL backup until 30 days after full decommission with no incidents.

---

## Monitoring During Migration

Set up alerts for:
- Firestore write failure rate > 0.1%
- Data validation mismatch count > 0
- Read latency increase > 20% after cutting to Firestore
- Cloud SQL active connections drop to zero (confirms all reads migrated)

---

## Checklist

- [ ] Export full Cloud SQL backup before starting (snapshot)
- [ ] Complete `BatchConfig.java` — wire all job steps
- [ ] Add missing readers: CommandeItem, AchatStockItem, VariationOption, ClientAddress, ClientLogin, Avis, Casse, Perte
- [ ] Add skip/fault-tolerance policies in batch config
- [ ] Configure `.env` with real DB + Firestore credentials
- [ ] Run migration tool against staging database
- [ ] Run verification job — confirm mismatches = 0
- [ ] Deploy `auth-service` in dual-write mode (SQL + Firestore)
- [ ] Monitor dual-write for 1 week
- [ ] Cut reads to Firestore for `auth-service`, validate 2 weeks
- [ ] Retire SQL `admin` / `token` tables
- [ ] Repeat for `catalog-service` (products, categories, themes, variations)
- [ ] Repeat for `user-service` (clients, addresses)
- [ ] Repeat for `order-service` (commandes, commandeitems, coursiers)
- [ ] Repeat for `inventory-service` (achatstock, sku, fournisseur)
- [ ] Repeat for `review-service` (avis)
- [ ] All 35 SQL tables migrated, all services reading Firestore only
- [ ] Cloud SQL decommissioned (keep backups 30 days)

---

## References

- [Firestore Data Model](https://firebase.google.com/docs/firestore/data-model)
- [Spring Cloud GCP Firestore](https://googlecloudplatform.github.io/spring-cloud-gcp/docs/current/reference/html/)
- [Cloud Dataflow](https://cloud.google.com/dataflow/docs)
- [Firestore Best Practices](https://firebase.google.com/docs/firestore/best-practices)
