package com.example.caja.models

data class Items(
    val id: Int,
    val internal_id: String,
    val barcode: String?,
    val description: String,
    val description_secondary: String,
    val category_id: Int,
    val brand_id: Int?,
    val image: String?,
    val stock: String,
    val stock_min: String,
    val price: Double,
    val created_at: String?,
    val updated_at: String?,
    val unit_type_id: String?,
)
