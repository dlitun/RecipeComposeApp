package com.example.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.recipecomposeapp.core.network.NetworkConfig
import com.example.recipecomposeapp.core.network.api.RecipesApiService
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val apiService = retrofit.create(RecipesApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            loadCategories()
        }

        if (intent?.data != null) {
            deepLinkIntent = intent
        }
        enableEdgeToEdge()
        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data != null) {
            deepLinkIntent = intent
        }
        setIntent(intent)
    }

    private suspend fun loadCategories() {
        try {
            val categories = apiService.getCategories()

            Log.d(
                TAG,
                "Поток: ${Thread.currentThread().name}, получено категорий: ${categories.size}"
            )

            categories.forEach { category ->
                lifecycleScope.launch(Dispatchers.IO) {
                    loadRecipes(category)
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Ошибка при загрузке категорий", exception)
        }
    }

    private suspend fun loadRecipes(category: CategoryDto) {
        try {
            val recipes = apiService.getRecipesByCategory(category.id)

            Log.d(
                TAG,
                "Поток: ${Thread.currentThread().name}, " +
                    "категория: ${category.title}, получено рецептов: ${recipes.size}"
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Ошибка при загрузке рецептов категории ${category.title}", exception)
        }
    }

    private companion object {
        const val TAG = "Pool"
    }
}
