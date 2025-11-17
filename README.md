# LifeLog: Mood • Sleep • Energy Tracker

---
### English
This is the Android source code for the LifeLog application, a native Android app for tracking mood, energy, sleep, and more, as outlined in the technical specification.

---
### Русский
Это исходный код Android-приложения LifeLog — нативного приложения для отслеживания настроения, энергии, сна и других показателей в соответствии с техническим заданием.

---

## How to Build / Как собрать проект

### English

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

### Русский

Проект использует Gradle в качестве системы сборки. Вы можете собрать проект из командной строки или с помощью Android Studio.

**Командная строка:**

1.  Склонируйте репозиторий.
2.  Перейдите в корневой каталог проекта.
3.  Предоставьте права на выполнение для Gradle wrapper: `chmod +x gradlew`
4.  Выполните команду сборки: `./gradlew build`

**Android Studio:**

1.  Откройте проект в Android Studio.
2.  Дождитесь завершения синхронизации Gradle.
3.  Перейдите в `Build > Make Project`.

---

## How to Run / Как запустить проект

### English

You can run the application on an Android emulator or a physical device directly from Android Studio.

1.  Select a run configuration (usually the `:app` module).
2.  Choose a target device.
3.  Click the "Run" button.

### Русский

Вы можете запустить приложение на эмуляторе Android или на физическом устройстве прямо из Android Studio.

1.  Выберите конфигурацию запуска (обычно это модуль `:app`).
2.  Выберите целевое устройство.
3.  Нажмите кнопку "Run".

---

## Permissions / Разрешения

### English

The application requires the following permissions to function correctly:

*   `android.permission.CAMERA`: To record video notes.
*   `android.permission.RECORD_AUDIO`: To capture audio for video notes.
*   `android.permission.POST_NOTIFICATIONS` (on Android 13+): To show reminders for medications.

The app will request these permissions at runtime when you first access the relevant feature.

### Русский

Приложению для корректной работы требуются следующие разрешения:

*   `android.permission.CAMERA`: для записи видеозаметок.
*   `android.permission.RECORD_AUDIO`: для записи звука в видеозаметках.
*   `android.permission.POST_NOTIFICATIONS` (на Android 13+): для отображения напоминаний о приеме лекарств.

Приложение запросит эти разрешения во время выполнения при первом доступе к соответствующей функции.

---

## Test Plan / План тестирования

### English

The project includes a set of unit and instrumentation tests to ensure code quality and stability.

*   **Unit Tests**: Located in `feature/*/src/test`. These tests cover ViewModels and business logic. Run with `./gradlew test`.
*   **UI (Instrumentation) Tests**: Located in `feature/*/src/androidTest`. These tests verify the Compose UI components. Run from Android Studio or with `./gradlew connectedAndroidTest`.
*   **Lint**: The project is configured with Lint to check for code style and potential errors. Run with `./gradlew lint`.
*   **CI**: A GitHub Actions workflow is set up in `.github/workflows/android_ci.yml` to automatically build and test the project on every push.

### Русский

Проект включает набор юнит-тестов и инструментальных тестов для обеспечения качества и стабильности кода.

*   **Юнит-тесты**: Расположены в `feature/*/src/test`. Эти тесты покрывают ViewModel и бизнес-логику. Запуск: `./gradlew test`.
*   **UI-тесты (инструментальные)**: Расположены в `feature/*/src/androidTest`. Эти тесты проверяют UI-компоненты Compose. Запускаются из Android Studio или командой `./gradlew connectedAndroidTest`.
*   **Lint**: Проект настроен с использованием Lint для проверки стиля кода и потенциальных ошибок. Запуск: `./gradlew lint`.
*   **CI**: В `.github/workflows/android_ci.yml` настроен рабочий процесс GitHub Actions для автоматической сборки и тестирования проекта при каждом push-событии.

---

## Known Limitations (MVP) / Известные ограничения (MVP)

### English

This is an MVP implementation, and some features are simplified or not yet complete:

*   **Video Playback**: The UI for playing back recorded video notes is not yet implemented. Videos are saved, but cannot be viewed within the app.
*   **Trends**: The graphs on the Trends screen are very basic and serve as a placeholder for a more advanced charting library.
*   **Medication Reminders**: The scheduling logic is simplified for demonstration purposes (e.g., fires 10 seconds after adding a med). It does not yet account for the actual time of day.
*   **Language Switching**: The UI for switching languages is present, but the actual locale change within the running app (re-inflating the UI with new strings) is not fully implemented.
*   **Data Export/Import**: This feature, mentioned in the technical specification, is not implemented in the MVP.
*   **Error Handling**: Robust error handling (e.g., for file I/O, database errors) is minimal.
*   **UI Polish**: The UI is functional but lacks the final polish and custom components described in the visual references. Emoji selection for mood is a placeholder.

### Русский

Это MVP-реализация, и некоторые функции упрощены или еще не завершены:

*   **Воспроизведение видео**: Пользовательский интерфейс для воспроизведения записанных видеозаметок еще не реализован. Видео сохраняются, но их нельзя просмотреть в приложении.
*   **Тренды**: Графики на экране "Тренды" очень простые и служат заглушкой для более продвинутой библиотеки диаграмм.
*   **Напоминания о лекарствах**: Логика планирования упрощена для демонстрационных целей (например, срабатывает через 10 секунд после добавления лекарства). Она еще не учитывает реальное время суток.
*   **Переключение языка**: Пользовательский интерфейс для переключения языков присутствует, но фактическое изменение локали в работающем приложении (пересоздание UI с новыми строками) не реализовано полностью.
*   **Экспорт/импорт данных**: Эта функция, упомянутая в техническом задании, не реализована в MVP.
*   **Обработка ошибок**: Надежная обработка ошибок (например, для файлового ввода-вывода, ошибок базы данных) минимальна.
*   **Полировка UI**: Пользовательский интерфейс функционален, но ему не хватает финальной полировки и кастомных компонентов, описанных в визуальных референсах. Выбор эмодзи для настроения является заглушкой.
