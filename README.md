# Learning Dashboard Mobile Application

## 1. Architecture
I implemented **Clean Architecture with MVVM and Unidirectional Data Flow (UDF)** using Jetpack Compose and Kotlin Coroutines/Flow.
* **Why:** Decouples UI from business logic and data sources. It makes unit testing straightforward and ensures predictable state management via `StateFlow`.
* **Flow:** `UI (Compose) -> ViewModel (UI State) -> Repository (SSOT) -> Room DB / Mock API`

## 2. Offline Support
* **Single Source of Truth (SSOT):** Room Database serves as the local cache.
* **Mechanism:** The repository first emits cached data from Room DB immediately so the user sees stored content. It then fetches fresh data from the API and updates Room. If the network request fails (e.g., device is offline), the app gracefully renders the cached data from Room DB without throwing an unhandled error state.

## 3. Security
In a production-ready application:
* **Token Storage:** EncryptedSharedPreferences (Android) / Keychain Services (iOS) to store OAuth JWT access/refresh tokens.
* **Network Layer:** SSL Pinning to prevent Man-In-The-Middle (MITM) attacks.
* **Auth Renewal:** Use an `Authenticator` interceptor (OkHttp) to automatically handle token refresh logic seamlessly when receiving HTTP `401 Unauthorized`.

## 4. Scalability (1M+ Users & Hundreds of Courses)
1. **Pagination & Lazy Loading:** Use `Paging 3` library for infinite scrolling of course catalogs instead of fetching all items at once.
2. **Database Indexing & Normalization:** Index `courseId` and `lessonId` in Room DB for fast lookup math.
3. **Delta Synchronization:** Sync only modified/updated lesson completion timestamps instead of posting the full course payload back to the server.
4. **Image Caching & CDN:** Use Coil/Nuke with memory + disk caching backed by a CDN for fast asset delivery.

## 5. Implementation on Second Platform (iOS / macOS)
If implementing this on iOS using Swift & SwiftUI:
* **UI:** SwiftUI views using `@StateObject` / `@ObservedObject` bound to `ObservableObject` ViewModels.
* **Database:** CoreData or SwiftData for local caching.
* **Networking:** `URLSession` with `async/await`.
* **State Management:** Combine framework or Swift Concurrency `AsyncStream`.