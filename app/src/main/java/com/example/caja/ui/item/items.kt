package com.example.caja.ui.item
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caja.ItemViewModel
import com.example.caja.models.Items
import com.example.caja.TokenManager
import com.example.caja.ItemViewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyRow
import com.example.caja.ViewModel.PaymentViewModel
import com.example.caja.ui.boxes.ProductoVenta


@Composable
fun ProductListScreen(tokenManager: TokenManager, paymentViewModel: PaymentViewModel) {
    val viewModel: ItemViewModel = viewModel(
       factory = ItemViewModelFactory(tokenManager)
    )
    val items by viewModel.items.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val categories by viewModel.categories.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

    val filteredItems = items.filter {
        (searchText.isBlank() || it.description.contains(searchText, ignoreCase = true)) &&
                (selectedCategoryId == null || it.category_id == selectedCategoryId)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchItems()
       viewModel.fetchCategories()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(8.dp)) {
            // Buscador
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Categorías
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(categories) { category ->
                    CategoryCircle(
                        label = category.category,
                        selected = category.id == selectedCategoryId,
                        onClick = {
                            selectedCategoryId = if (selectedCategoryId == category.id) null else category.id
                        }
                    )
                }
            }

            // Lista de productos
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                items(filteredItems) { item ->
                    ProductCard(item = item, onFood = {
                        val nuevo = paymentViewModel.agregarProducto(
                            ProductoVenta(item.id, item.description, 1, item.price)
                        )
                        snackbarMessage = if (nuevo) "Producto agregado" else "Cantidad actualizada"
                    })
                }
            }
        }
    }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
}

@Composable
fun CategoryCircle(label: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF6200EE) else Color(0xFFCCCCFF),
            contentColor = Color.Black
        ),
        modifier = Modifier.size(60.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = label.take(3).uppercase(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProductCard(item: Items, onFood: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val painter = rememberAsyncImagePainter(
                model = item.image ?: "https://via.placeholder.com/150"
            )
            Image(
                painter = painter,
                contentDescription = item.description,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "S/.${item.price}", fontWeight = FontWeight.Bold)
            Text(text = item.description)
            Text(text = item.internal_id)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = onFood,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Agregar al carrito",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


