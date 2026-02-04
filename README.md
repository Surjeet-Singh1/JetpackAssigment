# Android Passwordless Auth App

This project demonstrates a passwordless authentication flow (Email + OTP) using Jetpack Compose, ViewModel, and StateFlow. It includes a session management screen with a live timer.

## Features
- **Email Login**: Simple email validation and OTP generation.
- **OTP Verification**: 
    - 6-digit OTP locally generated.
    - Expiry handling (60 seconds).
    - Maximum 3 attempts logic.
    - Resend capability (invalidates old OTP).
- **Session Management**:
    - Shows session start time.
    - Live duration timer (mm:ss) that survives recomposition.
    - Logout functionality.
- **Analytics**:
    - Integrated `Timber` for logging events (OTP generation, success, failure, logout).

## Architecture
The app follows the recommended **MVVM (Model-View-ViewModel)** architecture with **Unidirectional Data Flow (UDF)**.

- **UI Layer**: Jetpack Compose screens (`LoginScreen`, `OtpScreen`, `SessionScreen`) observing state.
- **ViewModel**: `AuthViewModel` manages `AuthState` and business logic.
- **State**: `AuthState` sealed interface representing strictly typed UI states (`LoggedOut`, `OtpSent`, `LoggedIn`).
- **Data Layer**: 
    - `OtpManager`: Singleton object handling OTP generation, storage, and validation rules.
    - `AnalyticsLogger`: Wrapper around Timber for structured logging.

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose (Material3)
- **State Management**: ViewModel, StateFlow
- **Concurrency**: Coroutines
- **Logging**: Timber

## Key Design Decisions
1.  **OTP Logic**: Implemented in `OtpManager` to keep the ViewModel clean. It uses a `Map` to store OTP data keyed by email, allowing multiple potential users (though the UI currently flows for one). `System.currentTimeMillis()` is used for expiry checks.
2.  **Session Timer**: The session timer is a `Flow` in the ViewModel. This ensures that the timer continues correctly even if the screen rotates or recomposes, as it depends on the session start timestamp, not a UI counter.
3.  **Navigation**: Simple state-based navigation within `MainActivity` for simplicity given the scope. For larger apps, `Jetpack Navigation` would be used.
4.  **External SDK**: Chosen **Timber** for analytics logging as it provides a clean, extensive API for logging that can be easily piped to backend analytics tools in a real production environment.

## Setup Instructions
1.  Open the project in Android Studio.
2.  Sync Gradle files.
3.  Run on an Emulator or Physical Device.
4.  Check Logcat with tag `AnalyticsLogger` to see generated OTPs (for testing) and event logs.

## Result 
![photo_6059704989711535909_y](https://github.com/user-attachments/assets/a342b813-9e75-4ac4-ab7b-2c8fc492a661)

![photo_6059704989711535908_y](https://github.com/user-attachments/assets/bb645fcc-02d3-4329-9d56-5ce4c9577c5f)

![photo_6059704989711535907_y](https://github.com/user-attachments/assets/bed77f79-ec05-4155-9111-45e627ad5e46)





