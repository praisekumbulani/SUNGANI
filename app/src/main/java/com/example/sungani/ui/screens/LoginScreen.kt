package com.example.sungani.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.LoginData

@Composable
fun LoginScreen(
    loginData: LoginData,
    onLoginSuccess: () -> Unit,
    onRegister: (LoginData) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(!loginData.isRegistered) }
    var error by remember { mutableStateOf("") }

    val Brown = Color(0xFF3E2723)
    val Amber = Color(0xFFFF8F00)
    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Store,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Brown
                )
                Text(
                    text = if (isRegisterMode) "Create Shop Account" else "SUNGANI Login",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Brown
                )
                Text(
                    text = if (isRegisterMode) "Register your shop to start" else "Login to continue",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; error = "" },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = "" },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                if (isRegisterMode) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; error = "" },
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                if (error.isNotEmpty()) {
                    Text(error, color = Color.Red, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            error = "Fill all fields"
                            return@Button
                        }
                        if (isRegisterMode) {
                            if (password != confirmPassword) {
                                error = "Passwords don't match"
                                return@Button
                            }
                            if (password.length < 4) {
                                error = "Password too short"
                                return@Button
                            }
                            onRegister(LoginData(username, password, true))
                        } else {
                            if (username == loginData.username && password == loginData.password) {
                                onLoginSuccess()
                            } else {
                                error = "Wrong username or password"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Brown),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isRegisterMode) "Register" else "Login", color = Color.White, fontSize = 16.sp)
                }

                TextButton(onClick = { isRegisterMode = !isRegisterMode; error = "" }) {
                    Text(
                        text = if (isRegisterMode) "Already have account? Login" else "No account? Register",
                        color = Amber,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
