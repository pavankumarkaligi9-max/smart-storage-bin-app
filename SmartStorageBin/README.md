# Smart Storage Bin - Android Mobile Application

Modern native Android application developed for **Smart Storage Bin** — an intelligent mobile lifecycle-based file and storage manager.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (Material3 Design)
- **Architecture**: Clean Architecture / MVVM (Model-View-ViewModel)
- **Asynchronous**: Kotlin Coroutines & StateFlow
- **Networking**: Retrofit 2 & OkHttp 3 with Gson Converter
- **Image Loading**: Coil Compose
- **Permissions**: Runtime Permission Request Handling (`MANAGE_EXTERNAL_STORAGE`, `POST_NOTIFICATIONS`, `READ_MEDIA_*`)
- **Notifications**: NotificationCompat with High-Priority Expiry Notification Channel

---

## 📁 Project Directory Breakdown

```
app/src/main/java/com/smartstoragebin/
├── data/
│   ├── api/             # Retrofit ApiService, Client, Requests/Responses
│   ├── model/           # FileItem, Category, StorageMetrics, DuplicateGroup
│   ├── repository/      # FileRepository, StorageRepository, DuplicateRepository
│   └── sample/          # SampleData provider for mock/offline execution
├── notification/        # NotificationHelper (File expiry alerts & channels)
├── ui/
│   ├── components/      # Reusable Compose Widgets (FileCard, Dialogs, Charts, StoragePermissionDialog)
│   ├── navigation/      # NavGraph, BottomNavigation, Screen routes
│   ├── screens/         # Home, Files, Temporary, Storage, Duplicates, Bin, Settings
│   └── theme/           # Color, Theme, Type, Shapes
└── MainActivity.kt      # Main Activity entry point
```

---

## 🚀 How to Run in Android Studio

1. Open **Android Studio** (Hedgehog, Iguana, or Koala).
2. Select **File ➔ Open** and choose this directory (`SmartStorageBin`).
3. Wait for Gradle Sync to finish.
4. **API Base URL Configuration**:
   - For Android Emulator: Set `BASE_URL = "http://10.0.2.2:8080/api/"` in `RetrofitClient.kt`.
   - For Physical Device: Set `BASE_URL = "http://<YOUR_PC_IP>:8080/api/"` (make sure PC and phone are on the same Wi-Fi).
5. Click **Run 'app'** (Shift + F10) to launch on an emulator or connected phone.

---

## 🛡️ Permissions & System Notifications
- Automatically requests storage access upon first launch.
- Registers the `smart_storage_bin_file_expiry` notification channel to alert users when temporary files expire under the **Zero Auto-Deletion** policy.
