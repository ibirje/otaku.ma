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

Each microservice owns and is the **sole writer** to its collections:

| Microservice | Firestore Collections |
|-------------|----------------------|
| `auth-service` | `auth_tokens`, `refresh_tokens` |
| `user-service` | `users`, `user_settings`, `follows` |
| `catalog-service` | `animes`, `games`, `genres`, `studios` |
| `list-service` | `watchlists`, `gameplaylists` |
| `review-service` | `reviews`, `ratings`, `comments` |
| `notification-service` | `notifications`, `push_subscriptions` |

---

## Proposed Document Schemas

### `users` (owned by `user-service`)

```
users/{userId}
  ├── id: string
  ├── username: string
  ├── displayName: string
  ├── avatarUrl: string
  ├── bio: string
  ├── createdAt: timestamp
  ├── updatedAt: timestamp
  └── stats: {                    ← denormalised for fast profile reads
        animeCount: number,
        followersCount: number,
        followingCount: number
      }
```

### `animes` (owned by `catalog-service`)

```
animes/{animeId}
  ├── id: string
  ├── title: { en: string, ja: string, romaji: string }
  ├── synopsis: string
  ├── coverImageUrl: string
  ├── bannerImageUrl: string
  ├── genres: string[]            ← denormalised from genres collection
  ├── studio: string              ← denormalised name, not FK
  ├── episodeCount: number
  ├── status: 'AIRING' | 'FINISHED' | 'UPCOMING'
  ├── season: { year: number, quarter: 'WINTER'|'SPRING'|'SUMMER'|'FALL' }
  ├── averageRating: number       ← denormalised, updated by review-service events
  ├── ratingCount: number
  └── createdAt: timestamp
```

### `watchlists/{userId}/entries` (owned by `list-service`)

Using a **subcollection** per user for efficient per-user queries:

```
watchlists/{userId}/entries/{animeId}
  ├── animeId: string
  ├── animeTitle: string          ← denormalised for list display without joins
  ├── animeCoverUrl: string       ← denormalised
  ├── status: 'WATCHING' | 'COMPLETED' | 'PLANNED' | 'DROPPED'
  ├── progress: number            ← episodes watched
  ├── rating: number | null
  ├── notes: string
  ├── startedAt: timestamp | null
  └── completedAt: timestamp | null
```

**Query this collection:**

```
// Get all completed anime for a user
db.collection('watchlists').doc(userId)
  .collection('entries')
  .where('status', '==', 'COMPLETED')
  .orderBy('completedAt', 'desc')
  .limit(20)
```

### `reviews` (owned by `review-service`)

```
reviews/{reviewId}
  ├── id: string
  ├── userId: string
  ├── username: string            ← denormalised
  ├── userAvatarUrl: string       ← denormalised
  ├── animeId: string
  ├── animeTitle: string          ← denormalised
  ├── rating: number              ← 1–10
  ├── body: string
  ├── spoiler: boolean
  ├── likes: number
  ├── createdAt: timestamp
  └── updatedAt: timestamp
```

### `follows` (owned by `user-service`)

```
follows/{followerId_followedId}   ← composite document ID for uniqueness
  ├── followerId: string
  ├── followedId: string
  └── createdAt: timestamp
```

Composite IDs let you check "does user A follow user B?" in a single document get, with no query needed.

---

## Denormalisation Strategy

Firestore requires duplicating some data to avoid cross-collection reads. The key is to denormalise **only data that changes rarely**.

| Data to denormalise | Where it's copied | Update strategy |
|--------------------|------------------|-----------------|
| `username`, `avatarUrl` | Reviews, watchlists | Pub/Sub event when user updates profile |
| `animeTitle`, `coverUrl` | Watchlists, reviews | Pub/Sub event when catalog is updated |
| `averageRating` | Anime document | Pub/Sub event when review is created/updated |
| `followersCount` | User document | Firestore transaction when follow is created |

---

## Indexing Strategy

Firestore automatically indexes every field for single-field queries. Composite indexes must be defined explicitly.

```json
// firestore.indexes.json
{
  "indexes": [
    {
      "collectionGroup": "entries",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "status", "order": "ASCENDING" },
        { "fieldPath": "completedAt", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "reviews",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "animeId", "order": "ASCENDING" },
        { "fieldPath": "createdAt", "order": "DESCENDING" }
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
