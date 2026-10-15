package com.example.recipecomposeapp.core.network

import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object NetworkConfig {
    const val BASE_URL = "https://recipes.androidsprint.ru/api/"

    val apiService: RecipesApiService by lazy {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RecipesApiService::class.java)
    }
}
