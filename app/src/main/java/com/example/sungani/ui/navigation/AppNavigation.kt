package com.example.sungani.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.sungani.data.*
import com.example.sungani.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    items: List<UiItem>,
    sales: List<SaleRecord>,
    shop: ShopDetails,
    history: List<DailyProfit>,
    yesterdayProfit: Int,
    onItemsChange: (List<UiItem>) -> Unit,
    onSalesChange: (List<SaleRecord>) -> Unit,
    onShopChange: (ShopDetails) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val Brown = Color(0xFF3E2723)
    val Amber = Color(0xFFFF8F00)
    val BgColor = Color(0xFFF6F2EB)

    Scaffold(
        containerColor = BgColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(shop.shopName.uppercase(), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Brown,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Stocks") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    label = { Text("Add") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    label = { Text("Report") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text("Shop") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> HomeScreen(items, sales, shop, yesterdayProfit, Brown, Amber)
                1 -> StocksScreen(
                    items = items,
                    brown = Brown,
                    onSell = { idx ->
                        val item = items[idx]
                        if (item.qty > 0) {
                            val updatedItems = items.toMutableList()
                            updatedItems[idx] = item.copy(qty = item.qty - 1)
                            onItemsChange(updatedItems)
                            onSalesChange(sales + SaleRecord(item.name, item.sell - item.buy, getTodayDate()))
                        }
                    },
                    onDelete = { idx ->
                        onItemsChange(items.filterIndexed { i, _ -> i != idx })
                    },
                    onEdit = { idx, newItem ->
                        val updatedItems = items.toMutableList()
                        updatedItems[idx] = newItem
                        onItemsChange(updatedItems)
                    }
                )
                2 -> AddScreen(Amber) { newItem ->
                    onItemsChange(items + newItem)
                    selectedTab = 1
                }
                3 -> ReportScreen(sales, history, yesterdayProfit, Brown)
                4 -> ShopScreen(shop, Brown, Amber, onShopChange, onLogout)
            }
        }
    }
}
