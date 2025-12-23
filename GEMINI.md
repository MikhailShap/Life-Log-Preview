# GEMINI.md — Context & Guidelines for Life Log Project

## 1. Project Overview
**Name:** Life Log
**Purpose:** Android application for tracking personal well-being (Mood, Sleep, Meds, Side Effects, Video Notes).
**State:** Production Ready (v1.0+).

## 2. Tech Stack (Strict Adherence)
* **Language:** Kotlin (Strictly strongly typed).
* **UI Framework:** Jetpack Compose (Material3).
* **Architecture:** Clean Architecture + MVVM + MVI (Unidirectional Data Flow).
* **DI:** Hilt (Dagger).
* **Async:** Coroutines + Flow.
* **Database:** Room (SQLite).
* **Media:** CameraX (Video Recording), Media3 ExoPlayer (Playback).
* **Background Work:** WorkManager (for Notifications/Reminders).
* **Build System:** Gradle Kotlin DSL (Multi-module strategy compliant).

## 3. Project Structure
The project is vertically sliced by features, horizontally layered by concern.

### A. App Module (`:app`)
* **Role:** The entry point. Connects all feature modules.
* **Key Files:** `MainActivity.kt` (Must extend `AppCompatActivity` for locale switching), `LifeLogApp.kt` (Hilt Root).

### B. Core Modules (`:core:...`)
* `core:ui`: Theme, Colors, Typography, Common Composables. **NO business logic.**
* `core:domain`: Pure Kotlin. Models, Repository Interfaces, UseCases. **NO Android framework dependencies** (except `@Parcelize`).
* `core:data`: Room DB, DAOs, Repository Implementations, Mappers. Depends on `:core:domain`.
* `core:common`: Utility classes, Extensions.

### C. Feature Modules (`:feature:...`)
* `feature:log`: Dashboard with Drawer Navigation (`LogRootScreen`).
* `feature:meds`: Medication tracking + Reminder Worker.
* `feature:trends`: Analytics & Canvas Charts.
* `feature:videonotes`: Camera & Player logic.
* `feature:settings`: Profile, Theme, Language.
* `feature:today`: Sleep tracking.
* `feature:sideeffects`: Symptoms tracking.

**Dependency Rule:** `Feature` -> `Core:Domain` & `Core:UI`. Feature should NOT depend on `Core:Data` directly (use Hilt injection).

## 4. Coding Standards (The "Life Log" Way)

### 4.1. Clean Architecture & Data Flow
1.  **Source of Truth:** The Database (Room) is the single source of truth.
2.  **Data Flow:** `DAO (Entity)` -> `Repository (Map to Domain Model)` -> `UseCase` -> `ViewModel` -> `UI (State)`.
3.  **Never pass Entities to UI:** Always define a separate Domain Model in `core:domain`.
4.  **UseCases:** Required for modifying data (Insert/Update/Delete). Optional for simple reads (Repository access allowed in VM for simple `getFlow`).

### 4.2. Jetpack Compose UI
1.  **State Hoisting:** Screens are stateless. ViewModels hold state (`StateFlow`).
2.  **Events:** UI sends events to ViewModel (e.g., `fun onEvent(event: ScreenEvent)`).
3.  **Canvas:** Complex charts (Trends) must be drawn using `Canvas`, not heavy external libraries.

### 4.3. Critical System Features (Do Not Break)
* **Localization:** Language switching relies on `AppCompatDelegate.setApplicationLocales`. `MainActivity` MUST extend `AppCompatActivity`.
* **Theme:** Dark/Light mode is handled via DataStore `ThemeMode`.
* **Navigation:** The "Log" tab uses a nested `ModalNavigationDrawer` inside `LogRootScreen`. Do not remove this hierarchy.

### 4.4 Testing Strategy
1.  **Unit Tests:** All UseCases and ViewModels must have JUnit 5 tests. Use Mockk for mocking.
2.  **Naming:** `fun functionality_scenario_expectedResult()`.
3.  **Structure:** Arrange-Act-Assert (AAA) pattern inside tests.

## 5. How to Implement New Features (Prompting Guide)
When generating code, follow this sequence:
1.  **Domain:** Define `Model` and `Repository Interface`.
2.  **Data:** Create `Entity`, `DAO`, `Repository Implementation`, and `Mapper`.
3.  **DI:** Bind the Repository in a Hilt Module.
4.  **Presentation:** Create `ViewModel` and `Screen` (Composable).
5.  **Integration:** Add to `MainActivity` navigation or `LogRootScreen` drawer.

## 6. Known Limitations / TODOs
* **Backup:** Local JSON export/import is planned (partially implemented in Settings).
* **Video:** Video files are stored in internal app storage.