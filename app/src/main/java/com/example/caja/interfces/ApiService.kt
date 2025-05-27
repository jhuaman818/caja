package com.example.caja.interfces

import com.example.caja.models.Items
import com.example.caja.models.Categories
import com.example.caja.models.LoginRequest
import com.example.caja.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import com.example.caja.models.DocumentRequest
import retrofit2.http.Header
import retrofit2.Response

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("items")
    suspend fun getItems(
        @Header("Authorization") token: String
    ): Response<List<Items>>

    @POST("documents")
    fun createDocument(@Body request: DocumentRequest): Call<Void>

    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<List<Categories>>
}
data class LoginRequest(val email: String, val password: String)