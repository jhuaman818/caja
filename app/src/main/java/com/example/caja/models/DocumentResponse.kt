package com.example.caja.models

data class DocumentResponse (
    val id: Int,
    val series: String,
    val number: String,
    val customer_name: String?,
    val customer_number: String?,
    val date_of_issue: String,
)