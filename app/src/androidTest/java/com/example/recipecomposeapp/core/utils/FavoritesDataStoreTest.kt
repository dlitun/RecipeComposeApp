package com.example.recipecomposeapp.core.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesDataStoreTest {

    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        runBlocking { context.dataStore.edit { it.clear() } }
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)

        assertTrue(manager.getFavoriteIdsFlow().first().contains("42"))
    }

    @Test
    fun removeFavoriteDeletesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)
        manager.removeFavorite(recipeId = 42)

        assertFalse(manager.getFavoriteIdsFlow().first().contains("42"))
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        manager.getFavoriteIdsFlow().test {
            assertTrue(awaitItem().isEmpty())

            manager.addFavorite(recipeId = 42)

            assertEquals(setOf("42"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
