package com.example.caja.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caja.TokenManager
import com.example.caja.interfces.RetrofitInstance
import com.example.caja.models.DocumentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DocumentViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _document = MutableStateFlow<List<DocumentResponse>>(emptyList())
    val document: StateFlow<List<DocumentResponse>> = _document

    fun fetchDocuments() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = tokenManager.token.firstOrNull()

                if (token != null) {
                    val response = RetrofitInstance.api.getDocuments("Bearer $token")

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _document.value = it
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
}