# LifeLog: Mood • Sleep • Energy Tracker

This is the Android source code for the LifeLog application, a native Android app for tracking mood, energy, sleep, and more, as outlined in the technical specification.

## How to Build

The project uses Gradle as its build system. You can build the project from the command line or using Android Studio.

**Command Line:**

1.  Clone the repository.
2.  Navigate to the project's root directory.
3.  Grant execution permissions to the Gradle wrapper: `chmod +x gradlew`
4.  Run the build command: `./gradlew build`

**Android Studio:**

1.  Open the project in Android Studio.
2.  Let Gradle sync complete.
3.  Go to `Build > Make Project`.

## How to Run

You can run the application on an Android emulator or a physical device directly from Android Studio.

1.  Select a run configuration (usually the `:app` module).
2.  Choose a target device.
3.  Click the "Run" button.

## Permissions

The application requires the following permissions to function correctly:

*   `android.permission.CAMERA`: To record video notes.
*   `android.permission.RECORD_AUDIO`: To capture audio for video notes.
*   `android.permission.POST_NOTIFICATIONS` (on Android 13+): To show reminders for medications.

The app will request these permissions at runtime when you first access the relevant feature.

## Test Plan

The project includes a set of unit and instrumentation tests to ensure code quality and stability.

*   **Unit Tests**: Located in `feature/*/src/test`. These tests cover ViewModels and business logic. Run with `./gradlew test`.
*   **UI (Instrumentation) Tests**: Located in `feature/*/src/androidTest`. These tests verify the Compose UI components. Run from Android Studio or with `./gradlew connectedAndroidTest`.
*   **Lint**: The project is configured with Lint to check for code style and potential errors. Run with `./gradlew lint`.
*   **CI**: A GitHub Actions workflow is set up in `.github/workflows/android_ci.yml` to automatically build and test the project on every push.

## Known Limitations (MVP)

This is an MVP implementation, and some features are simplified or not yet complete:

*   **Video Playback**: The UI for playing back recorded video notes is not yet implemented. Videos are saved, but cannot be viewed within the app.
*   **Trends**: The graphs on the Trends screen are very basic and serve as a placeholder for a more advanced charting library.
*   **Medication Reminders**: The scheduling logic is simplified for demonstration purposes (e.g., fires 10 seconds after adding a med). It does not yet account for the actual time of day.
*   **Language Switching**: The UI for switching languages is present, but the actual locale change within the running app (re-inflating the UI with new strings) is not fully implemented.
*   **Data Export/Import**: This feature, mentioned in the technical specification, is not implemented in the MVP.
*   **Error Handling**: Robust error handling (e.g., for file I/O, database errors) is minimal.
*   **UI Polish**: The UI is functional but lacks the final polish and custom components described in the visual references. Emoji selection for mood is a placeholder.
