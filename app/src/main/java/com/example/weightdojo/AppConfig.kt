package com.example.weightdojo

const val PASSCODE_LENGTH = 4
const val DEPRECATED_MESSAGE = "Used for testing purposes. Do not use anywhere"
sealed class AppConfig {
    companion object {
        const val seedDatabase = true
    }
}

enum class TestTags {
    DELETE_BUTTON
}