# My Expense Tracker

## Overview
**My Expense Tracker** is a comprehensive Android mobile application designed to help users manage their personal finances effectively. It allows users to log daily expenses, view transaction history, and analyze spending habits through a modern, user-friendly interface. The app features cloud synchronization, multi-user support, and a professional dark/light theme.

## Features

### Core Functionality
*   **User Authentication:** Secure sign-up and login using Firebase Authentication. Each user has a private, isolated account.
*   **Expense Logging:** Add expenses with a title, amount (in PKR), and date.
*   **Dashboard:** View a scrollable list of all personal expenses.
*   **Statistics:** See total spending and transaction counts on a dedicated stats screen.
*   **Cloud Sync:** All data is stored in Google Cloud Firestore, ensuring expenses are synced across devices in real-time.
*   **Offline Support:** Data is also cached locally using SQLite for offline access.

### Advanced Features
*   **Dynamic Theme Switching:** Toggle between a professional **Light Mode** and **Dark Mode** with a single tap.
*   **Location Awareness:** The app checks for enabled Location Services on launch and prompts the user if disabled (Mandatory requirement).
*   **Daily Notifications:** Receive a daily reminder at 8:00 PM to log your expenses.
*   **Password Visibility:** Toggle password visibility on login and registration screens.

### Tech Stack
*   **Language:** Java
*   **UI:** XML, Material Design Components (BottomAppBar, FloatingActionButton, Cards)
*   **Architecture:** MVC Pattern
*   **Backend:** Firebase Authentication, Cloud Firestore
*   **Local Database:** SQLite (SQLiteOpenHelper)

## Setup Instructions

1.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Firebase Setup:**
    *   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add an Android app with package name `com.example.openendedlab`.
    *   Download `google-services.json` and place it in the `app/` directory.
    *   Enable **Authentication** (Email/Password provider).
    *   Enable **Cloud Firestore** and set security rules to:
        ```javascript
        allow read, write: if request.auth != null;
        ```
3.  **Build & Run:**
    *   Open the project in Android Studio.
    *   Sync Gradle files.
    *   Run on an emulator or physical device.

## Usage
1.  **Register:** Create a new account. You will be redirected to login.
2.  **Login:** Sign in with your credentials.
3.  **Add Expense:** Tap the "+" button to add a transaction.
4.  **View Stats:** Tap "Stats" to see your total spending.
5.  **Toggle Theme:** Tap the Moon/Sun icon in the top/login bar to switch themes.

## License
This project is an open-ended lab submission for the Mobile Application Development course.
