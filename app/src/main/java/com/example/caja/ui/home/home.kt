package com.example.caja.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caja.TokenManager
import com.example.caja.ViewModel.PaymentViewModel
import com.example.caja.ui.boxes.CajaView
import com.example.caja.ui.documents.ComprobanteVista
import com.example.caja.ui.item.ProductListScreen
import com.example.caja.ui.reports.DashboardView

sealed class Screen(
    val title: String,
    val icon: ImageVector
) {
    object Products : Screen("Productos", Icons.Filled.Apps)
    object Box : Screen("Caja", Icons.Filled.ShoppingCart)
    object Reports : Screen("Reportes", Icons.Filled.Report)
    object Documents : Screen("Documentos", Icons.Filled.Description)
}

@Composable
fun HomeScreen(tokenManager: TokenManager) {
    val items = listOf(
        Screen.Products,
        Screen.Box,
        Screen.Reports,
        Screen.Documents
    )

    var selectedItem by remember { mutableIntStateOf(0) }
    val paymentViewModel: PaymentViewModel = viewModel()

    Scaffold(
        bottomBar = {
            ModernNavigationBar(
                items = items,
                selectedIndex = selectedItem,
                onItemSelected = { index -> selectedItem = index }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> ProductListScreen(tokenManager, paymentViewModel)
                1 -> CajaView(tokenManager, paymentViewModel)
                2 -> DashboardView()
                3 -> ComprobanteVista(tokenManager)
            }
        }
    }
}

@Composable
fun ModernNavigationBar(
    items: List<Screen>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF073f68) // Color de fondo para el ítem seleccionado

    NavigationBar(
        modifier = modifier
            .height(80.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            val iconSize by animateDpAsState(
                targetValue = if (selected) 28.dp else 24.dp,
                label = "iconSize"
            )

            // Modificador para asegurar espacio para el texto
            val itemModifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .heightIn(min = 56.dp) // Altura mínima para el ítem

            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                selected = selected,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    // Mantener íconos y texto en blanco cuando están seleccionados
                    selectedIconColor = Color.White,
                    selectedTextColor = Color (0xFF293161),

                    // Colores para estado no seleccionado
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,

                    // Usar el color seleccionado como fondo
                    indicatorColor = selectedColor
                ),
                modifier = itemModifier,
                alwaysShowLabel = true // Forzar a mostrar siempre el texto
            )
        }
    }
}