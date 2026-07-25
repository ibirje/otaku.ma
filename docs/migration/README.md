# otaku.ma — Full-Stack Migration Strategy

## Overview

This folder documents the complete modernisation of **otaku.ma** across three dimensions:

| Dimension | From | To |
|-----------|------|----|
| **Frontend** | Angular 6, custom ad-hoc UI | Angular 20, Ionic + Capacitor (web + mobile) |
| **Backend** | Java EE monolith | Spring Boot microservices |
| **Database** | Google Cloud SQL (relational) | Firestore (NoSQL, document model) |

The migration is designed as a **series of independently shippable phases** using the Strangler Fig pattern — the old system remains live and functional throughout, with traffic shifted progressively to the new stack.

---

## Documents

| # | File | Covers |
|---|------|--------|
| 1 | [01-order-of-execution.md](./01-order-of-execution.md) | **Start here** — sequencing rationale, phase map, risk model |
| 2 | [02-frontend.md](./02-frontend.md) | Angular 6 → Angular 20, PWA, Ionic + Capacitor mobile |
| 3 | [03-backend.md](./03-backend.md) | Java EE monolith → Spring Boot microservices |
| 4 | [04-database-architecture.md](./04-database-architecture.md) | Relational schema → Firestore document model design |
| 5 | [05-database-migration.md](./05-database-migration.md) | Cloud SQL → Firestore data migration execution |

---

## Global Strategy in One Sentence

> **Design the data model first, migrate the backend service-by-service behind the same API contracts, migrate the database alongside each service, and let the frontend follow once the API surface is confirmed stable.**

---

## Core Principles

### 1. Backend before Frontend (for data migrations)
The frontend depends on API contracts. Changing the backend and database simultaneously with the frontend creates compounding risk. Stabilise the API layer first, then modernise the frontend freely.

### 2. Strangler Fig for the backend
Don't rewrite the monolith in one shot. Build new Spring microservices next to the old Java EE app, route traffic incrementally, and retire old modules one at a time.

### 3. Each microservice owns its data
In a microservices architecture, **no two services share a database**. Each Spring service owns its Firestore collection(s). This is also why the data model must be redesigned before the migration begins.

### 4. API contracts are frozen during frontend migration
The frontend migration (Angular 6 → 20) must see the exact same HTTP endpoints it sees today. API shape changes are only introduced deliberately and versioned (`/api/v2/...`).

### 5. Zero big-bang cutovers
Every phase ends in a working, deployable state. Rollback is always available until the old system is explicitly decommissioned.

---

## Phase Summary

```
Phase 0 ──► Phase 1 ──► Phase 2 ──► Phase 3 ──► Phase 4
 Design      Backend     Database   Frontend    Hardening
 & Arch.     (Strangler) Migration  Migration   & Decommission
```

See [01-order-of-execution.md](./01-order-of-execution.md) for the detailed breakdown.

---

## Related Documents

- [ANGULAR_MIGRATION_PLAN.md](../../ANGULAR_MIGRATION_PLAN.md) — standalone Angular v20 upgrade guide (pre-dates this full-stack strategy)
