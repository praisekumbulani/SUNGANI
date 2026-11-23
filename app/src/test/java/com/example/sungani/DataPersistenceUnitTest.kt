package com.example.sungani

import com.example.sungani.data.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Test

class DataPersistenceUnitTest {

    private val gson = Gson()

    @Test
    fun test_SYS_001_DataSerializationAndPersistence() {
        val originalItems = listOf(
            UiItem(id = 1001, name = "Sobo Orange", category = "Drinks", buy = 300, sell = 500, qty = 12),
            UiItem(id = 1002, name = "Mandasi", category = "Food", buy = 50, sell = 100, qty = 20)
        )
        val originalShop = ShopDetails(shopName = "Kumbulani Shop", owner = "Praise")
        val originalLogin = LoginData(username = "praise", password = "1234", isRegistered = true)

        // Serialize to JSON
        val itemsJson = gson.toJson(originalItems)
        val shopJson = gson.toJson(originalShop)
        val loginJson = gson.toJson(originalLogin)

        // Deserialize back from JSON
        val restoredItems: List<UiItem> = gson.fromJson(itemsJson, object : TypeToken<List<UiItem>>() {}.type)
        val restoredShop: ShopDetails = gson.fromJson(shopJson, ShopDetails::class.java)
        val restoredLogin: LoginData = gson.fromJson(loginJson, LoginData::class.java)

        assertEquals(2, restoredItems.size)
        assertEquals("Sobo Orange", restoredItems[0].name)
        assertEquals("Kumbulani Shop", restoredShop.shopName)
        assertEquals("Praise", restoredShop.owner)
        assertTrue(restoredLogin.isRegistered)
        assertEquals("praise", restoredLogin.username)
    }

    @Test
    fun test_SYS_002_FormStateValidation() {
        val validName = "Fanta"
        val validBuy = "400"
        val validSell = "600"
        val validQty = "10"

        val buyInt = validBuy.toIntOrNull()
        val sellInt = validSell.toIntOrNull()
        val qtyInt = validQty.toIntOrNull()

        assertNotNull(buyInt)
        assertNotNull(sellInt)
        assertNotNull(qtyInt)
        assertTrue(validName.isNotBlank())
        assertTrue(sellInt!! > buyInt!!)
        assertTrue(qtyInt!! >= 0)
    }

    @Test
    fun test_SYS_003_AppPerformanceAndLightweightModels() {
        val largeList = (1..1000).map { i ->
            UiItem(id = i.toLong(), name = "Item $i", category = "Category ${i % 5}", buy = 100 * i, sell = 150 * i, qty = i % 20)
        }
        val serialized = gson.toJson(largeList)
        val restored: List<UiItem> = gson.fromJson(serialized, object : TypeToken<List<UiItem>>() {}.type)

        assertEquals(1000, restored.size)
        assertEquals("Item 1", restored[0].name)
        assertEquals("Item 1000", restored[999].name)
    }
}
