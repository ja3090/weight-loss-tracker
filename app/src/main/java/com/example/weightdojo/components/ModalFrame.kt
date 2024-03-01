package com.example.weightdojo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.R
import com.example.weightdojo.components.CustomDivider
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.text.Heading
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.ui.Sizing

@Composable
fun ModalFrame(
    onClose: () -> Unit,
    title: String, onBack: () -> Unit = onClose,
    content: @Composable (() -> Unit),
) {

    Column(
        modifier = Modifier
            .padding(horizontal = Sizing.paddings.medium)
            .clip(RoundedCornerShape(Sizing.cornerRounding))
            .background(MaterialTheme.colors.secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBuilder(
                id = R.drawable.arrow_back,
                contentDescription = "Go back",
                testTag = "BACK_BUTTON",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(Sizing.paddings.medium)
            )
            TextDefault(
                text = title,
                modifier = Modifier.padding(horizontal = Sizing.paddings.medium),
                fontWeight = FontWeight.Bold,
                fontSize = Sizing.font.default * 1.05
            )
            IconBuilder(
                id = R.drawable.close,
                contentDescription = "Close",
                testTag = "CLOSE",
                modifier = Modifier
                    .clickable { onClose() }
                    .padding(Sizing.paddings.medium)
            )
        }
        CustomDivider(tinted = false)

        content()
    }
}