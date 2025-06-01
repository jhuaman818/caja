package com.example.caja.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardView() {
    val primaryColor = Color(0xFF073f68)
    val secondaryColor = Color(0xFF0A4A7A)
    val accentColor = Color(0xFF00C2FF)
    val cardGradient = Brush.verticalGradient(
        colors = listOf(primaryColor, secondaryColor)
    )

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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Resumen General",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        text = "Resumen de rendimiento comercial",
                        fontSize = 16.sp,
                        color = primaryColor.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.BarChart,
                        contentDescription = "Dashboard",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Tarjetas de métricas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Primera columna
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "Productos vendidos",
                        value = "150",
                        change = "+12% desde el mes pasado",
                        icon = Icons.Filled.LocalOffer,
                        primaryColor = primaryColor,
                        cardGradient = cardGradient,
                        accentColor = accentColor
                    )

                    DashboardCard(
                        title = "Documentos emitidos",
                        value = "87",
                        change = "+8% desde el mes pasado",
                        icon = Icons.Filled.Description,
                        primaryColor = primaryColor,
                        cardGradient = cardGradient,
                        accentColor = accentColor
                    )
                }

                // Segunda columna
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "Producto más vendido",
                        value = "Sneaker X",
                        change = "120 unidades vendidas",
                        icon = Icons.Filled.TrendingUp,
                        primaryColor = primaryColor,
                        cardGradient = cardGradient,
                        accentColor = accentColor
                    )

                    // Tarjeta de métrica adicional
                    DashboardCard(
                        title = "Tasa de conversión",
                        value = "68%",
                        change = "+15% desde el mes pasado",
                        icon = Icons.Filled.BarChart,
                        primaryColor = primaryColor,
                        cardGradient = cardGradient,
                        accentColor = accentColor
                    )
                }
            }

            // Gráfico de resumen (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Visualización de gráficos de rendimiento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    change: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    primaryColor: Color,
    cardGradient: Brush,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Barra decorativa superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(brush = cardGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = primaryColor.copy(alpha = 0.8f),
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(primaryColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = title,
                            tint = primaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = change,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = primaryColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}