# Smart Storage Bin - Capstone Project Master Submission Guide

## 📌 Project Overview
**Smart Storage Bin** is an intelligent mobile lifecycle-based file and storage management system engineered to solve unmanaged storage clutter on Android smartphones. 

Traditional mobile operating systems lack metadata-driven automated retention models, resulting in downloads, duplicate images, and temporary documents permanently consuming internal storage. Smart Storage Bin introduces:
1. **Dynamic Lifecycle Retention Pools**: Temporary (1, 3, 7, 15, 30, 60 days) vs Permanent classification.
2. **Zero Auto-Deletion Policy**: Files are never purged without explicit user notification and review options (Extend +7 Days, Keep Permanent, Move to Bin).
3. **SHA-256 Byte-Level Deduplication Engine**: Clusters exact duplicates regardless of differing file names.
4. **Interactive In-Browser File Viewer & Live Editor**: View, edit, preview, and download documents, PDFs, photos, and videos.
5. **System Push Notifications & Runtime Storage Permissions**: Real Android & Web Notifications trigger the instant a file's retention time expires.

---

## 🏛️ System Architecture

```
[ Android Mobile Client (Jetpack Compose / Kotlin) ]
                 │
                 ├── REST API Calls (Retrofit 2 / OkHttp)
                 ▼
[ Spring Boot 3.3.4 REST API Backend (Java 17) ]
                 │
                 ├── Spring Data JPA / Hibernate
                 ▼
[ MySQL 8.0 Relational Database Schema ]
                 │
                 └── Scheduled Cron Jobs (FileLifecycleScheduler @ 00:00 UTC)
```

---

## 📂 Project Repository Structure

| Folder | Technology | Files | Description |
|---|---|---|---|
| [`SmartStorageBin`](file:///C:/Users/Pavan%20Kumar/.gemini/antigravity/scratch/SmartStorageBin) | Kotlin / Jetpack Compose | 63 files | Native Android mobile app with MVVM architecture, storage permissions, and notifications. |
| [`SmartStorageBinBackend`](file:///C:/Users/Pavan%20Kumar/.gemini/antigravity/scratch/SmartStorageBinBackend) | Spring Boot 3.3.4 / MySQL | 51 files | Enterprise REST API backend, Docker Compose, Flyway/JPA schema, SHA-256 deduplication, JUnit tests. |
| [`SmartStorageBinWeb`](file:///C:/Users/Pavan%20Kumar/.gemini/antigravity/scratch/SmartStorageBinWeb) | HTML5 / Tailwind / Vanilla JS | 6 files | Complete, standalone, responsive web application for instant presentation, viva demo, and public deployment. |

---

## 🚀 Live Demonstration Links

- 🌐 **Active Public Tunnel**: **[https://f41daa51f3e2627a-157-50-156-28.serveousercontent.com](https://f41daa51f3e2627a-157-50-156-28.serveousercontent.com)**
- 📶 **Wi-Fi Network Access**: **`http://10.43.246.147:8085`**
- 💻 **Localhost**: **`http://localhost:8085`**

---

## 🎯 Viva & Defense Q&A Sheet

### Q1: What makes Smart Storage Bin different from the stock Android Files app?
**A:** The stock Android Files app only offers reactive storage cleanup (identifying junk files after storage is full). Smart Storage Bin is **proactive**: it intercepts new files at ingestion, enforces retention lifetimes (e.g., 7-day temporary notes), computes cryptographic SHA-256 hashes to prevent duplicate storage, and alerts the user when deadlines complete under a strict zero auto-deletion policy.

### Q2: How does the SHA-256 deduplication work?
**A:** Instead of relying on file names (which can be altered), the app reads the binary content and computes an irreversible 256-bit cryptographic hash. If two files have identical hashes, they share identical byte sequences and are grouped into a duplicate cluster, indicating recoverable wasted space.

### Q3: What happens when a temporary file's time is completed?
**A:** Under our zero auto-deletion policy, the system generates a high-priority system push notification and audio chime alerting the user. The user is presented with 4 choices:
1. **Convert to Permanent**: Clears expiry and keeps the file forever.
2. **Extend (+7 Days)**: Increments the retention countdown.
3. **Open & View**: Inspects the file contents.
4. **Move to Bin**: Safely moves it to the two-phase Recycle Bin.
