# 02 — Frontend Migration: Angular 6 → Angular 20

## Goal

Migrate the Angular 6 frontend to Angular 20 with:
- Modern architecture (standalone components, signals, zoneless)
- Same API calls — backend sees no change
- UI fully replaced (custom ad-hoc theme → Ionic design system)
- Single codebase for web (PWA) and mobile (iOS + Android via Capacitor)

---

## What to Keep vs. Replace

| Category | Action | Reason |
|----------|--------|--------|
| HTTP service methods (URLs, verbs, headers) | **Keep as-is** | Backend contract must not change |
| Model interfaces / DTOs | **Keep as-is** | Define API contract |
| Business logic in services | **Lift into SignalStore** | Preserve logic, modernise container |
| `NgModule`s | **Replace** with standalone components | Angular 6 pattern, no longer needed |
| Custom UI components | **Replace** with Ionic components | Poor maintainability, not mobile-ready |
| RxJS chains in components | **Replace** with signals where possible | |
| Routing | **Restructure** with lazy `loadComponent` | Modern Angular pattern |
| Zone.js | **Remove** once signals are adopted | Zoneless is the Angular 20 default |

---

## Project Architecture (Target)

```
libs/
  data-access/          ← services + HTTP calls (copied from Angular 6)
    anime/
      anime.service.ts       ← same HTTP calls, modernised syntax
      anime.store.ts         ← NgRx SignalStore wrapping the service
      anime.model.ts         ← same interfaces as before
    user/
    auth/

  feature/              ← smart (container) components, routing
    anime-list/
    anime-detail/
    profile/

  ui/                   ← dumb (presentational) Ionic components
    anime-card/
    anime-grid/
    nav-bar/

  util/                 ← pure functions, pipes, validators
```

---

## Step-by-Step Migration

### Step 1 — Scaffold the new Angular 20 project

```bash
npm install -g @angular/cli@latest
ng new otaku-ma --routing --style=scss --strict
cd otaku-ma
```

Configure path aliases in `tsconfig.json`:

```json
{
  "compilerOptions": {
    "paths": {
      "@data-access/*": ["libs/data-access/*"],
      "@feature/*": ["libs/feature/*"],
      "@ui/*": ["libs/ui/*"],
      "@util/*": ["libs/util/*"]
    }
  }
}
```

### Step 2 — Copy the API layer verbatim

From the Angular 6 project, copy every service file that contains `HttpClient` calls into `libs/data-access/`. Do not change URLs, HTTP methods, headers, or request/response shapes.

Update only the import syntax:

```ts
// Angular 6
import { HttpClient } from '@angular/common/http';
// Angular 20 — same import, still works
import { HttpClient } from '@angular/common/http';
```

Copy all model interfaces (`.model.ts` / `.interface.ts`) as-is.

### Step 3 — Configure HttpClient

In `app.config.ts` (replaces `AppModule`):

```ts
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    provideExperimentalZonelessChangeDetection(),
  ]
};
```

### Step 4 — Wrap business logic in SignalStore

For each domain service, create a store:

```ts
// libs/data-access/anime/anime.store.ts
export const AnimeStore = signalStore(
  withState<AnimeState>({ animes: [], selected: null, loading: false }),
  withMethods((store, service = inject(AnimeService)) => ({
    loadAnimes: rxMethod<void>(pipe(
      tap(() => patchState(store, { loading: true })),
      switchMap(() => service.getAnimes()),
      tapResponse({
        next: animes => patchState(store, { animes, loading: false }),
        error: () => patchState(store, { loading: false })
      })
    ))
  }))
);
```

### Step 5 — Set up Ionic for shared UI

```bash
npm install @ionic/angular@latest
ng add @ionic/angular
```

Use standalone Ionic components (no `IonicModule` needed):

```ts
import { IonContent, IonCard, IonButton, IonList } from '@ionic/angular/standalone';
```

Apply Ionic's theming with anime/game-appropriate CSS variables:

```scss
// src/theme/variables.scss
:root {
  --ion-color-primary: #e94560;       // anime red
  --ion-color-secondary: #0f3460;     // dark navy
  --ion-background-color: #16213e;
  --ion-text-color: #eaeaea;
  --ion-font-family: 'Rajdhani', sans-serif;
}
```

### Step 6 — Build feature + UI components

- Feature components (`libs/feature/`) inject the SignalStore and pass data down via `input()`.
- UI components (`libs/ui/`) are pure `@input()` / `@output()` with no service dependencies.

```ts
// Smart container
@Component({ providers: [AnimeStore] })
export class AnimeListPage {
  store = inject(AnimeStore);
  ngOnInit() { this.store.loadAnimes(); }
}
```

```html
<!-- passes data to dumb component -->
<app-anime-grid [animes]="store.animes()" (selected)="store.select($event)" />
```

### Step 7 — Set up PWA

```bash
ng add @angular/pwa
```

Configure `ngsw-config.json` for API caching:

```json
{
  "dataGroups": [{
    "name": "api",
    "urls": ["/api/**"],
    "cacheConfig": { "strategy": "freshness", "maxAge": "30m", "timeout": "5s" }
  }]
}
```

### Step 8 — Add Capacitor for mobile

```bash
npm install @capacitor/core @capacitor/cli
npx cap init "otaku.ma" "ma.otaku.app"
npm install @capacitor/ios @capacitor/android
npx cap add ios
npx cap add android
```

Build and sync workflow:

```bash
ng build
npx cap sync
npx cap open ios      # Xcode
npx cap open android  # Android Studio
```

### Step 9 — Enable zoneless & SSR

Remove `zone.js` from `polyfills`:

```json
// angular.json — remove this line
"polyfills": ["zone.js"]
```

Add SSR for web:

```bash
ng add @angular/ssr
```

---

## Checklist

- [ ] Scaffold Angular 20 project with lib folder structure
- [ ] Copy all HTTP service methods (URLs unchanged)
- [ ] Copy all model interfaces / DTOs
- [ ] Configure `provideHttpClient()` with auth interceptor
- [ ] Create NgRx SignalStore per domain
- [ ] Integrate Ionic 8 (standalone imports)
- [ ] Build feature (smart) components
- [ ] Build UI (dumb) Ionic components
- [ ] Apply anime/game theme via CSS variables
- [ ] Set up `@angular/pwa` with caching config
- [ ] Add Capacitor, configure iOS + Android
- [ ] Enable zoneless change detection
- [ ] Add SSR + hydration (web)
- [ ] Achieve Lighthouse PWA ≥ 90
- [ ] Deploy and validate API calls match old behaviour exactly

---

## References

- [Angular Update Guide](https://update.angular.io/)
- [NgRx SignalStore](https://ngrx.io/guide/signals/signal-store)
- [Ionic Angular Standalone](https://ionicframework.com/docs/angular/overview)
- [Capacitor Docs](https://capacitorjs.com/docs)
