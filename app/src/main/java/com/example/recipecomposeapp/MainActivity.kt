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
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val requestThread = Thread {
            Log.d(TAG, "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            var connection: HttpURLConnection? = null
            try {
                val url = URL(CATEGORIES_URL)
                connection = url.openConnection() as? HttpURLConnection
                    ?: error("Не удалось создать HTTP-соединение")
                connection.requestMethod = "GET"
                connection.connect()

                Log.d(
                    TAG,
                    "Ответ сервера: ${connection.responseCode} ${connection.responseMessage}"
                )

                val responseBody = connection.inputStream
                    .bufferedReader()
                    .use { reader -> reader.readText() }
                Log.d(TAG, "Тело ответа: $responseBody")

                val categories = Json.decodeFromString<List<CategoryDto>>(responseBody)
                Log.d(TAG, "Получено категорий: ${categories.size}")
                categories.forEach { category ->
                    Log.d(TAG, "Категория: ${category.title}")
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Не удалось получить категории", exception)
            } finally {
                connection?.disconnect()
            }
        }
        requestThread.start()

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

    private companion object {
        const val TAG = "MainActivity"
        const val CATEGORIES_URL = "https://recipes.androidsprint.ru/api/category"
    }
}
