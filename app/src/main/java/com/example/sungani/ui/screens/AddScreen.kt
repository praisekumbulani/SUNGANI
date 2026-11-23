package com.example.sungani.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.UiItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    buttonColor: Color,
    onAdd: (UiItem) -> Unit
) {
    val ctx = LocalContext.current

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Drinks") }
    var buy by remember { mutableStateOf("") }
    var sell by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    var buyError by remember { mutableStateOf("") }
    var sellError by remember { mutableStateOf("") }
    var qtyError by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add New Stock Item", fontWeight = FontWeight.Bold, color = buttonColor, fontSize = 20.sp)

        Card(
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Drinks", "Food", "Groceries", "Electronics", "Others").forEach { itemCat ->
                            DropdownMenuItem(
                                text = { Text(itemCat) },
                                onClick = {
                                    category = itemCat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Column {
                    OutlinedTextField(
                        value = buy,
                        onValueChange = { input ->
                            buy = input
                            buyError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Buying Price (K)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = buyError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (buyError.isNotEmpty()) {
                        Text(buyError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                    }
                }

                Column {
                    OutlinedTextField(
                        value = sell,
                        onValueChange = { input ->
                            sell = input
                            sellError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Selling Price (K)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = sellError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (sellError.isNotEmpty()) {
                        Text(sellError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                    }
                }

                Column {
                    OutlinedTextField(
                        value = qty,
                        onValueChange = { input ->
                            qty = input
                            qtyError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = qtyError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (qtyError.isNotEmpty()) {
                        Text(qtyError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                    }
                }

                Button(
                    onClick = {
                        if (name.isBlank()) {
                            Toast.makeText(ctx, "Enter item name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val buyVal = buy.toIntOrNull()
                        if (buyVal == null) {
                            Toast.makeText(ctx, "Enter valid buying price - numbers only", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val sellVal = sell.toIntOrNull()
                        if (sellVal == null) {
                            Toast.makeText(ctx, "Enter valid selling price - numbers only", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val qtyVal = qty.toIntOrNull()
                        if (qtyVal == null) {
                            Toast.makeText(ctx, "Enter valid quantity - numbers only", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (buyVal <= 0 || sellVal <= 0 || qtyVal < 0) {
                            Toast.makeText(ctx, "Values must be positive", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (sellVal <= buyVal) {
                            Toast.makeText(ctx, "Selling price must be higher than buying", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        onAdd(
                            UiItem(
                                name = name.trim(),
                                category = category,
                                buy = buyVal,
                                sell = sellVal,
                                qty = qtyVal
                            )
                        )
                        Toast.makeText(ctx, "Added $name", Toast.LENGTH_SHORT).show()
                        name = ""
                        buy = ""
                        sell = ""
                        qty = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Item", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(Modifier.height(120.dp))
            }
        }
    }
}
