package com.example.sungani.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.ShopDetails

@Composable
fun ShopScreen(
    shop: ShopDetails,
    brown: Color,
    orange: Color,
    onSave: (ShopDetails) -> Unit,
    onLogout: () -> Unit
) {
    val ctx = LocalContext.current
    var shopName by remember { mutableStateOf(shop.shopName) }
    var owner by remember { mutableStateOf(shop.owner) }
    var ownerError by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf<String?>(null) }
    val scroll = rememberScrollState()

    LaunchedEffect(shop) {
        shopName = shop.shopName
        owner = shop.owner
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("My Shop Management", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = brown)

        if (savedMessage != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                    Text(
                        savedMessage!!,
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = shopName,
                    onValueChange = {
                        shopName = it
                        savedMessage = null
                    },
                    label = { Text("Shop Name") },
                    leadingIcon = { Icon(Icons.Filled.Store, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Column {
                    OutlinedTextField(
                        value = owner,
                        onValueChange = { input ->
                            owner = input
                            savedMessage = null
                            ownerError = if (input.any { it.isDigit() }) "Owner name should not contain numbers" else ""
                        },
                        label = { Text("Owner Name") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        isError = ownerError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (ownerError.isNotEmpty()) {
                        Text(ownerError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                    }
                }

                Button(
                    onClick = {
                        if (shopName.isBlank() || owner.isBlank()) {
                            Toast.makeText(ctx, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (owner.any { it.isDigit() }) {
                            Toast.makeText(ctx, "Owner name should not contain numbers", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        onSave(ShopDetails(shopName, owner))
                        savedMessage = "Saved! Top bar is now $shopName"
                        Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(orange),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = brown),
                    border = BorderStroke(1.5.dp, brown),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = brown)
                    Spacer(Modifier.width(8.dp))
                    Text("Logout", fontWeight = FontWeight.Bold, color = brown, fontSize = 15.sp)
                }
            }
        }
    }
}
