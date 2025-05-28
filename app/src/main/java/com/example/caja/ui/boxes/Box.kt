package com.example.caja.ui.boxes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.caja.ViewModel.TablesViewModel
import com.example.caja.TablesViewModelFactory
import com.example.caja.models.Country
import com.example.caja.models.Department
import com.example.caja.models.District
import com.example.caja.models.IdentityDocumentType
import com.example.caja.models.Province


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
    //val countries = tables?.tables?.countries ?: emptyList<Country>()
   // val departments = tables?.tables?.departments ?: emptyList<Department>()
    //val provinces = tables?.tables?.provinces ?: emptyList<Province>()
    //val districts = tables?.tables?.districts ?: emptyList<District>()
    //val identityDocumentTypes = tables?.tables?.identityDocumentTypes ?: emptyList<IdentityDocumentType>()

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
        identityDocumentTypes, tablesViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CajaViewPerson(persons: List<Persons>, paymentViewModel: PaymentViewModel, countries: List<Country>,
                   departments: List<Department>,
                   provinces: List<Province>,
                   districts: List<District>,
                   identityDocumentTypes: List<IdentityDocumentType>,
                   tablesViewModel: TablesViewModel) {

    var mostrarFormulario by remember { mutableStateOf(false) }
    var tipoDocumento by remember { mutableStateOf("Factura") }
    val tiposDocumento = listOf("Factura", "Boleta", "Nota de Venta")
    var expanded by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<String?>(null) }
    val productosSeleccionados = paymentViewModel.itemSelect


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tiposDocumento.forEach { tipo ->
                Button(
                    onClick = { tipoDocumento = tipo },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoDocumento == tipo) Color(0xFF1976D2) else Color(0xFFADD8E6)
                    ),
                    modifier = Modifier.size(width = 90.dp, height = 30.dp)
                ) {
                    Text(tipo, fontSize = 12.sp)
                }
            }
            // Botón para mostrar numeración
            Button(
                onClick = { /* Mostrar numeración según tipoDocumento */ },
                modifier = Modifier.size(width = 90.dp, height = 30.dp)
            ) {
                Text("23424", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

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
                        // Aquí puedes agregar lógica para guardar el cliente
                    },
                    onCancelar = { mostrarFormulario = false }
                )
            } else {
                Button(
                    onClick = { mostrarFormulario = true },
                    modifier = Modifier
                        .width(80.dp)
                        .height(30.dp)
                ) {
                    Text("Nuevo Cliente", fontSize = 10.sp)
                }
            }

            // Selector de cliente ocupa el resto del espacio
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = clienteSeleccionado ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Cliente") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    persons.forEach { person ->
                        DropdownMenuItem(
                            text = { Text(person.name) },
                            onClick = {
                                clienteSeleccionado = person.name
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Lista de productos seleccionados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFB19CD9), RoundedCornerShape(24.dp))
                .padding(8.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(productosSeleccionados.size) { index ->
                    val producto = productosSeleccionados[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color.DarkGray, RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)

                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Text("Cantidad: ", color = Color.White)
                                    OutlinedTextField(
                                        value = producto.cantidad.toString(),
                                        onValueChange = { value: String ->
                                            val cantidad = value.toIntOrNull() ?: 0
                                            productosSeleccionados[index] = producto.copy(cantidad = cantidad)
                                        },
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(40.dp)
                                            .background(Color.White, RoundedCornerShape(8.dp)),
                                        singleLine = true,
                                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    IconButton(onClick = { paymentViewModel.eliminarProducto(index) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }

                                }

                            }
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {

                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Text("Precio: S/ ", color = Color.White)
                                    OutlinedTextField(
                                        value = producto.precio.toString(),

                                        onValueChange = { value: String ->
                                            val precio = value.toDoubleOrNull() ?: 0.0
                                            productosSeleccionados[index] = producto.copy(precio = precio)
                                        },
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(40.dp)
                                            .background(Color.White, RoundedCornerShape(8.dp)),
                                        singleLine = true,
                                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                                Text(
                                    "Total: S/ ${"%.2f".format(producto.precio * producto.cantidad)}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Fila inferior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón inferior izquierdo
            Button(
                onClick = { /* Lógica para crear cliente */ },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text("COBRAR", fontSize = 12.sp)
            }

            Text(
                text = "TOTAL 40",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

data class ProductoVenta(
    val nombre: String,
    val cantidad: Int,
    val precio: Double
)
