package com.example.weightdojo.screens.home.addmodal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.ui.Sizing

@Composable
fun ModalFrame(title: String, content: @Composable (() -> Unit)) {

    Column(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.medium)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.secondary)
    ) {
        Heading(
            text = title,
            modifier = Modifier.padding(horizontal = Sizing.paddings.medium)
        )
        CustomDivider(tinted = false)

        content()
    }
}