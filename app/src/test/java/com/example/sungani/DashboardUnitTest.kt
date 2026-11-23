package com.example.sungani

import com.example.sungani.data.SaleRecord
import com.example.sungani.data.ShopDetails
import com.example.sungani.data.UiItem
import com.example.sungani.data.getTodayDate
import org.junit.Assert.*
import org.junit.Test

class DashboardUnitTest {

    @Test
    fun test_DASH_HOME_001_HomeDashboardDisplaysCreativeStats() {
        val items = listOf(
            UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
            UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3)
        )
        val sales = listOf(
            SaleRecord("Sobo Orange", 200, getTodayDate()),
            SaleRecord("Fanta", 200, getTodayDate())
        )
        val shop = ShopDetails(shopName = "SUNGANI", owner = "Praise")

        val todaySales = sales.filter { it.date == getTodayDate() }
        val todayProfit = todaySales.sumOf { it.profit }
        val stockValue = items.sumOf { it.buy * it.qty }
        val potentialSales = items.sumOf { it.sell * it.qty }

        assertEquals(2, items.size)
        assertEquals(400, todayProfit)
        assertEquals(4800, stockValue)
        assertEquals(7800, potentialSales)
        assertEquals("Praise", shop.owner)
    }

    @Test
    fun test_DASH_HOME_002_LowStockAlertDetection() {
        val items = listOf(
            UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
            UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3),
            UiItem(name = "Mandasi", category = "Food", buy = 50, sell = 100, qty = 0)
        )

        val lowStock = items.filter { it.qty <= 5 }
        assertEquals(2, lowStock.size)
        assertTrue(lowStock.any { it.name == "Fanta" })
        assertTrue(lowStock.any { it.name == "Mandasi" })
    }
}
