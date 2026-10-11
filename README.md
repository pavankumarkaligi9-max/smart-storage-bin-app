# Smart Storage Bin 📦
> **Mobile Lifecycle-Based Automated File Retention & Deduplication System**

Smart Storage Bin is an intelligent storage management suite designed for Android mobile devices and modern desktop web browsers. It eliminates smartphone storage clutter by introducing dynamic lifecycle retention pools, SHA-256 cryptographic duplicate detection, and a strict **Zero Auto-Deletion Policy** backed by actionable expiry alerts.

---

## 🏛️ System Architecture

```
[ Android Client (Jetpack Compose / Kotlin) ]
                     │
                     ├── REST API (Retrofit 2 / OkHttp)
                     ▼
[ Spring Boot 3.3.4 Microservice (Java 17 / JPA) ]
                     │
                     ├── Scheduled Cron Daemon (@Scheduled @ 00:00 UTC)
                     ▼
[ MySQL 8.0 Relational Storage & Metadata DB ]

                     ▲
                     │ Standalone Interactive Presentation
[ HTML5 / Tailwind CSS / Vanilla JS Web App ]
```

---

## 📂 Repository Structure

| Directory | Technology | Description |
|---|---|---|
| **[`SmartStorageBin/`](./SmartStorageBin)** | Kotlin • Jetpack Compose • MVVM | Native Android mobile application featuring lifecycle classification, runtime storage permissions, and notifications. |
| **[`SmartStorageBinBackend/`](./SmartStorageBinBackend)** | Java 17 • Spring Boot 3.3.4 • MySQL | Enterprise REST API backend with SHA-256 deduplication, lifecycle retention scheduler, and Docker Compose. |
| **[`SmartStorageBinWeb/`](./SmartStorageBinWeb)** | HTML5 • Tailwind CSS • JavaScript | Standalone, zero-cache interactive web application for live demonstrations, viva presentations, and instant deployment. |
| **[`PROJECT_SUBMISSION_GUIDE.md`](./PROJECT_SUBMISSION_GUIDE.md)** | Markdown Documentation | Capstone project defense guide, system architecture diagrams, and viva Q&A sheet. |

---

## 🌟 Key Features

1. **Dynamic Lifecycle Retention Pools**:
   - Classify incoming files into **Temporary** (1, 3, 7, 15, 30 days) or **Permanent** storage pools.
   - Real-time countdowns track remaining retention time.

2. **Zero Auto-Deletion Policy**:
   - Files are **never purged automatically** without explicit user consent.
   - When a temporary file reaches its retention deadline, a high-priority notification prompts the user to:
     - 🛡️ **Keep Permanent**
     - 📅 **Extend (+7 Days)**
     - 👁️ **Open & View**
     - 🗑️ **Move to Bin**

3. **SHA-256 Cryptographic Deduplication**:
   - Analyzes binary content using SHA-256 hashing to cluster identical files, regardless of differing file names.
   - Calculates recoverable wasted space across storage pools.

4. **Multi-Platform Experience**:
   - Native Android client with Material Design 3 and Android Notifications API.
   - Responsive web dashboard with live charts, category filters, file viewers, and mock API explorer.

---

## 🚀 Quick Start Guide

### 1. Running the Web Application
```bash
cd SmartStorageBinWeb
python server.py
# Open http://localhost:8088 in your browser
```
*(Or double-click `start_preview.bat` on Windows)*

### 2. Running the Spring Boot Backend
```bash
cd SmartStorageBinBackend
mvn clean spring-boot:run
# Server runs on http://localhost:8080
```

### 3. Running the Android App
1. Open the [`SmartStorageBin`](./SmartStorageBin) folder in **Android Studio**.
2. Sync Gradle dependencies.
3. Select an Android Emulator or physical device (Android 8.0+ / API 26+).
4. Click **Run (`Shift + F10`)**.

---

## 📜 Capstone Project Documentation
Detailed viva defense questions, architecture rationale, and evaluation answers are available in:
👉 **[`PROJECT_SUBMISSION_GUIDE.md`](./PROJECT_SUBMISSION_GUIDE.md)**

---

## 👤 Author
- **Developer**: Pavan Kumar
- **GitHub**: [@pavankumarkaligi9-max](https://github.com/pavankumarkaligi9-max)
- **Repository**: [smart-storage-bin-app](https://github.com/pavankumarkaligi9-max/smart-storage-bin-app)
