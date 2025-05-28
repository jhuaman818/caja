package com.example.caja.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caja.models.Document
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.caja.interfces.RetrofitInstance
import kotlinx.coroutines.flow.firstOrNull
import com.example.caja.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SendPaymentViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _ventaExitosa = MutableStateFlow(false)
    val ventaExitosa: StateFlow<Boolean> = _ventaExitosa
    /*fun enviarVenta(document: Document) {
        viewModelScope.launch {
            try {
                val token = tokenManager.token.firstOrNull()
                if (token != null) {
                    val response = RetrofitInstance.api.enviarVenta("Bearer $token", document)
                    if (response.isSuccessful) {
                        println("Venta realizada con éxito: ${response.body()}")
                    } else {
                        println("Error: ${response.code()} - ${response.message()}")
                    }

                } else {
                    println("Error: No hay token disponible")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
    fun enviarVenta(document: Document) {
        viewModelScope.launch {
            try {
                val token = tokenManager.token.firstOrNull()
                if (token != null) {
                    val response = RetrofitInstance.api.enviarVenta("Bearer $token", document)
                    if (response.code() == 201) {
                        _ventaExitosa.value = true
                    } else {
                        _ventaExitosa.value = false
                        println("Error: ${response.code()} - ${response.message()}")
                    }
                } else {
                    println("Error: No hay token disponible")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun resetVentaExitosa() {
        _ventaExitosa.value = false
    }

}