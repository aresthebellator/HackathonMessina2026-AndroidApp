# Wikipedia's Hackathon Messina - 2-4 October '26

Wikingo originated at the Wikipedia Hackathon held in Messina in October 2026. The concept is to enable users to expand their knowledge through a game based on daily streaks, covering a wide range of topics; each chapter addresses different themes, allowing users to learn even when they answer a quiz question incorrectly. The project is fully open-source, welcoming improvements from external contributors.

# Web-App repository

To find the web code, this is the other repository: https://github.com/aresthebellator/HackathonMessina2026-WebApp.git

## Android Firebase and Google sign-in

1. Register an Android app in the `wikingo-auth` Firebase project with package name `com.exertia.wikingo`.
2. Add the SHA-1 signing fingerprints for the debug and release builds in the Firebase app settings, and enable Google as a sign-in provider in Firebase Authentication.
3. Download that Android app's `google-services.json` into `android-app/app/`.
4. Build the app. The Google Services Gradle plugin is applied when this file is present; its generated `default_web_client_id` is used by Credential Manager to obtain a Google ID token, which the app exchanges with Firebase Authentication.

Email/password Firebase authentication uses the bundled public project configuration as a
fallback, so the app remains usable without `google-services.json`. Add the file to enable
the Android-specific OAuth configuration required by Google Sign-In.
