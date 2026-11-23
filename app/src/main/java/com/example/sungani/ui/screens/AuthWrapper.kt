package com.example.sungani.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.sungani.data.*
import com.example.sungani.ui.navigation.AppNavigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun AuthWrapper() {
    val ctx = LocalContext.current
    var loaded by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf(listOf<UiItem>()) }
    var sales by remember { mutableStateOf(listOf<SaleRecord>()) }
    var shop by remember { mutableStateOf(ShopDetails()) }
    var history by remember { mutableStateOf(listOf<DailyProfit>()) }
    var lastDate by remember { mutableStateOf(getTodayDate()) }
    var yesterdayProfit by remember { mutableIntStateOf(0) }
    var loginData by remember { mutableStateOf(LoginData()) }
    var isLoggedIn by remember { mutableStateOf(false) }

    fun checkAndResetDayIfNeeded(currentSales: List<SaleRecord>): List<SaleRecord> {
        val today = getTodayDate()
        if (lastDate != today) {
            val profitForYesterday = currentSales.filter { it.date == lastDate }.sumOf { it.profit }
            val newHistory = history + DailyProfit(lastDate, profitForYesterday)
            history = if (newHistory.size > 7) newHistory.takeLast(7) else newHistory
            yesterdayProfit = profitForYesterday
            lastDate = today
            val freshSales = emptyList<SaleRecord>()
            saveAll(ctx, items, freshSales, shop, history, today, profitForYesterday, loginData, isLoggedIn)
            return freshSales
        }
        return currentSales
    }

    LaunchedEffect(Unit) {
        val prefs = ctx.getSharedPreferences("sungani_final", Context.MODE_PRIVATE)
        val iJson = prefs.getString("items", null)
        val sJson = prefs.getString("sales", null)
        val shopJson = prefs.getString("shop", null)
        val hJson = prefs.getString("history", null)
        val loginJson = prefs.getString("login", null)

        items = if (iJson != null) {
            try { Gson().fromJson(iJson, object : TypeToken<List<UiItem>>() {}.type) }
            catch (e: Exception) { emptyList() }
        } else {
            listOf(
                UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
                UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3),
                UiItem(name = "Mandasi", category = "Food", buy = 50, sell = 100, qty = 20)
            )
        }

        val loadedSales: List<SaleRecord> = if (sJson != null) {
            try { Gson().fromJson(sJson, object : TypeToken<List<SaleRecord>>() {}.type) }
            catch (e: Exception) { emptyList() }
        } else emptyList()

        shop = if (shopJson != null) {
            try { Gson().fromJson(shopJson, ShopDetails::class.java) }
            catch (e: Exception) { ShopDetails() }
        } else ShopDetails()

        history = if (hJson != null) {
            try { Gson().fromJson(hJson, object : TypeToken<List<DailyProfit>>() {}.type) }
            catch (e: Exception) { emptyList() }
        } else emptyList()

        loginData = if (loginJson != null) {
            try { Gson().fromJson(loginJson, LoginData::class.java) }
            catch (e: Exception) { LoginData() }
        } else LoginData()

        lastDate = prefs.getString("lastDate", getTodayDate()) ?: getTodayDate()
        yesterdayProfit = prefs.getInt("yesterdayProfit", 0)
        isLoggedIn = prefs.getBoolean("loggedIn", false)

        sales = checkAndResetDayIfNeeded(loadedSales)
        loaded = true
    }

    LaunchedEffect(items, sales, shop, history, lastDate, yesterdayProfit, loginData, isLoggedIn) {
        if (loaded) {
            saveAll(ctx, items, sales, shop, history, lastDate, yesterdayProfit, loginData, isLoggedIn)
        }
    }

    if (!loaded) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (!isLoggedIn || !loginData.isRegistered) {
        LoginScreen(
            loginData = loginData,
            onLoginSuccess = { isLoggedIn = true },
            onRegister = { newLogin ->
                loginData = newLogin
                isLoggedIn = true
            }
        )
    } else {
        AppNavigation(
            items = items,
            sales = sales,
            shop = shop,
            history = history,
            yesterdayProfit = yesterdayProfit,
            onItemsChange = { items = it },
            onSalesChange = { updatedSales ->
                val activeSales = checkAndResetDayIfNeeded(sales)
                sales = activeSales + updatedSales.takeLast(1)
            },
            onShopChange = { shop = it },
            onLogout = { isLoggedIn = false }
        )
    }
}
