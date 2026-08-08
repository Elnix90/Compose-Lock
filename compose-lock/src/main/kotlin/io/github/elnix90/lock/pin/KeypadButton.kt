package io.github.elnix90.lock.pin

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun KeypadButton(
    modifier: Modifier = Modifier,
    icon: Int,
    tint: Color,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {

    Box(
        modifier = modifier.keyPadModifier(enabled, onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
internal fun KeypadButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
) {

    Box(
        modifier = modifier.keyPadModifier { onClick(text) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

