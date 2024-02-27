package com.example.weightdojo.utils

import android.util.Log

fun validateInput(newText: String): Boolean {
    if (newText.isEmpty()) return true
    if (newText.startsWith("0") && newText.length > 1 && newText[1] == '0') return false
    if (newText.startsWith(".")) return false
    if (newText.count { it == '.' } > 1) return false
    val withoutDots = newText.filter { it != '.' }


    return withoutDots.all { it.isDigit() }
}