package com.example.caja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.caja.ui.login.LoginScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.caja.ui.home.HomeScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Inicializar tokenManager aquí
        tokenManager = TokenManager(applicationContext)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(onLoginSuccess = { token ->
                        lifecycleScope.launch {
                            tokenManager.saveToken(token)
                        }
                        navController.navigate("home")
                    })
                }
                composable("home") {
                    HomeScreen(tokenManager)
                }
            }
        }
    }
}




