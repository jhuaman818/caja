package com.example.caja.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.caja.models.TablesResponse
import com.example.caja.TokenManager
import com.example.caja.interfces.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull


class TablesViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _tablesResponse = MutableStateFlow<TablesResponse?>(null)
    val tablesResponse: StateFlow<TablesResponse?> = _tablesResponse

    fun fetchTables() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = tokenManager.token.firstOrNull()
                if (token != null) {
                    println("Token obtenido: $token")
                    val response = RetrofitInstance.api.getTables("Bearer $token")
                    println("Respuesta recibida: ${response.code()} - ${response.message()}")
                    if (response.isSuccessful) {
                        response.body()?.let { tables: TablesResponse ->
                            println("Datos recibidos: $tables")
                            _tablesResponse.value = tables
                        } ?: println("El body de la respuesta es null")
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
    }
}