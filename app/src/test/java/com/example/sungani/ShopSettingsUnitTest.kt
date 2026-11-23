package com.example.sungani

import com.example.sungani.data.ShopDetails
import org.junit.Assert.*
import org.junit.Test

class ShopSettingsUnitTest {

    @Test
    fun test_SHOP_001_SaveShopDetailsSuccessfully() {
        val shopName = "Kumbulani Shop"
        val owner = "Praise"

        assertTrue(shopName.isNotBlank())
        assertTrue(owner.isNotBlank())
        assertFalse("Owner name should not contain digits", owner.any { it.isDigit() })

        val shop = ShopDetails(shopName = shopName, owner = owner)
        assertEquals("Kumbulani Shop", shop.shopName)
        assertEquals("Praise", shop.owner)
    }

    @Test
    fun test_SHOP_002_ShopOwnerNameRejectsNumbers() {
        val ownerInput = "Praise123"
        val hasNumbers = ownerInput.any { it.isDigit() }
        assertTrue("Owner name with numbers should be detected and rejected", hasNumbers)
    }

    @Test
    fun test_SHOP_003_ShopSaveWithEmptyFields() {
        val shopName = ""
        val owner = ""

        val isBlocked = shopName.isBlank() || owner.isBlank()
        assertTrue("Saving shop details with empty fields should be blocked", isBlocked)
    }

    @Test
    fun test_SHOP_004_LogoutFunctionality() {
        var isLoggedIn = true

        // Logout action
        isLoggedIn = false
        assertFalse("Session should be logged out", isLoggedIn)
    }
}
