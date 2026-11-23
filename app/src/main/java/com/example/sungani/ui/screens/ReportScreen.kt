package com.example.sungani.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sungani.data.DailyProfit
import com.example.sungani.data.SaleRecord
import com.example.sungani.data.getTodayDate

@Composable
fun ReportScreen(
    sales: List<SaleRecord>,
    history: List<DailyProfit>,
    yesterdayProfit: Int,
    brown: Color
) {
    val todaySales = sales.filter { it.date == getTodayDate() }
    val todayProfit = todaySales.sumOf { it.profit }
    val orange = Color(0xFFFF8F00)
    val isUp = todayProfit >= yesterdayProfit

    // Group today's sales breakdown
    val salesBreakdown = todaySales.groupBy { it.name }.map { (name, list) ->
        Triple(name, list.size, list.sumOf { it.profit })
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Profit Report & Analytics", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = brown)
        }

        // Today vs Yesterday Comparison Cards
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(brown),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Today (${getTodayDate()})", fontSize = 11.sp, color = Color.White.copy(0.8f))
                        Text("K$todayProfit", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${todaySales.size} sales recorded", fontSize = 11.sp, color = Color.White.copy(0.8f))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Yesterday", fontSize = 11.sp, color = Color.Gray)
                        Text("K$yesterdayProfit", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = brown)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isUp) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                                contentDescription = null,
                                tint = if (isUp) Color(0xFF2E7D32) else Color(0xFF5D4037),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = if (isUp) "▲ Up" else "▼ Down",
                                color = if (isUp) Color(0xFF2E7D32) else Color(0xFF5D4037),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 7-Day Trend Scrollable Bar Chart Card
        item {
            val chartData = history + DailyProfit(getTodayDate(), todayProfit)
            val maxProfit = (chartData.maxOfOrNull { it.profit } ?: 1).coerceAtLeast(1)
            val chartScrollState = rememberScrollState()

            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.BarChart, contentDescription = null, tint = brown)
                            Text("7-Day Profit Trend", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = brown)
                        }
                        Text("Scroll →", fontSize = 11.sp, color = Color.Gray)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .horizontalScroll(chartScrollState),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        chartData.forEach { record ->
                            val isToday = record.date == getTodayDate()
                            val barHeight = ((record.profit.toFloat() / maxProfit) * 100).coerceAtLeast(20f).dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.height(150.dp)
                            ) {
                                Text(
                                    "K${record.profit}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isToday) orange else brown
                                )
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(barHeight)
                                        .background(
                                            color = if (isToday) orange else brown,
                                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        )
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = if (record.date.length >= 5) record.date.takeLast(5) else record.date,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Today's Sales Breakdown Card
        item {
            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = brown)
                        Text("Today's Sales Breakdown (${todaySales.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = brown)
                    }

                    if (salesBreakdown.isEmpty()) {
                        Text("No sales yet today", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 12.dp))
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .heightIn(max = 260.dp)
                                .fillMaxWidth()
                        ) {
                            salesBreakdown.forEach { (name, qtySold, totalProfit) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF6F2EB), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("$qtySold sold today", fontSize = 11.sp, color = Color.Gray)
                                    }
                                    Text("+K$totalProfit", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
