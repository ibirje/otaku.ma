# 04 — Database Architecture: Relational → Firestore Document Model

## Goal

Redesign the data model from a normalised relational schema (Google Cloud SQL / MySQL or PostgreSQL) to a Firestore document model that is:

- Optimised for the access patterns of the application (not theoretical normalisation)
- Partitioned per microservice (each service owns its collections)
- Efficient for the most frequent reads (anime lists, user watchlists, reviews)

---

## Why Firestore (NoSQL) Changes How You Model Data

In SQL, you normalise to avoid duplication and use JOINs to reassemble data.  
In Firestore, **JOINs don't exist**. You model data the way it will be read.

**Key rule:** Design your documents around your queries, not around your entities.

---

## Relational → Document Thinking

| SQL concept | Firestore equivalent |
|-------------|---------------------|
| Table | Collection |
| Row | Document |
| Column | Field |
| Foreign key | Embedded sub-document or document reference |
| JOIN | Denormalised copy of data, or client-side join |
| Index | Composite index defined in `firestore.indexes.json` |
| Transaction | Firestore transaction (multi-document, same DB) |
| Schema | Flexible (enforced by application validation) |

---

## Microservice → Collection Ownership

Each microservice owns and is the **sole writer** to its collections.  
Derived from the actual `otaserver` MySQL schema (35 tables):

| Microservice | Firestore Collections | Source SQL Tables |
|-------------|----------------------|-------------------|
| `auth-service` | `admins`, `admin_roles`, `admin_droits`, `admin_tokens`, `client_sessions` | `admin`, `admin_role`, `admin_droit`, `admin_roledroit`, `token`, `client_session` |
| `user-service` | `clients`, `client_addresses`, `client_pending` | `client`, `client_login`, `client_adresse`, `client_pending` |
| `catalog-service` | `products`, `categories`, `themes`, `attributes`, `attribute_options`, `variations`, `variation_options` | `produit`, `categorie`, `theme`, `attribut`, `optionattribut`, `variation`, `variationoption` |
| `order-service` | `orders`, `order_items`, `couriers` | `commande`, `commandeitem`, `coursier` |
| `inventory-service` | `purchase_orders`, `purchase_items`, `skus`, `suppliers`, `damages`, `losses` | `achatstock`, `achatstockitem`, `sku`, `fournisseur`, `casse`, `perte` |
| `review-service` | `reviews` | `avis` |

---

## Proposed Document Schemas

These are grounded in the **actual MySQL DDL** (`migration-sql-firestore/ddl/ddl.sql` — 35 tables).

### `products` (owned by `catalog-service`)

```
products/{productId}
  ├── id: string                  ← produitID (string form)
  ├── code: string                ← URL-safe slug
  ├── nom: string
  ├── keywords: string | null
  ├── description: string | null
  ├── prixUnite: number
  ├── prixPromo: number | null
  ├── qte: number                 ← denormalised stock total
  ├── images: string[]
  ├── isActive: boolean
  ├── categorie: { id, code, nom }   ← denormalised
  ├── theme: { id, code, nom }       ← denormalised
  └── attributs: [{                  ← denormalised from attribut + optionattribut
        attributId, nom,
        options: [{ optionId, description }]
      }]
```

Subcollection for variations:
```
products/{productId}/variations/{variationId}
  ├── id, code, nom
  ├── prixUnite: number
  ├── prixPromo: number | null
  ├── images: string[]
  ├── qte: number
  └── options: [{ optionAttributId, description }]
```

### `clients` (owned by `user-service`)

```
clients/{clientId}
  ├── id, nom, prenom, email, pseudo
  ├── telephone1, telephone2
  ├── dateNaissance: timestamp | null
  ├── isActive: boolean
  ├── etat: 'NORMAL' | 'BLOQUE' | 'SUPPRIME'
  ├── panierCount: number
  └── dateCreation: timestamp
```

Subcollection for addresses:
```
clients/{clientId}/adresses/{adresseId}
  ├── id, prenom, nom, organisation
  ├── adresse1, adresse2, ville, codePostal
  ├── telephone1, telephone2, email
  └── etat: string | null         ← 'SELECTED' or null
```

### `orders` (owned by `order-service`)

```
orders/{orderId}
  ├── id: string
  ├── code: string                ← unique order reference
  ├── clientId: string
  ├── clientNom, clientEmail      ← denormalised snapshot
  ├── etat: 'EN_ATTENTE' | 'ACCEPTEE' | 'EN_PREPARATION' | 'ENVOYEE' | 'LIVREE' | 'ANNULEE'
  ├── prixPieces: number
  ├── prixLivraison: number
  ├── dateCommande: timestamp
  ├── adresseLivraison: { ... }   ← full address snapshot at order time
  ├── items: [{                   ← embedded commandeitem rows
  │     produitId, produitNom, variationId, variationNom,
  │     quantite: number, prixUnite: number
  │   }]
  └── coursier: { id, nom, prix } ← snapshot
```

### `categories` (owned by `catalog-service`)

```
categories/{categorieId}
  ├── id, code, nom
  ├── keywords, description
  ├── nombreProduits: number
  ├── isActive: boolean
  ├── smallImage, mediumImage, largeImage
  └── categorieParentId: string | null
```

### `themes` (owned by `catalog-service`)

```
themes/{themeId}
  ├── id, code, nom
  ├── description
  ├── nombreProduits: number
  ├── isActive: boolean
  └── images: string[]
```

### `reviews` (owned by `review-service`)

```
reviews/{reviewId}
  ├── id, clientId, productId
  ├── clientNom           ← denormalised
  ├── productNom          ← denormalised
  ├── note: number
  ├── commentaire: string
  └── class: string       ← legacy display class from old app
```

### `skus` (owned by `inventory-service`)

```
skus/{skuId}
  ├── id, code
  ├── variationId: string
  ├── productId: string          ← denormalised
  ├── achatId: string            ← purchase order reference
  └── qte: number
```

---

## Denormalisation Strategy

| Data to denormalise | Where it's copied | Update trigger |
|--------------------|------------------|----------------|
| `categorie.nom`, `categorie.code` | `products` document | Pub/Sub event when category is updated |
| `theme.nom`, `theme.code` | `products` document | Pub/Sub event when theme is updated |
| `client.nom`, `client.email` | `orders` document | Snapshot at order creation (never changes) |
| `produit.nom`, `produit.prixUnite` | `orders.items[]` | Snapshot at order creation (price at time of purchase) |
| `variation.nom` | `orders.items[]` | Snapshot at order creation |
| `produit.nom` | `reviews` document | Pub/Sub event when product name changes |
| `qte` on `products` | Aggregate from `skus` | Recalculated by inventory-service on SKU change |

---

## Indexing Strategy

Firestore automatically indexes every field for single-field queries. Composite indexes must be defined explicitly.

```json
// firestore.indexes.json
{
  "indexes": [
    {
      "collectionGroup": "products",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "categorie.code", "order": "ASCENDING" },
        { "fieldPath": "prixUnite", "order": "ASCENDING" }
      ]
    },
    {
      "collectionGroup": "products",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "theme.code", "order": "ASCENDING" },
        { "fieldPath": "prixUnite", "order": "ASCENDING" }
      ]
    },
    {
      "collectionGroup": "orders",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "clientId", "order": "ASCENDING" },
        { "fieldPath": "dateCommande", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "reviews",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "productId", "order": "ASCENDING" },
        { "fieldPath": "note", "order": "DESCENDING" }
      ]
    }
  ]
}
```

---

## Firestore Security Rules

Each service accesses Firestore via a **dedicated service account** with IAM roles, not through client-side rules. Client-side rules are only needed if the Angular frontend accesses Firestore directly (not recommended — route through APIs).

If direct client access is ever used:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth.uid == userId;
    }
    match /watchlists/{userId}/{document=**} {
      allow read, write: if request.auth.uid == userId;
    }
    match /reviews/{reviewId} {
      allow read: if true;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.userId;
    }
  }
}
```

---

## Checklist

- [ ] List all existing SQL tables and their relationships
- [ ] Map each table to the microservice that owns its domain
- [ ] Design Firestore document schema per collection (document above as starting point)
- [ ] Identify which fields to denormalise and define the update strategy (Pub/Sub events)
- [ ] Define composite indexes in `firestore.indexes.json`
- [ ] Define Firestore security rules (or IAM service account strategy)
- [ ] Create architecture decision record (ADR) for each significant schema decision
- [ ] Review with team before any migration code is written
