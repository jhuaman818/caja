package com.example.caja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caja.interfces.RetrofitInstance
import com.example.caja.models.Items
import com.example.caja.models.Categories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers

class ItemViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _items = MutableStateFlow<List<Items>>(emptyList())
    val items: StateFlow<List<Items>> = _items

    private val _categories = MutableStateFlow<List<Categories>>(emptyList())
    val categories: StateFlow<List<Categories>> = _categories

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = tokenManager.token.firstOrNull()

                if (token != null) {
                    val response = RetrofitInstance.api.getItems("Bearer $token")

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _items.value = it
                        }
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
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val token = tokenManager.token.firstOrNull()
                val response = RetrofitInstance.api.getCategories("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { _categories.value = it }
                }
            } catch (e: Exception) {
                println("❌ Error al cargar categorías: ${e.message}")
            }
        }
    }
}