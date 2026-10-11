# 🚀 Smart Storage Bin — Spring Boot REST Backend

> **Production-grade REST API backend for the Smart Storage Bin mobile application (B.Tech Capstone Project).**

---

## 🏛️ Architecture & Layered Design

```
[ Android Client / Retrofit ]
          │ (HTTP JSON REST)
          ▼
[ Controller Layer ]      -> Handles HTTP mappings, Swagger OpenAPI, and request validation
          │
          ▼
[ Service Layer ]         -> Business logic, SHA-256 duplicate detection, category deduction, lifecycle & storage calculations
          │
          ▼
[ Repository Layer ]      -> Spring Data JPA queries & projections
          │
          ▼
[ Database (MySQL) ]      -> smart_file_manager database schema
```

---

## 🛠️ Technology Stack

- **Language**: Java 17+
- **Framework**: Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Validation, Spring Scheduling)
- **Database**: MySQL 8.x / (H2 in-memory supported for testing)
- **Build Tool**: Maven
- **Hashing**: SHA-256 (`java.security.MessageDigest`)
- **API Documentation**: SpringDoc OpenAPI 2.6.0 (Swagger UI)
- **Testing**: JUnit 5, Mockito

---

## 📁 Package Structure

```
com.smartstoragebin.backend
├── config/             # CORS configuration, Swagger OpenAPI configuration, DataInitializer
├── controller/         # REST Controllers (Files, Search, Categories, Duplicates, Storage, Bin, Notifications, Settings)
├── dto/                # Request and Response DTOs (Strict separation from database entities)
├── entity/             # JPA Entities (FileEntity, BinFileEntity, SettingsEntity, NotificationEntity)
│   └── enums/          # Enums (FileStatus, CategoryType, NotificationType, DuplicateAction)
├── exception/          # Custom exceptions & GlobalExceptionHandler (@RestControllerAdvice)
├── repository/         # Spring Data JPA Repository interfaces
├── scheduler/          # FileLifecycleScheduler (@Scheduled background jobs)
├── service/            # Service interfaces
│   └── impl/           # Business logic implementations
└── util/               # SHA-256 Hash utility & MIME/Extension Category detector
```

---

## ⚙️ Configuration & Prerequisites

### 1. MySQL Database Setup
Ensure MySQL is running and execute:
```sql
CREATE DATABASE IF NOT EXISTS smart_file_manager;
```
*(The schema and seed data are automatically initialized by Spring Boot on startup via `schema.sql`, `data.sql`, and `DataInitializer`).*

### 2. Configure `application.yml`
Located at `src/main/resources/application.yml`:
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_file_manager?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: YOUR_MYSQL_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  expiry:
    warning-days: 3
  temporary:
    default-days: 7
  storage:
    total-bytes: 137438953472 # 128 GB
    warning-threshold-percent: 80
  bin:
    retention-days: 30
```

---

## 🚀 Building and Running

### 1. Build project with Maven:
```bash
mvn clean package
```

### 2. Run unit tests:
```bash
mvn test
```

### 3. Run application:
```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`.

---

## 📖 Swagger / OpenAPI Interactive Documentation

Open in your browser:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🌐 Complete REST API Endpoint Reference

### 1. File Management (`/api/files`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/files` | List all active files (excluding bin) |
| `GET` | `/api/files/{id}` | Get file metadata by ID |
| `POST` | `/api/files` | Register/upload new file with SHA-256 hash |
| `PUT` | `/api/files/{id}` | Update file name or path |
| `DELETE` | `/api/files/{id}` | Move file to Recycle Bin |
| `GET` | `/api/files/temporary` | List all active temporary files |
| `GET` | `/api/files/permanent` | List all permanent files |
| `GET` | `/api/files/expired` | List temporary files whose expiry period has ended |
| `PUT` | `/api/files/{id}/extend` | Extend retention period of a temporary file |
| `PUT` | `/api/files/{id}/convert-permanent` | Convert temporary file to permanent (cancels expiry) |
| `GET` | `/api/files/temporary/summary` | Get temporary dashboard statistics |

### 2. Global Search & Filters (`/api/search`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/search` | Multi-criteria search (`query`, `category`, `status`, `minimumSize`, `maximumSize`, `createdDate`, `expiryDate`) |

### 3. Categories (`/api/categories`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | List all 7 categories with file counts and storage used |
| `GET` | `/api/categories/{category}` | List files under a specific category |

### 4. Duplicate Detection (`/api/duplicates`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/duplicates` | Get all clusters of duplicate files & wasted storage space |
| `GET` | `/api/duplicates/{hash}` | Get all files sharing a specific SHA-256 hash |
| `POST` | `/api/duplicates/check` | Check if a file's hash already exists in storage |
| `POST` | `/api/duplicates/action` | Execute duplicate action: `KEEP_EXISTING`, `REPLACE`, `KEEP_BOTH`, `CANCEL` |

### 5. Storage Analyzer & Dashboard (`/api/storage`, `/api/dashboard`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/storage` | Storage overview: `totalStorage`, `usedStorage`, `freeStorage`, `temporaryStorage`, `duplicateStorage`, `binStorage` |
| `GET` | `/api/dashboard/temporary` | Metrics: `totalTemporaryFiles`, `totalTemporaryStorage`, `expiredCount`, `expiringTodayCount`, `expiringSoonCount` |

### 6. Recycle Bin (`/api/bin`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/bin` | List all items in the recycle bin |
| `POST` | `/api/bin/{id}/restore` | **Restore file** (Preserves original status and expiry date) |
| `DELETE` | `/api/bin/{id}` | Permanently delete file from bin and database |
| `DELETE` | `/api/bin/empty` | Empty the entire recycle bin permanently |

### 7. Notifications (`/api/notifications`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/notifications` | Get system notifications (Expiring soon, Expired, Storage warnings) |
| `PUT` | `/api/notifications/{id}/read` | Mark notification as read |

### 8. Settings (`/api/settings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/settings` | Get current application settings |
| `PUT` | `/api/settings` | Update settings (default expiry days, warning days, retention period) |

---

## ⏰ Temporary File Lifecycle & Background Scheduler

The `FileLifecycleScheduler` runs periodically (`@Scheduled(cron = "0 0 * * * *")`):
1. **Critical Constraint**: Expired files are **NEVER** automatically deleted without explicit user permission.
2. Identifies files expiring within `expiryWarningDays` and generates `EXPIRING_SOON` notifications.
3. Identifies files whose `expiryDate < today` and marks/creates `EXPIRED` notifications.
4. Monitors storage consumption against `warningThresholdPercent` and creates `STORAGE_WARNING` notifications.

---

## 📱 Connecting to the Android Frontend

To connect the Android client to this running backend:

1. **Android Emulator**:
   The default `BASE_URL` in `ApiConfig.kt` (`http://10.0.2.2:8080`) connects directly to your local Spring Boot server.
2. **Physical Device**:
   Find your local IP address (e.g. `192.168.1.100`) using `ipconfig`, then in `SmartStorageBin/app/.../config/ApiConfig.kt` set:
   ```kotlin
   const val BASE_URL = "http://192.168.1.100:8080"
   ```
3. **Switch Repositories to API Mode**:
   In `FileRepository.kt`, `StorageRepository.kt`, and `BinRepository.kt`, change:
   ```kotlin
   private val useSampleData: Boolean = false
   ```
