# MangaTies 📖

MangaTies is a modern, open-source Android application designed to provide a seamless reading and discovery experience for manga enthusiasts. Built with **Jetpack Compose** and **Kotlin**, this app leverages the latest Android architectural patterns to ensure performance, maintainability, and a delightful user experience.

---

## 🚀 Features
- **Manga Discovery**: Browse and search through extensive manga libraries.
- **User Authentication**: Secure sign-in powered by Firebase Auth.
- **Personalized Library**: Save your favorite titles to your personal bookshelf.
- **Modern UI**: Built entirely with Jetpack Compose Material3 for a consistent, high-quality look and feel.
- **Offline Capabilities**: Leveraging Room Database to manage local data efficiently.

---

## 🛠 Tech Stack & Architecture
MangaTies follows the [Google Recommended Architecture](https://developer.android.com/topic/architecture) to ensure clean separation of concerns:

*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
*   **Asynchronous Processing**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
*   **Networking**: [Retrofit](https://square.github.io/retrofit/)
*   **Database**: [Room](https://developer.android.com/training/data-storage/room)
*   **Backend/Auth**: [Firebase](https://firebase.google.com/) (Auth, Firestore, Storage)
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/)

---

## 🛠 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (latest version recommended)
- JDK 17 or higher
- A [Firebase Project](https://console.firebase.google.com/)

### Setup Instructions
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/devchaos0/MangaTies.git
    ```
    
2.  **Firebase Configuration**:
    - Create a new project in the Firebase Console.
    - Add an Android app to your project using your package name: `com.chaos.mangaties`.
    - Download the `google-services.json` file and place it in the `app/` folder of your project.
    - Enable **Email/Password** authentication in the Firebase Authentication console.

3.  **Build and Run**:
    - Open the project in Android Studio.
    - Perform a Gradle Sync (`File > Sync Project with Gradle Files`).
    - Connect your device or emulator and press **Run**.

---

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request, or simply open an issue with the tag "enhancement".

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## ⚖️ License
Distributed under the [MIT License](https://opensource.org/licenses/MIT). See `LICENSE` for more information.

---

*Built with ❤️ by the MangaTies Team*
