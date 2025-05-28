package com.example.caja.models

data class Document (

    val customer_id: Int,
    val series: String,
    val number: String,
    val date_of_issue: String,
    val time_of_issue: String,
    val status_type_id: String,
    val total: Double,
    val items: List<DocumentItem>
)