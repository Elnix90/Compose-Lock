package io.github.elnix90.lock.pin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.elnix90.lock.R
import io.github.elnix90.lock.pin.configuration.KeyPadSettings

@Composable
internal fun KeypadButton(
    icon: Int,
    keyPadSettings: KeyPadSettings,
    onLongClick: (() -> Unit)?,
    onClick: () -> Unit
) {
    val state = rememberKeyPadState(
        keyPadSettings = keyPadSettings,
        specialBg = true,
        onLongClick = onLongClick,
        onClick = onClick
    )

    Box(
        modifier = state.modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = state.animatedTextColor.value,
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
internal fun KeypadButton(
    text: String,
    keyPadSettings: KeyPadSettings,
    onClick: (String) -> Unit,
) {
    val state = rememberKeyPadState(
        keyPadSettings = keyPadSettings,
        specialBg = false,
        onLongClick = null
    ) { onClick(text) }

    Box(
        modifier = state.modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = state.animatedTextColor.value,
            style = TextStyle(
                fontSize = if (state.isPressed) 32.sp else 30.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily(Font(R.font.roboto_mono_variable)),
            ),
            modifier = Modifier.graphicsLayer {
                if (state.isPressed) {
                    scaleX = 0.9f
                }
            }
        )
    }
}
