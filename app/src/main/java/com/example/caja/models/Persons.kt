package com.example.caja.models

data class Persons(
    val id: Int,
    val name: String,
    val number: String,
    val trade_name: String,
    val country_id: String,
    val department_id: Int,
    val province_id: Int?,
    val district_id: String?,
    val address: String,
    val email: String,
    val telephone: String,
    val identity_document_type_id: String?,
    val updated_at: String?,
)