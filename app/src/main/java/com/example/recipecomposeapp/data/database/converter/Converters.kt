package com.example.recipecomposeapp.data.database.converter

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @ColumnTypeConverter
    fun fromString(value: String): List<String> =
        if (value.isEmpty()) emptyList() else Json.decodeFromString(value)

    @ColumnTypeConverter
    fun fromList(value: List<String>): String = Json.encodeToString(value)
}
