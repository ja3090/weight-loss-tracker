package com.example.weightdojo.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getOrdinalIndicator(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

fun returnNeatDate(date: LocalDate): String {
    val dayOfMonth = date.dayOfMonth

    return "${dayOfMonth}${getOrdinalIndicator(dayOfMonth)} ${date.format(monthYearFormatter)}"
}