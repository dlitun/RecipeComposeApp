package com.example.recipecomposeapp.app.di

interface Factory<T> {
    fun create(): T
}
