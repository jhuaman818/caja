package com.example.caja.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caja.TokenManager
import com.example.caja.interfces.RetrofitInstance
import com.example.caja.models.Persons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers

class PersonViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _persons = MutableStateFlow<List<Persons>>(emptyList())
    val persons: StateFlow<List<Persons>> = _persons

    fun fetchPersons() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = tokenManager.token.firstOrNull()

                if (token != null) {
                    val response = RetrofitInstance.api.getPersons("Bearer $token")

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _persons.value = it
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