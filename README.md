# AtlasMemories

AtlasMemories is a Compose Multiplatform project built with Kotlin, targeting Android and Desktop (JVM).

## Project Structure

This project follows a multi-module architecture:

- **`:shared`**: Contains the core logic and UI components shared across platforms.
  - `commonMain`: Shared Compose UI and business logic.
  - `androidMain`: Android-specific implementations and resources.
  - `jvmMain`: Desktop-specific implementations and resources.
- **`:androidApp`**: The Android application entry point.
- **`:desktopApp`**: The Desktop (JVM) application entry point.

## Tech Stack

- **[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)**
- **[Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)** for declarative UI.
- **Material 3** for modern design components.
- **AndroidX Lifecycle** for state management.

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) or IntelliJ IDEA.
- JDK 17 or higher.

### Running the applications

#### Android
To assemble the debug APK:
```bash
./gradlew :androidApp:assembleDebug
```
You can also run it directly from Android Studio using the `androidApp` run configuration.

#### Desktop
To run the desktop application:
```bash
./gradlew :desktopApp:run
```
For development with hot reload:
```bash
./gradlew :desktopApp:hotRun --auto
```

## Testing

The project includes tests for both shared logic and platform-specific code.

- **Run all shared tests**: `./gradlew :shared:allTests`
- **Android Host tests**: `./gradlew :shared:testAndroidHostTest`
- **Desktop (JVM) tests**: `./gradlew :shared:jvmTest`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://www.jetbrains.com/help/compose-multiplatform-dev/getting-started.html).
