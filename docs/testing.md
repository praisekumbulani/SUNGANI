# SUNGANI Testing Strategy & Test Suite Documentation

## Overview
SUNGANI is equipped with a unit test suite covering all 31 test cases defined in `Praise Kumbulani - Test Case Template.docx`.

## Installed Testing Dependencies
- **JUnit 4** (`junit:junit:4.13.2`): Unit test framework.
- **Robolectric** (`org.robolectric:robolectric:4.12.2`): Local Android environment simulation.
- **MockK** (`io.mockk:mockk:1.13.10`): Mocking library for Kotlin.
- **Compose UI Test** (`androidx.compose.ui:ui-test-junit4`): Jetpack Compose testing framework.

## Test Suites & Test Cases Mapping

### 1. Authentication Module (`AuthUnitTest.kt`)
- `AUTH_REG_001`: Successful Shop Owner Registration.
- `AUTH_LOGIN_001`: Successful Login with Registered Credentials.
- `AUTH_LOGIN_002`: Login with Incorrect Password.
- `AUTH_LOGIN_003`: Login with Empty Fields.
- `AUTH_LOGIN_004`: Registration with Mismatched Passwords.

### 2. Inventory & Stock Management Module (`InventoryUnitTest.kt`)
- `INV_ADD_001`: Add New Stock Item Successfully.
- `INV_ADD_002`: Add Stock with Empty Item Name.
- `INV_ADD_003`: Add Stock with Letters in Buying Price.
- `INV_ADD_004`: Add Stock with Letters in Selling Price.
- `INV_ADD_005`: Add Stock with Letters in Quantity.
- `INV_ADD_006`: Add Stock with Negative Values.
- `INV_ADD_007`: Add Stock Where Selling Price $\le$ Buying Price.
- `INV_STOCK_001`: View Stocks Grouped by Category.
- `INV_SELL_001`: Successful Sale Reduces Quantity and Updates Profit.
- `INV_SELL_002`: Sell Action Disabled When Quantity is Zero.
- `INV_EDIT_001`: Edit Existing Stock Item.
- `INV_EDIT_002`: Edit Item Validation Blocks Non-Numeric Inputs.
- `INV_DEL_001`: Delete Stock Item.

### 3. Home Dashboard Module (`DashboardUnitTest.kt`)
- `DASH_HOME_001`: Home Dashboard Displays Creative Stats.
- `DASH_HOME_002`: Low Stock Alert Detection ($\le 5$ items).

### 4. Reports & Reset Module (`ReportAndResetUnitTest.kt`)
- `REP_001`: Profit Report Shows Today vs Yesterday Comparison.
- `REP_002`: 7-Day Trend Data Scrollable and Scaled.
- `REP_003`: Sales Breakdown Grouped by Item Name.
- `REP_004`: Daily Profit Reset Logic (`lastDate != today`).

### 5. Shop Management Module (`ShopSettingsUnitTest.kt`)
- `SHOP_001`: Save Shop Details Successfully.
- `SHOP_002`: Shop Owner Name Rejects Numbers.
- `SHOP_003`: Shop Save with Empty Fields.
- `SHOP_004`: Logout Functionality.

### 6. System & Performance Module (`DataPersistenceUnitTest.kt`)
- `SYS_001`: Data Serialization & Persistence (Gson).
- `SYS_002`: Form State Validation.
- `SYS_003`: App Performance & Lightweight Models.

## Running the Tests
To execute all local unit tests from the command line or terminal:
```bash
./gradlew testDebugUnitTest
```

To run a specific test suite (e.g. `AuthUnitTest`):
```bash
./gradlew testDebugUnitTest --tests "com.example.sungani.AuthUnitTest"
```
