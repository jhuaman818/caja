package com.example.caja.ui.home
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.caja.ui.boxes.CajaView
import com.example.caja.ui.documents.ComprobanteVista
import com.example.caja.ui.item.ProductListScreen
import com.example.caja.ui.reports.DashboardView
import com.example.caja.TokenManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caja.ViewModel.PaymentViewModel

@Composable
fun HomeScreen(tokenManager: TokenManager) {
    val items = listOf("Productos", "Caja", "Reporte", "documentos")
    val icons = listOf(Icons.Filled.ShoppingCart, Icons.Filled.ShoppingCart, Icons.Filled.Report, Icons.Filled.Description)
    var selectedItem by remember { mutableStateOf(0) }
    val paymentViewModel: PaymentViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> ProductListScreen(tokenManager, paymentViewModel) // ✅ Productos

                1 -> CajaView(tokenManager, paymentViewModel) // ✅ Caja
                2 -> DashboardView()
                3 -> ComprobanteVista()
            }
        }
    }
}
