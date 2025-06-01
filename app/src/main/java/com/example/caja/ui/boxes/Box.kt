package com.example.caja.ui.boxes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caja.PersonViewModelFactory
import com.example.caja.TokenManager
import com.example.caja.ViewModel.PaymentViewModel
import com.example.caja.ViewModel.PersonViewModel
import com.example.caja.models.Persons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.caja.ViewModel.TablesViewModel
import com.example.caja.TablesViewModelFactory
import com.example.caja.ViewModel.SendPaymentViewModel
import com.example.caja.models.Country
import com.example.caja.models.Department
import com.example.caja.models.District
import com.example.caja.models.Document
import com.example.caja.models.DocumentItem
import com.example.caja.models.IdentityDocumentType
import com.example.caja.models.Province
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import com.example.caja.SendPaymentViewModelFactory

@Composable
fun CajaView(tokenManager: TokenManager, paymentViewModel: PaymentViewModel,) {
    val viewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(tokenManager)
    )

    val personViewModel: PersonViewModel = viewModel(factory = PersonViewModelFactory(tokenManager))
    val tablesViewModel: TablesViewModel = viewModel(factory = TablesViewModelFactory(tokenManager))
    val persons by personViewModel.persons.collectAsState()
    val clientes = persons.map { it.name }
    val paymentViewModel: PaymentViewModel = viewModel()
    val tables by tablesViewModel.tablesResponse.collectAsState()
    val sendPaymentViewModel: SendPaymentViewModel = viewModel(factory = SendPaymentViewModelFactory(tokenManager))

    val countries = tables?.countries ?: emptyList<Country>()
    val departments = tables?.departments ?: emptyList<Department>()
    val provinces = tables?.provinces ?: emptyList<Province>()
    val districts = tables?.districts ?: emptyList<District>()
    val identityDocumentTypes = emptyList<IdentityDocumentType>()




    LaunchedEffect(Unit) {
        personViewModel.fetchPersons()
        tablesViewModel.fetchTables()
    }
    CajaViewPerson(persons, paymentViewModel, countries,
        departments,
        provinces,
        districts,
        identityDocumentTypes, tablesViewModel, sendPaymentViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CajaViewPerson(persons: List<Persons>, paymentViewModel: PaymentViewModel, countries: List<Country>,
                   departments: List<Department>,
                   provinces: List<Province>,
                   districts: List<District>,
                   identityDocumentTypes: List<IdentityDocumentType>,
                   tablesViewModel: TablesViewModel, sendPaymentViewModel: SendPaymentViewModel) {

    var mostrarFormulario by remember { mutableStateOf(false) }
    var tipoDocumento by remember { mutableStateOf("Factura") }
    val tiposDocumento = listOf("Factura", "Boleta", "Nota de Venta")
    var expanded by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<String?>(null) }
    val productosSeleccionados = paymentViewModel.itemSelect
    val total = productosSeleccionados.sumOf { it.precio * it.cantidad }
    val horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    val fechaActual = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val cliente = persons.find { it.name == clienteSeleccionado }
    val customerId = cliente?.id ?: 0
    val snackbarHostState = remember { SnackbarHostState() }
    val ventaExitosa by sendPaymentViewModel.ventaExitosa.collectAsState()


    LaunchedEffect(ventaExitosa) {
        if (ventaExitosa) {
            paymentViewModel.itemSelect.clear()
            snackbarHostState.showSnackbar("¡Venta realizada con éxito!")
            sendPaymentViewModel.resetVentaExitosa()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Botones de tipo de documento mejorados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botones de tipo de documento con mejor manejo de texto
                tiposDocumento.forEach { tipo ->
                    Button(
                        onClick = { tipoDocumento = tipo },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tipoDocumento == tipo) Color(0xFF1976D2) else Color.Transparent,
                            contentColor = if (tipoDocumento == tipo) Color.White else Color(0xFF1976D2)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (tipoDocumento == tipo) Color(0xFF1976D2) else Color(0xFF1976D2).copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = when(tipo) {
                                "Factura" -> "FAC"
                                "Boleta" -> "BOL"
                                "Nota de Venta" -> "N/V"
                                else -> tipo
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Display de numeración con mejor visibilidad
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(110.dp)  // Ajuste de ancho para balance
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Documento",
                            tint = Color(0xFF757575),
                            modifier = Modifier.size(16.dp) // Icono más pequeño
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "23424",
                            fontSize = 13.sp, // Tamaño reducido para mejor ajuste
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón "Nuevo Cliente" con mejor visibilidad de texto
                if (mostrarFormulario) {
                    NewPersonForm(
                        tablesViewModel = tablesViewModel,
                        countries = countries,
                        departments = departments,
                        provinces = provinces,
                        districts = districts,
                        identityDocumentTypes = identityDocumentTypes,
                        onGuardar = { nuevoCliente ->
                            mostrarFormulario = false
                        },
                        onCancelar = { mostrarFormulario = false }
                    )
                } else {
                    Button(
                        onClick = { mostrarFormulario = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .width(120.dp)  // Aumentado para mejor visibilidad
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Nuevo Cliente",
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Nuevo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                }

                // Selector de cliente optimizado para visibilidad
                // Selector de cliente con placeholder en lugar de label
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = clienteSeleccionado ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                "Seleccionar cliente",
                                fontSize = 14.sp,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Cliente",
                                tint = Color(0xFF1976D2))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE3F2FD),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .height(56.dp)
                            .horizontalScroll(rememberScrollState()) // Permite desplazar texto largo
                            .padding(horizontal = 8.dp),
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true)
                    ) {
                        persons.forEach { person ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = person.name,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                onClick = {
                                    clienteSeleccionado = person.name
                                    expanded = false
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Lista de productos seleccionados mejorada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                if (productosSeleccionados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos seleccionados",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(productosSeleccionados.size) { index ->
                            val producto = productosSeleccionados[index]

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    // Fila superior: Nombre y acciones
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = producto.nombre,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF333333),
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        IconButton(
                                            onClick = { paymentViewModel.eliminarProducto(index) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color(0xFFE53935)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    // Fila media: Cantidad y Precio Unitario - Versión optimizada
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Columna de Cantidad (40% del ancho)
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.weight(0.4f)
                                        ) {
                                            Text(
                                                "Cantidad",
                                                fontSize = 14.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                                                    .border(1.dp, Color(0xFFBBDEFB), RoundedCornerShape(8.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    IconButton(
                                                        onClick = {
                                                            if (producto.cantidad > 1) {
                                                                productosSeleccionados[index] = producto.copy(cantidad = producto.cantidad - 1)
                                                            }
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Remove,
                                                            contentDescription = "Disminuir",
                                                            tint = Color(0xFF1976D2)
                                                        )
                                                    }

                                                    Text(
                                                        producto.cantidad.toString(),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF1976D2),
                                                        modifier = Modifier.padding(horizontal = 8.dp)
                                                    )

                                                    IconButton(
                                                        onClick = {
                                                            productosSeleccionados[index] = producto.copy(cantidad = producto.cantidad + 1)
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Add,
                                                            contentDescription = "Aumentar",
                                                            tint = Color(0xFF1976D2)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Columna de Precio Unitario (60% del ancho)
                                        // Versión editable optimizada
                                        Column(
                                            horizontalAlignment = Alignment.Start,
                                            modifier = Modifier.weight(0.6f)
                                        ) {
                                            Text(
                                                "Precio Unitario",
                                                fontSize = 14.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                                                    .border(1.dp, Color(0xFFBBDEFB), RoundedCornerShape(8.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                BasicTextField(
                                                    value = producto.precio.toString(),
                                                    onValueChange = { value: String ->
                                                        val precio = value.toDoubleOrNull() ?: producto.precio
                                                        productosSeleccionados[index] = producto.copy(precio = precio)
                                                    },
                                                    textStyle = LocalTextStyle.current.copy(
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF1976D2),
                                                        textAlign = TextAlign.End
                                                    ),
                                                    singleLine = true,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                                        .horizontalScroll(rememberScrollState())
                                                ) { innerTextField ->
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            "S/ ",
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = Color(0xFF1976D2)
                                                        )
                                                        Box(modifier = Modifier.weight(1f)) {
                                                            innerTextField()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    // Fila inferior: Total
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Total:",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF333333)
                                        )

                                        Text(
                                            text = "S/ ${"%.2f".format(producto.precio * producto.cantidad)}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1976D2)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón de Cobrar mejorado
                Button(
                    onClick = {
                        val document = Document(
                            customer_id = customerId,
                            series = "B001",
                            number = "0001",
                            date_of_issue = fechaActual,
                            time_of_issue = horaActual,
                            status_type_id = "01",
                            total = total,
                            items = productosSeleccionados.map { producto ->
                                DocumentItem(
                                    item_id = producto.id,
                                    quantity = producto.cantidad,
                                    sale_unit_price = producto.precio,
                                    total = producto.precio * producto.cantidad
                                )
                            }
                        )
                        sendPaymentViewModel.enviarVenta(document)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(52.dp)
                        .weight(0.45f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cobrar",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("COBRAR",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.width(16.dp))

                // Sección de Total mejorada
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .weight(0.55f)
                        .background(Color(0xFF1976D2), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF0D47A1), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "TOTAL A PAGAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE3F2FD)
                            )
                            Text(
                                text = "S/ ${"%.2f".format(total)}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // SnackbarHost posicionado correctamente
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp) // Espacio suficiente para no tapar el botón
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFF4CAF50), // Color verde
                contentColor = Color.White, // Texto blanco
                shape = RoundedCornerShape(12.dp), // Bordes redondeados
                modifier = Modifier
                    .padding(horizontal = 16.dp) // Margen horizontal
            )
        }
    }
}

data class ProductoVenta(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val precio: Double
)
