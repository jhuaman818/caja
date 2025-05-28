package com.example.caja.interfces

import com.example.caja.models.Items
import com.example.caja.models.Categories
import com.example.caja.models.LoginRequest
import com.example.caja.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import com.example.caja.models.Document
import com.example.caja.models.DocumentResponse
import com.example.caja.models.Persons
import com.example.caja.models.TablesResponse
import retrofit2.http.Header
import retrofit2.Response

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("items")
    suspend fun getItems(
        @Header("Authorization") token: String
    ): Response<List<Items>>

    @POST("documents/store")
    suspend fun enviarVenta(
        @Header("Authorization") token: String,
        @Body document: Document
    ): Response<Void>

    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<List<Categories>>

    @GET("documents")
    suspend fun getDocuments(
        @Header("Authorization") token: String
    ): Response<List<DocumentResponse>>

    @GET("persons")
    suspend fun getPersons(
        @Header("Authorization") token: String
    ): Response<List<Persons>>


    @GET("persons/tables")
    suspend fun getTables(
        @Header("Authorization") token: String
    ): Response<TablesResponse>
}
data class LoginRequest(val email: String, val password: String)