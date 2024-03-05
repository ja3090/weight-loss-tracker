package com.example.weightdojo.repositories.mealrepo

import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.dao.MealDao
import com.example.weightdojo.database.models.MealIngredient
import com.example.weightdojo.database.models.NutrimentIngredient
import com.example.weightdojo.database.models.NutrimentMeal
import com.example.weightdojo.datatransferobjects.Marked
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.datatransferobjects.SingleMealDetailedIngredient
import com.example.weightdojo.datatransferobjects.SingleMealDetailedNutriment
import com.example.weightdojo.utils.seeder.CalorieTotals
import com.example.weightdojo.utils.seeder.NutrimentTotals
import com.example.weightdojo.utils.totalGrams

interface DeleteMealTemplate {
    val database: AppDatabase
    val mealDao: MealDao

    fun deleteTemplateHandler(mealId: Long) {
        mealDao.deleteMeal(mealId)
        database.nutrimentMealDao().deleteNutrimentMeal(mealId)
        database.mealIngredientDao().deleteMealIngredientByMealId(mealId)
        database.ingredientDao().deleteRedundantIngredients()
        database.nutrimentIngredientDao().deleteRedundantNutrimentIngredient()
    }
}