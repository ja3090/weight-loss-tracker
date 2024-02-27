package com.example.weightdojo.database.models

interface ToState <State> {
    fun toState(): State
}

interface ToModel <Model> {
    fun toModel(): Model
}