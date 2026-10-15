package com.example.recipecomposeapp.data.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.recipecomposeapp.data.database.dao.CategoryDao
import com.example.recipecomposeapp.data.database.entity.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

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
