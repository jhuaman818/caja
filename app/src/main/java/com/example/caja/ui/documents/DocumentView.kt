package com.example.caja.ui.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caja.DocumentViewModelFactory
import com.example.caja.TokenManager
import com.example.caja.ViewModel.DocumentViewModel
import com.example.caja.models.Document

@Composable
fun ComprobanteVista(tokenManager: TokenManager) {
    val documentViewModel: DocumentViewModel = viewModel(
        factory = DocumentViewModelFactory(tokenManager)
    )
    val documentos by documentViewModel.document.collectAsState()
    val primaryColor = Color(0xFF073f68)
    val secondaryColor = Color(0xFF0A4A7A)
    val accentColor = Color(0xFF00C2FF)

    LaunchedEffect(Unit) {
        documentViewModel.fetchDocuments()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF0F7FF), Color(0xFFD8E9F8))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Encabezado con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(primaryColor, secondaryColor)
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Mis Comprobantes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            // Botones de filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón para Boletas/Facturas
                Button(
                    onClick = { /* Acción Boleta/Factura */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Boleta/Factura")
                }

                // Botón para Notas de Venta
                Button(
                    onClick = { /* Acción Nota Venta */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = primaryColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Nota Venta")
                }
            }

            // Lista de comprobantes
            if (documentos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay comprobantes disponibles",
                        fontSize = 18.sp,
                        color = primaryColor
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(documentos) { doc ->
                        DocumentCard(
                            series = doc.series ?: "",
                            number = doc.number ?: "",
                            date = doc.date_of_issue ?: "",
                            customerName = doc.customer_name ?: "Cliente no disponible",
                            customerNumber = doc.customer_number ?: "",
                            primaryColor = primaryColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentCard(
    series: String,
    number: String,
    date: String,
    customerName: String,
    customerNumber: String,
    primaryColor: Color,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado con número de documento
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$series-$number",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del cliente
            Column {
                Text(
                    text = "CLIENTE:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = customerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (customerNumber.isNotEmpty()) {
                    Text(
                        text = "DOCUMENTO: $customerNumber",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de acción
            Button(
                onClick = { /* Acción PDF */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Icon(
                    Icons.Default.FileDownload,
                    contentDescription = "PDF",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Descargar PDF")
            }
        }
    }
}