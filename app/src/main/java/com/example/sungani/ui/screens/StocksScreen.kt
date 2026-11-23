package com.example.sungani.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.UiItem

@Composable
fun StocksScreen(
    items: List<UiItem>,
    brown: Color,
    onSell: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int, UiItem) -> Unit
) {
    val ctx = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var editItem by remember { mutableStateOf<UiItem?>(null) }
    var editIndex by remember { mutableStateOf(-1) }

    val filteredItems = items.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search stock items...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = brown) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = null)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty()) "No stock items added yet." else "No items match '$searchQuery'",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                filteredItems.groupBy { it.category }.forEach { (cat, list) ->
                    item {
                        Text(
                            text = cat.uppercase(),
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 4.dp)
                        )
                    }
                    items(list.size) { i ->
                        val item = list[i]
                        val originalIndex = items.indexOf(item)
                        Card(
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 6.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(item.name, fontWeight = FontWeight.Bold, color = brown, fontSize = 16.sp)
                                        Text(
                                            "Buy: K${item.buy} | Sell: K${item.sell} | Profit: K${item.sell - item.buy}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            "Qty: ${item.qty} ${if (item.qty == 0) "(OUT OF STOCK)" else if (item.qty <= 5) "(LOW)" else ""}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.qty == 0) Color.Red else if (item.qty <= 5) Color(0xFFFF8F00) else Color(0xFF2E7D32)
                                        )
                                    }
                                    Button(
                                        onClick = { onSell(originalIndex) },
                                        enabled = item.qty > 0,
                                        colors = ButtonDefaults.buttonColors(brown),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(38.dp)
                                    ) {
                                        Text("SELL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { editItem = item; editIndex = originalIndex },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(32.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Edit", fontSize = 12.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onDelete(originalIndex) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(32.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Delete", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editItem != null) {
        var ename by remember { mutableStateOf(editItem!!.name) }
        var ebuy by remember { mutableStateOf(editItem!!.buy.toString()) }
        var esell by remember { mutableStateOf(editItem!!.sell.toString()) }
        var eqty by remember { mutableStateOf(editItem!!.qty.toString()) }

        var buyError by remember { mutableStateOf("") }
        var sellError by remember { mutableStateOf("") }
        var qtyError by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { editItem = null },
            title = { Text("Edit Stock Item", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ename,
                        onValueChange = { ename = it },
                        label = { Text("Item Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ebuy,
                        onValueChange = { input ->
                            ebuy = input
                            buyError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Buying Price (K)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = buyError.isNotEmpty(),
                        singleLine = true
                    )
                    if (buyError.isNotEmpty()) Text(buyError, color = Color.Red, fontSize = 11.sp)

                    OutlinedTextField(
                        value = esell,
                        onValueChange = { input ->
                            esell = input
                            sellError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Selling Price (K)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = sellError.isNotEmpty(),
                        singleLine = true
                    )
                    if (sellError.isNotEmpty()) Text(sellError, color = Color.Red, fontSize = 11.sp)

                    OutlinedTextField(
                        value = eqty,
                        onValueChange = { input ->
                            eqty = input
                            qtyError = if (input.isNotBlank() && input.toIntOrNull() == null) "Numbers only" else ""
                        },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = qtyError.isNotEmpty(),
                        singleLine = true
                    )
                    if (qtyError.isNotEmpty()) Text(qtyError, color = Color.Red, fontSize = 11.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val buyVal = ebuy.toIntOrNull()
                        val sellVal = esell.toIntOrNull()
                        val qtyVal = eqty.toIntOrNull()

                        if (ename.isBlank() || buyVal == null || sellVal == null || qtyVal == null) {
                            Toast.makeText(ctx, "Please check all fields - numbers only", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        onEdit(
                            editIndex,
                            editItem!!.copy(
                                name = ename,
                                buy = buyVal,
                                sell = sellVal,
                                qty = qtyVal
                            )
                        )
                        Toast.makeText(ctx, "Updated", Toast.LENGTH_SHORT).show()
                        editItem = null
                    }
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
