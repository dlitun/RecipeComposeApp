package com.example.recipecomposeapp.data.database

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.recipecomposeapp.data.database.dao.CategoryDao
import com.example.recipecomposeapp.data.database.dao.RecipeDao
import com.example.recipecomposeapp.data.database.converter.Converters
import com.example.recipecomposeapp.data.database.entity.CategoryEntity
import com.example.recipecomposeapp.data.database.entity.RecipeEntity

@Database(
    entities = [CategoryEntity::class, RecipeEntity::class],
    version = 2,
    exportSchema = false
)
@ColumnTypeConverters(Converters::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        fun buildDatabase(context: Context): RecipesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RecipesDatabase::class.java,
                "recipes_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
