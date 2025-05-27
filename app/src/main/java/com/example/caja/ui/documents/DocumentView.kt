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

@Composable
fun ComprobanteVista() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botones BOLETA y NOTA VENTA
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

        // Tarjeta con datos y botones PDF
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("CLIENTE: JOSE HUAMAN PEZO", fontWeight = FontWeight.Bold)
                    Text("DOCUMENTO: 78718846")
                    Text("F001-234")
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