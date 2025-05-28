package com.example.caja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caja.ViewModel.SendPaymentViewModel

class SendPaymentViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendPaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SendPaymentViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
