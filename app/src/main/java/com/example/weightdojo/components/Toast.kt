package com.example.weightdojo.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun toast(string: String?, context: Context) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}