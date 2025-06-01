package com.example.caja.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.input.PasswordVisualTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.ui.graphics.Color
import com.example.caja.interfces.RetrofitInstance
import com.example.caja.models.LoginRequest
import com.example.caja.models.LoginResponse

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.caja.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // Estado para mostrar error
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var loginSuccessToken by remember { mutableStateOf("") }

    // Efecto para manejar el mensaje de éxito
    if (showSuccess) {
        LaunchedEffect(Unit) {
            delay(500)
            showSuccess = false
            onLoginSuccess(loginSuccessToken)
        }
    }

    // Efecto para ocultar errores automáticamente
    if (showError) {
        LaunchedEffect(Unit) {
            delay(2000) // 3 segundos para errores
            showError = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF073f68)) // Azul Material
    ) {

        // Contenedor principal
        Column(modifier = Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center) {
            // Imagen agregada (ajusta el resource y descripción)
            Image(
                painter = painterResource(id = R.drawable.dragon), // Reemplaza con tu recurso
                contentDescription = "Imagen descriptiva",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)  // Ajusta según necesidad
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contenedor unificado para campos y botón
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    label = { Text("Email", color = Color.Black, fontSize = 16.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color (0xFF073f68)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF073f68),
                        unfocusedIndicatorColor = Color(0xFF073f68).copy(alpha = 0.7f),
                        focusedLabelColor = Color(0xFF073f68),
                        unfocusedLabelColor = Color(0xFF073f68).copy(alpha = 0.7f),
                        cursorColor = Color(0xFF073f68)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                // Campo de contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña", color = Color.Black, fontSize = 16.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña",
                            tint = Color (0xFF073f68)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF073f68),
                        unfocusedIndicatorColor = Color(0xFF073f68).copy(alpha = 0.7f),
                        focusedLabelColor = Color(0xFF073f68),
                        unfocusedLabelColor = Color(0xFF073f68).copy(alpha = 0.7f),
                        cursorColor = Color(0xFF073f68)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón
                Button(
                    onClick = {
                        val request = LoginRequest(email, password)
                        RetrofitInstance.api.login(request).enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                if (response.isSuccessful) {
                                    val token = response.body()?.token ?: ""

                                    loginSuccessToken = token
                                    showSuccess = true
                                } else {
                                    errorMessage = "Credenciales incorrectas"
                                    showError = true
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                errorMessage = "Error de conexión: ${t.message ?: "Desconocido"}"
                                showError = true
                            }
                        })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF073f68),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ingresar", fontWeight = FontWeight.Bold)
                }
            }

            // Espacio entre el botón/mensaje y la imagen
            Spacer(modifier = Modifier.height(24.dp))

            // Imagen agregada (ajusta el resource y descripción)
            Image(
                painter = painterResource(id = R.drawable.sdrimsac), // Reemplaza con tu recurso
                contentDescription = "Imagen descriptiva",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)  // Ajusta según necesidad
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            val context = LocalContext.current

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono con enlace
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/51995764963"))
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Whatsapp,
                        contentDescription = "Icono con enlace",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White // Cambia el color del ícono si es necesario
                    )
                }
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sdrimsac"))
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Facebook,
                        contentDescription = "Icono con enlace",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White // Cambia el color del ícono si es necesario
                    )
                }
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sdrimsac.com"))
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Icono con enlace",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White // Cambia el color del ícono si es necesario
                    )
                }
            }
        }
        // MENSAJE DE ÉXITO - ENCIMA DE TODO
        if (showSuccess) {
            CenteredMessage(
                icon = Icons.Filled.CheckCircle,
                iconColor = Color(0xFF4CAF50),
                message = "Inicio de sesión exitoso"
            )
        }

        // MENSAJE DE ERROR - ENCIMA DE TODO
        if (showError) {
            CenteredMessage(
                icon = Icons.Filled.Warning, // O Icons.Filled.Error
                iconColor = Color(0xFFF44336),
                message = errorMessage
            )
        }
    }
}

// Componente reutilizable para mensajes centrados
@Composable
fun CenteredMessage(
    icon: ImageVector,
    iconColor: Color,
    message: String,
    onDismiss: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss?.invoke() }, // Permite cerrar al hacer clic
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Mensaje",
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar botón solo para errores (opcional)
                if (onDismiss != null) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = iconColor
                        )
                    ) {
                        Text("Entendido")
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF073f68),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}