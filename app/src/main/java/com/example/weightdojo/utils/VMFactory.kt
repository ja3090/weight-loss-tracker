package com.example.weightdojo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMFactory {
    companion object {
        fun <VM: ViewModel> build(initializer: () -> VM): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return initializer() as T
                }
            }
        }
    }
}