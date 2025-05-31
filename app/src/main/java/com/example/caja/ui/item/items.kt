package com.example.caja.ui.item
import androidx.compose.foundation.BorderStroke
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.Alignment.Companion.CenterVertically
import com.example.caja.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow


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
            SearchBar(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .heightIn(min = 56.dp)
            )

            // Categorías
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                // Agrega un elemento para "Todas"
                item {
                    CategoryCircle(
                        label = "TODOS",
                        selected = selectedCategoryId == null,
                        onClick = { selectedCategoryId = null }
                    )
                }

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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .focusRequester(focusRequester),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color (0xFF293161) // Color del icono de búsqueda
            )
        },
        placeholder = {
            Text(
                "Buscar productos...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun CategoryCircle(label: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFF293161) else Color(0xFFF0F1F5)
    val contentColor = if (selected) Color.White else Color(0xFF293161)

    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(end = 8.dp)
            .height(36.dp),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border = if (selected) null else BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}


@Composable
fun ProductCard(item: Items, onFood: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F7)),
                contentAlignment = Alignment.Center
            ) {
                if (item.image.isNullOrEmpty()) {
                    // Placeholder desde recursos drawable
                    Image(
                        painter = painterResource(id = R.drawable.ic_product_placeholder),
                        contentDescription = "Placeholder",
                        modifier = Modifier.size(60.dp)
                    )
                } else {
                    val painter = rememberAsyncImagePainter(
                        model = item.image
                    )
                    Image(
                        painter = painter,
                        contentDescription = item.description,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Código: ${item.internal_id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "S/.${item.price}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF293161)
                    )

                    IconButton(
                        onClick = onFood,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFF293161),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Agregar al carrito",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}


