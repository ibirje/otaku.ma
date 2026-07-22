# Angular Migration Plan — Latest Angular (v20) for Browser & Phone

> **Target stack:** Angular 20 · Standalone Components · Signals · Zoneless · Ionic 8 + Capacitor 7 · PWA  
> **Goal:** A single codebase that runs as a high-quality web app **and** a native iOS/Android app.

---

## 1. Current-State Audit

Before touching code, snapshot what you have:

- [ ] Run `ng version` and record current Angular, Node, and TypeScript versions.
- [ ] List all third-party libraries and their versions (`npm list --depth=0`).
- [ ] Identify every `NgModule` that could become a standalone component/route.
- [ ] Note any use of `HttpClientModule`, `FormsModule`, `ReactiveFormsModule` (all now tree-shakable without a module wrapper).
- [ ] Check if the project already uses Ivy (everything after Angular 9 does).
- [ ] Review current build target in `angular.json` (`browser` → must change to `application`).

---

## 2. Upgrade Angular Incrementally

Angular recommends upgrading **one major version at a time**. Use the interactive guide at [update.angular.io](https://update.angular.io/).

```bash
# Repeat for each major version until you reach v20
npx ng update @angular/core@<next> @angular/cli@<next> --force
npm install
ng build   # fix any breaking changes before continuing
```

Key breaking changes to watch per version band:

| From → To | Notable change |
|-----------|---------------|
| ≤14 → 15  | Standalone APIs stable, `bootstrapApplication()` available |
| 15 → 16   | Signals preview, required inputs, `DestroyRef` |
| 16 → 17   | New control flow (`@if`, `@for`, `@switch`), `@defer`, new `application` builder (esbuild + Vite) |
| 17 → 18   | Zoneless change detection (experimental), `provideHttpClient()` with fetch backend |
| 18 → 19   | Incremental hydration, linked signals, `effect()` stabilised, resource API |
| 19 → 20   | Zoneless stable, full SSR hydration, `@let` template variable, signal-based forms preview |

---

## 3. Modernise the Codebase

### 3.1 Switch to the `application` builder

In `angular.json`, change the builder and remove the old Webpack-only options:

```json
"builder": "@angular-devkit/build-angular:application",
"options": {
  "browser": "src/main.ts",
  "outputPath": "dist/otaku-ma",
  "server": "src/main.server.ts",   // only if using SSR
  "ssr": { "entry": "server.ts" }
}
```

### 3.2 Convert to Standalone Components

Run the automated migration schematics:

```bash
ng generate @angular/core:standalone      # converts components
ng generate @angular/core:route-lazy-loading   # converts lazy routes
```

Replace `AppModule` with `bootstrapApplication()` in `main.ts`:

```ts
bootstrapApplication(AppComponent, appConfig);
```

### 3.3 Adopt Signals for State Management

- Replace `BehaviorSubject` / `Observable` properties with `signal()` / `computed()`.
- Use `input()`, `output()`, `model()` instead of `@Input()` / `@Output()`.
- Use `toSignal()` for existing RxJS streams you can't replace yet.
- Use `httpResource()` (Angular 19+) for simple data fetching instead of a full service + subscribe pattern.

### 3.4 Switch to New Control Flow

Run the automated migration:

```bash
ng generate @angular/core:control-flow
```

This converts `*ngIf`, `*ngFor`, `*ngSwitch` to `@if`, `@for`, `@switch`.

### 3.5 Enable Zoneless Change Detection (v18+)

In `appConfig`:

```ts
provideExperimentalZonelessChangeDetection()
```

Remove `zone.js` from `polyfills` in `angular.json` once all components are signal/`markForCheck` compatible.

### 3.6 Update HTTP Client

```ts
// app.config.ts
provideHttpClient(withFetch(), withInterceptors([authInterceptor]))
```

Remove any remaining `HttpClientModule` imports.

---

## 4. Browser App — PWA Setup

Install the official PWA schematic:

```bash
ng add @angular/pwa
```

This generates:
- `ngsw-config.json` — configure caching strategies here
- `manifest.webmanifest` — app name, icons, theme colour
- Registers the service worker in `app.config.ts`

#### Key PWA configuration tips

```json
// ngsw-config.json (example data strategy)
{
  "dataGroups": [{
    "name": "api-freshness",
    "urls": ["/api/**"],
    "cacheConfig": { "strategy": "freshness", "maxAge": "1h", "timeout": "3s" }
  }]
}
```

- Use `installable` prompt / "Add to Home Screen" via `SwUpdate` service.
- Target **Lighthouse PWA score ≥ 90** before releasing.

---

## 5. Phone App — Ionic + Capacitor Setup

### 5.1 Add Ionic to the Angular project

```bash
npm install @ionic/angular@latest @ionic/angular-toolkit@latest
ng add @ionic/angular
```

Use Ionic standalone components (no `IonicModule` needed in v7+):

```ts
import { IonButton, IonContent } from '@ionic/angular/standalone';
```

### 5.2 Add Capacitor

```bash
npm install @capacitor/core @capacitor/cli
npx cap init "otaku.ma" "ma.otaku.app"
npm install @capacitor/ios @capacitor/android
npx cap add ios
npx cap add android
```

### 5.3 Build & Sync Workflow

```bash
ng build                  # Angular production build
npx cap sync              # copy dist + sync native plugins
npx cap open ios          # open Xcode
npx cap open android      # open Android Studio
```

### 5.4 Essential Capacitor Plugins

| Need | Plugin |
|------|--------|
| Push notifications | `@capacitor/push-notifications` |
| Camera / gallery | `@capacitor/camera` |
| Filesystem | `@capacitor/filesystem` |
| Biometric auth | `@aparajita/capacitor-biometric-auth` |
| HTTP (native) | `@capacitor/http` (bypasses CORS on device) |
| Splash screen | `@capacitor/splash-screen` |
| Status bar | `@capacitor/status-bar` |

### 5.5 Adaptive UI Strategy

- Use CSS variables and Ionic's theming tokens so components adapt automatically to iOS / Android / web.
- Use `@media (prefers-color-scheme: dark)` + Ionic's dark mode variables.
- Use `Platform` from Ionic to conditionally adjust behaviour (`isPlatform('ios')`, `isPlatform('android')`).

---

## 6. Performance Best Practices

- **SSR + Hydration** (web only): add `ng add @angular/ssr` for initial page load performance and SEO.
- **Incremental hydration** (`@defer` blocks): lazy-hydrate below-the-fold content.
- **Image optimisation**: use `NgOptimizedImage` (`<img ngSrc="...">`) everywhere.
- **Code splitting**: all routes should be lazy (`loadComponent` / `loadChildren`).
- **Track by in `@for`**: always use `track item.id`.
- **Bundle analysis**: run `ng build --stats-json && npx webpack-bundle-analyzer dist/stats.json` periodically.

---

## 7. Tooling & DX

| Tool | Purpose |
|------|---------|
| ESLint + `angular-eslint` | `ng add @angular-eslint/schematics` |
| Prettier | consistent formatting |
| Husky + lint-staged | pre-commit hooks |
| Vitest or Jest | unit tests (Vitest is faster) |
| Playwright | e2e (replaces Protractor/Karma e2e) |
| Storybook | component development in isolation |

---

## 8. CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/ci.yml (sketch)
jobs:
  build-web:
    steps:
      - run: npm ci
      - run: ng lint
      - run: ng test --watch=false
      - run: ng build

  build-android:
    needs: build-web
    steps:
      - run: npx cap sync android
      - run: ./gradlew assembleRelease   # inside android/

  build-ios:
    needs: build-web
    runs-on: macos-latest
    steps:
      - run: npx cap sync ios
      - run: xcodebuild -scheme App archive ...
```

---

## 9. Migration Checklist

- [ ] Audit current versions and dependencies
- [ ] Upgrade Angular step-by-step to v20
- [ ] Switch build system to `application` builder (esbuild)
- [ ] Convert NgModules → Standalone Components
- [ ] Migrate to new control flow (`@if` / `@for`)
- [ ] Adopt Signals (state, inputs, outputs)
- [ ] Enable zoneless change detection
- [ ] Set up PWA with `@angular/pwa`
- [ ] Integrate Ionic 8 with standalone component imports
- [ ] Integrate Capacitor 7 (iOS + Android)
- [ ] Add essential Capacitor plugins
- [ ] Enable SSR + incremental hydration (web)
- [ ] Apply `NgOptimizedImage` across the app
- [ ] Add `angular-eslint`, Prettier, Husky
- [ ] Write/update unit tests (Vitest)
- [ ] Set up Playwright e2e
- [ ] Wire up CI/CD for web + mobile builds
- [ ] Achieve Lighthouse PWA score ≥ 90

---

## 10. References

- [Angular Update Guide](https://update.angular.io/)
- [Angular Signals](https://angular.dev/guide/signals)
- [Angular SSR](https://angular.dev/guide/ssr)
- [Ionic Angular](https://ionicframework.com/docs/angular/overview)
- [Capacitor Docs](https://capacitorjs.com/docs)
- [Angular PWA](https://angular.dev/ecosystem/service-workers)
