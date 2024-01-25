package com.example.weightdojo.utils

import android.util.Log

fun validateInput(newText: String): Boolean {
    if (newText.isEmpty()) return true
    if (newText.count() > 6) return false
    if (newText.startsWith("0") && newText.length > 1 && newText[1] == '0') return false
    if (newText.startsWith(".")) return false

    return newText.all { it.isDigit() } || newText.count { it == '.' } == 1
}