package com.example.recipecomposeapp.data.database.converter

import androidx.room3.ColumnTypeConverter

class Converters {
    @ColumnTypeConverter
    fun fromString(value: String): List<String> = value.split("|||")

    @ColumnTypeConverter
    fun fromList(value: List<String>): String = value.joinToString("|||")
}
