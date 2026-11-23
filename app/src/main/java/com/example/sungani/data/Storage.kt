package com.example.sungani.data

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTodayDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

fun saveAll(
    ctx: Context,
    items: List<UiItem>,
    sales: List<SaleRecord>,
    shop: ShopDetails,
    history: List<DailyProfit>,
    lastDate: String,
    yesterdayProfit: Int,
    login: LoginData,
    isLoggedIn: Boolean
) {
    val prefs = ctx.getSharedPreferences("sungani_final", Context.MODE_PRIVATE)
    CoroutineScope(Dispatchers.IO).launch {
        prefs.edit()
            .putString("items", Gson().toJson(items))
            .putString("sales", Gson().toJson(sales))
            .putString("shop", Gson().toJson(shop))
            .putString("history", Gson().toJson(history))
            .putString("lastDate", lastDate)
            .putInt("yesterdayProfit", yesterdayProfit)
            .putString("login", Gson().toJson(login))
            .putBoolean("loggedIn", isLoggedIn)
            .apply()

        // Schedule WorkManager network-constrained background sync task
        SyncWorker.scheduleNetworkSync(ctx)
    }
}
