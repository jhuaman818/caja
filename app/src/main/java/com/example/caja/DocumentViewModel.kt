package com.example.caja

import com.example.caja.models.DocumentRequest
import com.example.caja.models.Items
import com.example.caja.interfces.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentViewModel {
}

fun pagarVenta(document: DocumentRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
    val call = RetrofitInstance.api.createDocument(document)
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            onError("Fallo de red: ${t.message}")
        }
    })
}