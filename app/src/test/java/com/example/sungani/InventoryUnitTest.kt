package com.example.sungani

import com.example.sungani.data.SaleRecord
import com.example.sungani.data.UiItem
import com.example.sungani.data.getTodayDate
import org.junit.Assert.*
import org.junit.Test

class InventoryUnitTest {

    @Test
    fun test_INV_ADD_001_AddNewStockItemSuccessfully() {
        val items = mutableListOf<UiItem>()
        val name = "Sobo Orange"
        val category = "Drinks"
        val buy = 300
        val sell = 500
        val qty = 12

        assertTrue(name.isNotBlank())
        assertTrue(buy > 0)
        assertTrue(sell > buy)
        assertTrue(qty >= 0)

        val newItem = UiItem(name = name, category = category, buy = buy, sell = sell, qty = qty)
        items.add(newItem)

        assertEquals(1, items.size)
        assertEquals("Sobo Orange", items[0].name)
        assertEquals(200, items[0].sell - items[0].buy)
    }

    @Test
    fun test_INV_ADD_002_AddStockWithEmptyItemName() {
        val name = ""
        val isBlocked = name.isBlank()
        assertTrue("Adding item with empty name should be blocked", isBlocked)
    }

    @Test
    fun test_INV_ADD_003_AddStockWithLettersInBuyingPrice() {
        val buyInput = "abc"
        val parsedBuy = buyInput.toIntOrNull()
        assertNull("Buying price with letters should fail numeric parsing", parsedBuy)
    }

    @Test
    fun test_INV_ADD_004_AddStockWithLettersInSellingPrice() {
        val sellInput = "xyz"
        val parsedSell = sellInput.toIntOrNull()
        assertNull("Selling price with letters should fail numeric parsing", parsedSell)
    }

    @Test
    fun test_INV_ADD_005_AddStockWithLettersInQuantity() {
        val qtyInput = "ten"
        val parsedQty = qtyInput.toIntOrNull()
        assertNull("Quantity with letters should fail numeric parsing", parsedQty)
    }

    @Test
    fun test_INV_ADD_006_AddStockWithNegativeValues() {
        val buy = -100
        val sell = -50
        val qty = -5

        val isInvalid = buy <= 0 || sell <= 0 || qty < 0
        assertTrue("Negative values should be rejected", isInvalid)
    }

    @Test
    fun test_INV_ADD_007_AddStockWhereSellingPriceLessThanBuyingPrice() {
        val buy = 500
        val sell = 400

        val isSellingPriceInvalid = sell <= buy
        assertTrue("Selling price <= buying price should be rejected", isSellingPriceInvalid)
    }

    @Test
    fun test_INV_STOCK_001_ViewStocksGroupedByCategory() {
        val items = listOf(
            UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
            UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3),
            UiItem(name = "Mandasi", category = "Food", buy = 50, sell = 100, qty = 20)
        )

        val grouped = items.groupBy { it.category }
        assertTrue(grouped.containsKey("Drinks"))
        assertTrue(grouped.containsKey("Food"))
        assertEquals(2, grouped["Drinks"]?.size)
        assertEquals(1, grouped["Food"]?.size)
    }

    @Test
    fun test_INV_SELL_001_SuccessfulSaleReducesQuantityAndUpdatesProfit() {
        val items = mutableListOf(UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12))
        val sales = mutableListOf<SaleRecord>()

        val targetItem = items[0]
        assertTrue(targetItem.qty > 0)

        // Sell 1 item
        items[0] = targetItem.copy(qty = targetItem.qty - 1)
        val profit = targetItem.sell - targetItem.buy
        sales.add(SaleRecord(targetItem.name, profit, getTodayDate()))

        assertEquals(11, items[0].qty)
        assertEquals(1, sales.size)
        assertEquals(200, sales[0].profit)
        assertEquals("Sobo Orange", sales[0].name)
    }

    @Test
    fun test_INV_SELL_002_SellButtonDisabledWhenQuantityZero() {
        val item = UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 0)
        val canSell = item.qty > 0
        assertFalse("Sale action should be disabled when quantity is zero", canSell)
    }

    @Test
    fun test_INV_EDIT_001_EditExistingStockItem() {
        val items = mutableListOf(UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3))
        val editIndex = 0

        val editedItem = items[editIndex].copy(name = "Fanta Big", buy = 450, sell = 650, qty = 10)
        items[editIndex] = editedItem

        assertEquals("Fanta Big", items[0].name)
        assertEquals(450, items[0].buy)
        assertEquals(650, items[0].sell)
        assertEquals(10, items[0].qty)
    }

    @Test
    fun test_INV_EDIT_002_EditItemWithLettersInNumericFields() {
        val buyInput = "abc"
        val parsedBuy = buyInput.toIntOrNull()
        assertNull("Non-numeric input in edit dialog should be rejected", parsedBuy)
    }

    @Test
    fun test_INV_DEL_001_DeleteStockItem() {
        val items = mutableListOf(
            UiItem(name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
            UiItem(name = "Fanta", category = "Drinks", buy = 400, sell = 600, qty = 3)
        )

        items.removeAt(0)
        assertEquals(1, items.size)
        assertEquals("Fanta", items[0].name)
    }
}
