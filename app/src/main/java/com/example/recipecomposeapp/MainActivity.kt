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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        threadPool.execute {
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
        var connection: HttpURLConnection? = null

        try {
            connection = openConnection(CATEGORIES_URL)
            val responseBody = connection.inputStream
                .bufferedReader()
                .use { reader -> reader.readText() }
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
        } finally {
            connection?.disconnect()
        }
    }

    private fun loadRecipes(category: CategoryDto) {
        var connection: HttpURLConnection? = null

        try {
            val recipesUrl = "$CATEGORIES_URL/${category.id}/recipes"
            connection = openConnection(recipesUrl)
            val responseBody = connection.inputStream
                .bufferedReader()
                .use { reader -> reader.readText() }
            val recipes = Json.decodeFromString<List<RecipeDto>>(responseBody)

            Log.d(
                TAG,
                "Поток: ${Thread.currentThread().name}, " +
                    "категория: ${category.title}, получено рецептов: ${recipes.size}"
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Ошибка при загрузке рецептов категории ${category.title}", exception)
        } finally {
            connection?.disconnect()
        }
    }

    private fun openConnection(url: String): HttpURLConnection {
        return (URL(url).openConnection() as? HttpURLConnection)
            ?.apply {
                requestMethod = "GET"
                connect()
            }
            ?: error("Не удалось создать HTTP-соединение для $url")
    }

    private companion object {
        const val TAG = "Pool"
        const val CATEGORIES_URL = "https://recipes.androidsprint.ru/api/category"
    }
}
