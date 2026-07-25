# 01 — Order of Execution

## Why Order Matters

All three migrations (frontend, backend, database) have dependencies on each other. Doing them in the wrong order means:

- Migrating the frontend while the API is still changing → constant rework
- Migrating the database before microservice boundaries are defined → wrong data model
- Migrating the backend before the data model is designed → forced schema rewrites mid-flight

The correct order resolves these dependencies systematically.

---

## Recommended Order

```
┌─────────────────────────────────────────────────────────────────┐
│  PHASE 0 — Architecture Design (2–3 weeks)                      │
│  Define Firestore data model + microservice boundaries           │
│  Output: architecture decision records (ADRs), data model docs  │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  PHASE 1 — Backend: Strangler Fig (6–10 weeks)                  │
│  Build Spring microservices alongside Java EE monolith           │
│  Each new service: same API contract, new Firestore storage      │
│  Shift traffic per service, retire Java EE modules one by one    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  PHASE 2 — Database Migration (runs WITH Phase 1, per service)  │
│  Migrate Cloud SQL tables → Firestore collections per service    │
│  Dual-write → validate → cut over → decommission SQL tables      │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  PHASE 3 — Frontend Migration (4–6 weeks)                       │
│  Angular 6 → Angular 20, Ionic + Capacitor                      │
│  Business logic preserved, UI fully replaced                     │
│  Consumes the now-stable Spring API (same endpoints)             │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  PHASE 4 — Hardening & Decommission (2–3 weeks)                 │
│  Remove Java EE app + Cloud SQL, finalize CI/CD, load testing    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Why This Order Specifically

### Phase 0 before everything else
You cannot design a Firestore schema (document model) without knowing your microservice boundaries. You cannot define microservice boundaries without understanding the domain. Two weeks of design saves months of rework.

### Backend (Phase 1) before Frontend (Phase 3)
The frontend currently calls the Java EE API. If you migrate the frontend while also migrating the backend, you have two moving targets. By finishing the backend first (with the same API surface), the frontend migration works against a stable, known, modern API.

### Database (Phase 2) runs alongside Phase 1, not separately
Each microservice is built and deployed with its own Firestore storage. Migrating the database is part of deploying each service, not a separate big-bang event. This keeps the scope of each service migration small and testable.

### Frontend (Phase 3) last
Once the backend API is stable and the data layer is Firestore, the frontend migration is pure UI/framework work with no backend unknowns. This is the safest time to do it.

---

## Risk Model

| Risk | Mitigation |
|------|-----------|
| Backend rewrite breaks existing frontend | Strangler Fig: old Java EE app stays live until each service is validated |
| Data loss during SQL → Firestore migration | Dual-write period; Cloud SQL stays read-available for 30 days post-cutover |
| API contract drift during backend migration | Contract tests (Spring Cloud Contract) run on every PR |
| Frontend migration breaks mobile users | Progressive rollout; keep old Angular 6 app live until Angular 20 is validated |
| Microservice boundary mistakes | Domain model designed in Phase 0; ADRs reviewed before any code is written |

---

## Parallel Work Opportunities

These tracks can run in parallel once Phase 0 is done:

```
Phase 1+2 (backend + DB) ──────────────────────────► done
Phase 3 UI design/prototyping ──► Angular scaffold ─► integration ► done
                        ↑
              Can start UI work in parallel
              as long as it uses mock data
              until the real API is ready
```

The Angular 20 project can be scaffolded and UI components built against mock data while the backend is being migrated. Integration with the real API happens at the end of Phase 3.

---

## Deliverables Per Phase

| Phase | Deliverable |
|-------|------------|
| 0 | Firestore data model doc, microservice map, ADRs |
| 1 | Spring Boot services deployed behind API Gateway, old monolith retired |
| 2 | All data in Firestore, Cloud SQL decommissioned |
| 3 | Angular 20 app deployed (web + iOS + Android) |
| 4 | Full monitoring, load tests passing, old infra gone |
