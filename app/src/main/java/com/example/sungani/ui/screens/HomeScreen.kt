package com.example.sungani.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.SaleRecord
import com.example.sungani.data.ShopDetails
import com.example.sungani.data.UiItem
import com.example.sungani.data.getTodayDate

@Composable
fun HomeScreen(
    items: List<UiItem>,
    sales: List<SaleRecord>,
    shop: ShopDetails,
    yesterdayProfit: Int,
    brown: Color,
    orange: Color
) {
    val todaySales = sales.filter { it.date == getTodayDate() }
    val todayProfit = todaySales.sumOf { it.profit }
    val stockValue = items.sumOf { it.buy * it.qty }
    val potentialSales = items.sumOf { it.sell * it.qty }
    val lowStock = items.filter { it.qty <= 5 }
    val bestSeller = todaySales.groupBy { it.name }.maxByOrNull { it.value.size }?.key ?: "None yet"
    val isProfitUp = todayProfit >= yesterdayProfit

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Card
        item {
            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.WavingHand, contentDescription = null, tint = orange, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Hello, ${shop.owner.ifEmpty { "Boss" }}!",
                            fontWeight = FontWeight.Bold,
                            color = brown,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${shop.shopName} • ${getTodayDate()}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Profit Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(brown),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("TODAY'S PROFIT", color = Color.White.copy(0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("K$todayProfit", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isProfitUp) Color(0xFFE8F5E9) else Color(0xFFEFEBE9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isProfitUp) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                                    contentDescription = null,
                                    tint = if (isProfitUp) Color(0xFF2E7D32) else Color(0xFF5D4037),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = if (isProfitUp) "Up from K$yesterdayProfit yesterday" else "Down from K$yesterdayProfit yesterday",
                                    color = if (isProfitUp) Color(0xFF2E7D32) else Color(0xFF5D4037),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 Mini Stats Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Total Items", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Text("${items.size} registered", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = brown)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Low Stock Alert", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Text("${lowStock.size} low", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = orange)
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Stock Value", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Text("K$stockValue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Potential Sales", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Text("K$potentialSales", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                        }
                    }
                }
            }
        }

        // Best Seller & Categories Row
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = orange, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Best Seller", fontSize = 10.sp, color = Color.Gray)
                            Text(bestSeller, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = brown)
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Category, contentDescription = null, tint = brown, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Categories", fontSize = 10.sp, color = Color.Gray)
                            Text("${items.map { it.category }.distinct().size} Active", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = brown)
                        }
                    }
                }
            }
        }

        // Restock Alert Section
        if (lowStock.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = orange)
                            Text("Restock Alert (${lowStock.size} items)", color = orange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        lowStock.forEach { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("${item.qty} remaining", color = orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Daily Business Tip Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEBE9)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = orange)
                    Text(
                        "Tip: Keep your buying prices updated to automatically calculate accurate daily profit!",
                        fontSize = 12.sp,
                        color = brown
                    )
                }
            }
        }
    }
}
