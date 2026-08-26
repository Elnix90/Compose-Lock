package io.github.elnix90.lock.pin.configuration

import androidx.annotation.FloatRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public data class KeyPadSettings(
    val pressedColor: Color,
    val defaultColor: Color,
    val specialColor: Color,
    val defaultTextColor: Color,
    val pressedTextColor: Color,
    @FloatRange(0.0, 1.0)
    val keyTransparency: Float
) {
    public companion object {
        public val defaultKeyPadSettings: KeyPadSettings
            @Composable
            get() = KeyPadSettings(
                pressedColor = MaterialTheme.colorScheme.secondary,
                defaultColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                specialColor = MaterialTheme.colorScheme.secondaryContainer,
                defaultTextColor = MaterialTheme.colorScheme.onSurface,
                pressedTextColor = MaterialTheme.colorScheme.background,
                keyTransparency = 0.85f
            )
    }
}