package com.example.recipecomposeapp.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecomposeapp.MainActivity
import com.example.recipecomposeapp.e2e.screen.CategoriesComposeScreen
import com.example.recipecomposeapp.e2e.screen.RecipesComposeScreen
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesE2ETest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun categoriesScreenLoadsContent() = run {
        step("Открыть приложение и проверить экран категорий") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                assertIsDisplayed()
                categoriesGrid { assertIsDisplayed() }
            }
        }
    }

    @Test
    fun clickingCategoryOpensRecipesScreen() = run {
        step("Дождаться загрузки категорий") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                assertIsDisplayed()
                categoriesGrid { assertIsDisplayed() }
            }
        }

        step("Нажать на первую категорию") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                categoryItems[0]
                    .assertIsDisplayed()
                    .performClick()
            }
        }

        step("Проверить открытие экрана рецептов") {
            onComposeScreen<RecipesComposeScreen>(composeTestRule) {
                assertIsDisplayed()
            }
        }
    }
}
