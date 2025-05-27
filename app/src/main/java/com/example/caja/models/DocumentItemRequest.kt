package com.example.caja.models

data class DocumentItemRequest(
    val item_id: Int,
    val quantity: Int,
    val sale_unit_price: String,
    val total: Double

)
