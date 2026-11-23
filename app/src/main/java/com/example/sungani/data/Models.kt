package com.example.sungani.data

data class UiItem(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val category: String,
    val buy: Int,
    val sell: Int,
    var qty: Int
)

data class SaleRecord(
    val name: String,
    val profit: Int,
    val date: String = getTodayDate()
)

data class ShopDetails(
    val shopName: String = "SUNGANI",
    val owner: String = ""
)

data class DailyProfit(
    val date: String,
    val profit: Int
)

data class LoginData(
    val username: String = "",
    val password: String = "",
    val isRegistered: Boolean = false
)
