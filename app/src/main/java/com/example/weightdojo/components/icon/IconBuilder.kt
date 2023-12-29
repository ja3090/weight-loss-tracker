package com.example.weightdojo.components.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconBuilder(
    modifier: Modifier = Modifier,
    id: Int,
    contentDescription: String,
    testTag: String,
    color: Color = MaterialTheme.colors.primaryVariant,
    onClick: (() -> Unit)? = null,
) {
    Image(
        painter = painterResource(id = id),
        contentDescription = contentDescription,
        modifier = Modifier
            .clickable { if (onClick !== null) onClick() }
            .testTag(testTag)
            .then(modifier),
        colorFilter = ColorFilter.tint(color),
        contentScale = ContentScale.Inside,
        alignment = Alignment.Center
    )
}