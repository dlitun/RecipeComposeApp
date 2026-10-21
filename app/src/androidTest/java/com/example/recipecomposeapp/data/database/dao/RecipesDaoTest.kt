package com.example.recipecomposeapp.data.database.dao

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecomposeapp.data.database.RecipesDatabase
import com.example.recipecomposeapp.data.database.entity.CategoryEntity
import com.example.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        categoryDao.insertCategories(
            listOf(
                category(id = 1, name = "Завтраки"),
                category(id = 2, name = "Обеды")
            )
        )

        val retrieved = categoryDao.getAllCategories().first()

        assertEquals(2, retrieved.size)
        assertEquals(listOf("Завтраки", "Обеды"), retrieved.map { it.name })
    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        categoryDao.insertCategories(listOf(category(id = 1, name = "Старое название")))
        categoryDao.insertCategories(listOf(category(id = 1, name = "Новое название")))

        val retrieved = categoryDao.getAllCategories().first()

        assertEquals(1, retrieved.size)
        assertEquals("Новое название", retrieved.single().name)
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {
        recipeDao.insertRecipes(
            listOf(
                recipe(id = 1, categoryId = 1, title = "Омлет"),
                recipe(id = 2, categoryId = 2, title = "Суп"),
                recipe(id = 3, categoryId = 1, title = "Каша")
            )
        )

        val retrieved = recipeDao.getRecipesByCategory(categoryId = 1).first()

        assertEquals(2, retrieved.size)
        assertEquals(setOf("Омлет", "Каша"), retrieved.map { it.title }.toSet())
        assertTrue(retrieved.all { it.categoryId == 1 })
    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        val retrieved = categoryDao.getAllCategories().first()

        assertTrue(retrieved.isEmpty())
    }

    private fun category(id: Int, name: String) = CategoryEntity(
        id = id,
        name = name,
        description = "Описание",
        imageUrl = ""
    )

    private fun recipe(id: Int, categoryId: Int, title: String) = RecipeEntity(
        id = id,
        title = title,
        categoryId = categoryId,
        imageUrl = "",
        ingredients = "[]",
        method = "[]"
    )
}
