package com.example.caja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caja.ViewModel.DocumentViewModel

class DocumentViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocumentViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}