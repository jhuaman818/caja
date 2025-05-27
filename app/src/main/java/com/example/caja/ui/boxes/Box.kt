package com.example.caja.ui.boxes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.example.caja.models.DocumentRequest
import com.example.caja.models.DocumentItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.caja.TokenManager
import com.example.caja.models.Items
import com.example.caja.interfces.RetrofitInstance
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun CajaView(tokenManager: TokenManager) {


    val apiService = RetrofitInstance.api
    val productos = remember { mutableStateListOf<Items>() }
    val carrito = remember { mutableStateListOf<Items>() }


    // Cargar los ítems desde la API
    LaunchedEffect(Unit) {
        try {
            val token = tokenManager.token.firstOrNull() // 👈 OBTENER el token de DataStore

            if (!token.isNullOrEmpty()) {
                val response = apiService.getItems("Bearer $token") // 👈 MANDAR el token en la request

                if (response.isSuccessful) {
                    response.body()?.let {
                        productos.clear()
                        productos.addAll(it)
                    }
                } else {
                    println("❌ Error al obtener ítems: ${response.errorBody()?.string()}")
                }
            } else {
                println("❌ Token no disponible")
            }
        } catch (e: Exception) {
            println("❌ Error de red: ${e.message}")
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Lista de productos
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productos) { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(producto.description, fontWeight = FontWeight.Bold)
                        Text("S/ ${producto.price}")
                    }
                    Button(onClick = {
                        val existente = carrito.find { it.id == producto.id }
                        if (existente != null) {
                            existente.quantity += 1
                        } else {
                            carrito.add(producto.copy(quantity = 1))
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar total
        val total = carrito.sumOf { it.price.toDouble() * it.quantity }

        Text("Total: S/ $total", fontWeight = FontWeight.Bold)

        // Botón pagar
        Button(
            onClick = {
                val itemsRequest = carrito.map {
                    DocumentItemRequest(
                        item_id = it.id,
                        quantity = it.quantity,
                        sale_unit_price = it.price.toString(),
                        total = it.price  * it.quantity
                    )
                }

                // Fecha y hora actual
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                val ventaRequest = DocumentRequest(
                    customer_id = 1, // Reemplazar si seleccionás un cliente
                    series = "F001",
                    number = "0001",
                    date_of_issue = fechaActual,
                    time_of_issue = horaActual,
                    status_type_id = "01", // Factura
                    total = total,
                    items = itemsRequest
                )

                // Enviar venta
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pagar")
        }
    }
}