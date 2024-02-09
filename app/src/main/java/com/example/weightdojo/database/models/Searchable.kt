package com.example.weightdojo.database.models

interface Searchable {
    val name: String
    val protein: Float
    val cals: Float
    val fat: Float
    val carbs: Float
}