# FitQuest

A pixel-art RPG-flavored gym and food tracker. Native Android app (Kotlin + Jetpack Compose) backed by a Node.js + PostgreSQL REST API.

```
fitquest/
├── backend/   ← Node.js + Express REST API (auth, data, RPG engine)
├── app/       ← Native Android app (Kotlin + Jetpack Compose)
├── Makefile   ← top-level shortcut (delegates to backend/Makefile)
└── README.md
```

## Quick start

### Backend

```bash
cd backend
cp .env.example .env
npm install
make migrate      # apply SQL migrations
make dev          # start dev server on :8080
```

Health check: `curl http://localhost:8080/health` → `{"status":"ok"}`

### Android

```bash
cd app
./gradlew assembleDebug
```

The backend URL is wired through `BuildConfig.BACKEND_URL` in `app/build.gradle.kts`.

## Visual identity

See `DESIGN.md` — Retro Heroic, midnight nebula palette, Space Mono everywhere, sharp 0px corners, 4px pixel shadows.

## Commit style

Conventional Commits in English: `feat:`, `fix:`, `test:`, `chore:`, `refactor:`, `docs:`.
