# Wikipedia's Hackathon Messina - 2-4 October '26

Wikingo originated at the Wikipedia Hackathon held in Messina in October 2026. The concept is to enable users to expand their knowledge through a game based on daily streaks, covering a wide range of topics; each chapter addresses different themes, allowing users to learn even when they answer a quiz question incorrectly. The project is fully open-source, welcoming improvements from external contributors.

## Content sources, attribution, and licensing

Wikingo uses Wikipedia as its primary source of educational content. Article
summaries are retrieved through the Wikipedia API and used to generate lessons,
questions, and explanations. Users can open the original Wikipedia article from
the app to consult the complete source and its attribution history.

Wikipedia content is generally available under the [Creative Commons
Attribution-ShareAlike 4.0 International License (CC BY-SA
4.0)](https://creativecommons.org/licenses/by-sa/4.0/), unless an article or
media file states otherwise. When redistributing or adapting Wikipedia text or
media, the applicable license terms must be followed, including attribution,
providing the license notice, and sharing adaptations under the same or a
compatible license where required. This repository does not claim ownership of
Wikipedia's articles, text, images, logos, or trademarks.

Wikingo is an independent open-source project and is not operated by or
officially affiliated with the Wikimedia Foundation. Contributors should verify
the license and attribution requirements for each external resource before
including it in the app.

# Web-App repository

To find the web code, this is the other repository: https://github.com/aresthebellator/HackathonMessina2026-WebApp.git

## Android Firebase and Google sign-in

1. Register an Android app in the `wikingo-auth` Firebase project with package name `com.exertia.wikingo`.
2. Add the SHA-1 signing fingerprints for the debug and release builds in the Firebase app settings, and enable Google as a sign-in provider in Firebase Authentication.
3. Download that Android app's `google-services.json` into `android-app/app/`.
4. Build the app. When the file is present, the Google Services Gradle plugin supplies the Android OAuth configuration. The app starts Firebase's Google OAuth flow directly, then completes authentication with Firebase.

Email/password and Google authentication use the bundled public project configuration as a
fallback, so the app can start Firebase authentication without `google-services.json`. Add
the file for the Android app's SHA-1/package registration and production Google OAuth setup.
