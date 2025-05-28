package com.example.caja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caja.ViewModel.TablesViewModel

class TablesViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TablesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TablesViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}