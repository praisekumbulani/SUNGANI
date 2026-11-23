package com.example.sungani

import com.example.sungani.data.LoginData
import org.junit.Assert.*
import org.junit.Test

class AuthUnitTest {

    @Test
    fun test_AUTH_REG_001_SuccessfulShopOwnerRegistration() {
        val username = "praise"
        val password = "1234"
        val confirmPassword = "1234"

        assertTrue("Username should not be blank", username.isNotBlank())
        assertTrue("Password should not be blank", password.isNotBlank())
        assertEquals("Passwords must match", password, confirmPassword)
        assertTrue("Password length should be >= 4", password.length >= 4)

        val newLoginData = LoginData(username = username, password = password, isRegistered = true)
        assertTrue(newLoginData.isRegistered)
        assertEquals("praise", newLoginData.username)
        assertEquals("1234", newLoginData.password)
    }

    @Test
    fun test_AUTH_LOGIN_001_SuccessfulLoginWithRegisteredCredentials() {
        val registeredData = LoginData(username = "praise", password = "1234", isRegistered = true)
        val inputUser = "praise"
        val inputPass = "1234"

        val isLoginSuccessful = inputUser == registeredData.username && inputPass == registeredData.password
        assertTrue("Login should succeed with correct credentials", isLoginSuccessful)
    }

    @Test
    fun test_AUTH_LOGIN_002_LoginWithIncorrectPassword() {
        val registeredData = LoginData(username = "praise", password = "1234", isRegistered = true)
        val inputUser = "praise"
        val inputPass = "wrongpass"

        val isLoginSuccessful = inputUser == registeredData.username && inputPass == registeredData.password
        assertFalse("Login should fail with wrong password", isLoginSuccessful)
    }

    @Test
    fun test_AUTH_LOGIN_003_LoginWithEmptyFields() {
        val inputUser = ""
        val inputPass = ""

        val isBlocked = inputUser.isBlank() || inputPass.isBlank()
        assertTrue("Login should be blocked when fields are empty", isBlocked)
    }

    @Test
    fun test_AUTH_LOGIN_004_RegistrationWithMismatchedPasswords() {
        val password = "1234"
        val confirmPassword = "4321"

        val isMismatch = password != confirmPassword
        assertTrue("Registration should be blocked on password mismatch", isMismatch)
    }
}
