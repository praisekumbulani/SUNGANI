package com.example.sungani

import com.example.sungani.data.DailyProfit
import com.example.sungani.data.SaleRecord
import com.example.sungani.data.getTodayDate
import org.junit.Assert.*
import org.junit.Test

class ReportAndResetUnitTest {

    @Test
    fun test_REP_001_ProfitReportShowsTodayVsYesterday() {
        val yesterdayProfit = 500
        val sales = listOf(
            SaleRecord("Sobo Orange", 200, getTodayDate()),
            SaleRecord("Fanta", 200, getTodayDate())
        )

        val todayProfit = sales.filter { it.date == getTodayDate() }.sumOf { it.profit }
        assertEquals(400, todayProfit)
        assertEquals(500, yesterdayProfit)
        assertFalse("Today's profit is less than yesterday", todayProfit >= yesterdayProfit)
    }

    @Test
    fun test_REP_002_7DayTrendDataScrollableAndScaled() {
        val history = mutableListOf(
            DailyProfit("2026-03-25", 100),
            DailyProfit("2026-03-26", 300),
            DailyProfit("2026-03-27", 500),
            DailyProfit("2026-03-28", 200),
            DailyProfit("2026-03-29", 400),
            DailyProfit("2026-03-30", 600)
        )
        val todayProfit = 350
        val chartData = history + DailyProfit(getTodayDate(), todayProfit)

        val maxProfit = (chartData.maxOfOrNull { it.profit } ?: 1).coerceAtLeast(1)
        assertEquals(600, maxProfit)
        assertEquals(7, chartData.size)

        // Verify min height scaling
        chartData.forEach { record ->
            val barHeightRatio = (record.profit.toFloat() / maxProfit)
            assertTrue("Ratio should be between 0 and 1", barHeightRatio in 0.0f..1.0f)
        }
    }

    @Test
    fun test_REP_003_SalesBreakdownGroupedByItemName() {
        val sales = listOf(
            SaleRecord("Sobo Orange", 200, getTodayDate()),
            SaleRecord("Sobo Orange", 200, getTodayDate()),
            SaleRecord("Fanta", 200, getTodayDate())
        )

        val breakdown = sales.groupBy { it.name }.map { (name, list) ->
            Triple(name, list.size, list.sumOf { it.profit })
        }

        assertEquals(2, breakdown.size)
        val soboEntry = breakdown.find { it.first == "Sobo Orange" }
        assertNotNull(soboEntry)
        assertEquals(2, soboEntry?.second)
        assertEquals(400, soboEntry?.third)
    }

    @Test
    fun test_REP_004_DailyProfitResetLogic() {
        var lastDate = "2026-03-30"
        val today = "2026-03-31"
        var yesterdayProfit = 0
        var sales = listOf(
            SaleRecord("Sobo Orange", 200, "2026-03-30"),
            SaleRecord("Fanta", 300, "2026-03-30")
        )
        var history = listOf<DailyProfit>()

        // Simulate date change detection
        if (lastDate != today) {
            val profitForYesterday = sales.filter { it.date == lastDate }.sumOf { it.profit }
            val newHistory = history + DailyProfit(lastDate, profitForYesterday)
            history = if (newHistory.size > 7) newHistory.takeLast(7) else newHistory
            yesterdayProfit = profitForYesterday
            lastDate = today
            sales = emptyList()
        }

        assertEquals("2026-03-31", lastDate)
        assertEquals(500, yesterdayProfit)
        assertEquals(0, sales.size)
        assertEquals(1, history.size)
        assertEquals("2026-03-30", history[0].date)
        assertEquals(500, history[0].profit)
    }
}
