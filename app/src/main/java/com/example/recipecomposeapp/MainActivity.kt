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
import com.example.recipecomposeapp.data.dto.CategoryDto
import com.example.recipecomposeapp.data.dto.RecipeDto
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val okHttpClient = OkHttpClient()
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
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

    override fun onDestroy() {
        threadPool.shutdown()
        super.onDestroy()
    }

    private fun loadCategories() {
        try {
            val request = Request.Builder()
                .url(CATEGORIES_URL)
                .build()
            val responseBody = okHttpClient.newCall(request).execute().use { response ->
                response.body?.string() ?: error("Пустое тело ответа категорий")
            }
            val categories = Json.decodeFromString<List<CategoryDto>>(responseBody)

            Log.d(
                TAG,
                "Поток: ${Thread.currentThread().name}, получено категорий: ${categories.size}"
            )

            categories.forEach { category ->
                threadPool.execute {
                    loadRecipes(category)
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Ошибка при загрузке категорий", exception)
        }
    }

    private fun loadRecipes(category: CategoryDto) {
        try {
            val recipesUrl = "$CATEGORIES_URL/${category.id}/recipes"
            val request = Request.Builder()
                .url(recipesUrl)
                .build()
            val responseBody = okHttpClient.newCall(request).execute().use { response ->
                response.body?.string()
                    ?: error("Пустое тело ответа рецептов категории ${category.title}")
            }
            val recipes = Json.decodeFromString<List<RecipeDto>>(responseBody)

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
        const val CATEGORIES_URL = "https://recipes.androidsprint.ru/api/category"
    }
}
