package com.example.caja.ui.documents
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.caja.ViewModel.DocumentViewModel
import com.example.caja.DocumentViewModelFactory
import com.example.caja.TokenManager
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun ComprobanteVista(tokenManager: TokenManager) {
    val documentViewModel: DocumentViewModel = viewModel(
        factory = DocumentViewModelFactory(tokenManager)
    )
    val documentos by documentViewModel.document.collectAsState()
    LaunchedEffect(Unit) {
        documentViewModel.fetchDocuments()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botones arriba
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { /* Acción Boleta */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBBFF))
            ) {
                Text("BOLETA FACTURA", color = Color.Black)
            }
            Button(
                onClick = { /* Acción Nota Venta */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBBFF))
            ) {
                Text("NOTA VENTA", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista con scroll
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa el espacio restante
        ) {
            items(documentos) { doc ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CLIENTE: ${doc.customer_name ?: "Cargando..."}", fontWeight = FontWeight.Bold)
                            Text("DOCUMENTO: ${doc.customer_number ?: ""}")
                            Text("${doc.series ?: ""}-${doc.number ?: ""}")
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(3) {
                                Button(
                                    onClick = { /* Acción PDF */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(50),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("PDF")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}