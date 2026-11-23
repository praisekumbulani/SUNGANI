# SUNGANI — Small Business Stock & Sales Manager

**Student Name**: Praise Kumbulani  
**Reg No / Student ID**: BED-COM-15-21  
**Target Platform**: Android 11+ (Min SDK 24, Target SDK 37)  
**App Name**: SUNGANI (*"Store / Save"* in Chichewa)

---

## 📱 Project Overview

**SUNGANI** is an offline-first Android application designed for small business owners and micro-enterprises in Malawi (such as tuckshops, kaunjika sellers, groceries, and market vendors). Most micro-businesses rely on physical notebooks to log sales, leading to lost records, manual calculation errors, and an inability to track net profit. 

SUNGANI provides a clean, modern, and lightweight solution that works 100% offline, automatically calculating profit (`Selling Price - Buying Price`), sending stock alerts, and archiving 7-day profit history.

---

## ✨ Key Features

- **Offline-First Storage**: All core operations (inventory management, sales execution, profit calculations, daily resets) work 100% offline without requiring active internet connectivity.
- **Home Dashboard Analytics**: 
  - **Today's Profit** hero banner with trend indicators comparing performance against yesterday.
  - **Financial Metrics Grid**: Total registered items, low stock count, stock value, and potential sales value.
  - **Best Seller & Category Summary**.
  - **Restock Alert Card**: Highlights items running low ($\le 5$ units).
- **Inventory Management**:
  - Add, edit, and delete stock items.
  - Interactive search bar filtering stock items by name.
  - Items organized by category headers (`DRINKS`, `FOOD`, `GROCERIES`, etc.).
  - Input validation enforcing positive numbers for prices and quantities, and verifying that selling price is higher than buying price.
- **One-Tap Selling**:
  - One-tap sales execution that automatically decrements stock quantity and records profit.
  - Sell button automatically disables when stock quantity reaches 0.
- **Reports & Analytics**:
  - Today vs. Yesterday profit comparisons.
  - Scrollable 7-day profit trend bar chart.
  - Today's itemized sales breakdown.
- **Automated Daily Reset**:
  - Detects date transitions automatically, archives yesterday's total profit into 7-day history, and starts today's sales counter fresh at `K0`.
- **WorkManager Background Sync**:
  - Leverages Android Jetpack `WorkManager` with `NetworkType.CONNECTED` constraints to queue and execute background data synchronization when internet connectivity is detected.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Asynchronous Execution**: Kotlin Coroutines (`Dispatchers.IO`) & `LaunchedEffect`
- **Background Tasks**: Jetpack `WorkManager` (`CoroutineWorker`) with `NetworkType.CONNECTED` constraints
- **Persistence**: `SharedPreferences` + `Gson` serialization
- **Testing Frameworks**: JUnit 4, Robolectric, MockK, Compose UI Test

---

## 🧪 Unit Test Suite & Verification

The project includes 32 passing unit tests mapping 1:1 with all 31 test cases specified in the project test plan template:

| Test Module | Coverage | Status |
| :--- | :--- | :---: |
| **Authentication** (`AuthUnitTest`) | Registration, valid/invalid login, empty fields, password matching (`AUTH_REG_001` to `AUTH_LOGIN_004`) | **PASSED** |
| **Inventory** (`InventoryUnitTest`) | Add item, input validation (numeric, positive, sell > buy), category grouping, sell execution, edit/delete (`INV_ADD_001` to `INV_DEL_001`) | **PASSED** |
| **Dashboard** (`DashboardUnitTest`) | Creative metrics, stock value, potential sales, low stock detection (`DASH_HOME_001` to `DASH_HOME_002`) | **PASSED** |
| **Reports** (`ReportAndResetUnitTest`) | Today vs yesterday comparison, 7-day trend chart scaling, sales breakdown, daily profit reset (`REP_001` to `REP_004`) | **PASSED** |
| **Shop Settings** (`ShopSettingsUnitTest`) | Save details, owner name number validation, logout action (`SHOP_001` to `SHOP_004`) | **PASSED** |
| **System & Persistence** (`DataPersistenceUnitTest`) | Gson JSON serialization/deserialization, form state validation, 1000-item model performance (`SYS_001` to `SYS_003`) | **PASSED** |

### Running Unit Tests:
```bash
./gradlew testDebugUnitTest
```

---

## 🚀 Building & Running the Project

### Prerequisites:
- Android Studio Ladybug or newer
- JDK 17 / Kotlin 2.2+
- Android SDK (API Level 24+)

### Build Steps:
1. Clone this repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in Android Studio.
3. Sync Gradle and build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
4. Output APK location:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 📌 Note on Project Submission & Commit History

> [!NOTE]
> **Submission Notice**: All application development, testing, and deployment configurations were completed prior to the assessment submission deadline on **Monday, 23 November 2026, 2:00 PM**. This public repository contains the complete, verified codebase and unit test suite as presented for evaluation.
