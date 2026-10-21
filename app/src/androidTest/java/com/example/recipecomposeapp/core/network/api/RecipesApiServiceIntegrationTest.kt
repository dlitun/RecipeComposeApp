package com.example.recipecomposeapp.core.network.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class RecipesApiServiceIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RecipesApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer().also { it.start() }
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RecipesApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesJsonIsDeserializedIntoCategoryDto() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(
                    """[{"id":1,"title":"Завтраки","description":"Лёгкие блюда","imageUrl":"breakfast.jpg"}]"""
                )
        )

        val categories = apiService.getCategories()

        assertEquals(1, categories.size)
        assertEquals("Завтраки", categories.single().title)
        assertEquals("Лёгкие блюда", categories.single().description)
        assertEquals("/category", mockWebServer.takeRequest().path)
    }
}
