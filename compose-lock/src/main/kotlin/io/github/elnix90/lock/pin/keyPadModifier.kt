package io.github.elnix90.lock.pin

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import io.github.elnix90.lock.pin.configuration.KeyPadSettings
import kotlinx.coroutines.launch
import kotlin.math.roundToInt



internal data class KeyPadState(
    val isPressed: Boolean,
    val animatedColor: Animatable<Color, AnimationVector4D>,
    val animatedTextColor: Animatable<Color, AnimationVector4D>,
    val modifier: Modifier
)


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun rememberKeyPadState(
    keyPadSettings: KeyPadSettings,
    specialBg: Boolean,
    onLongClick: (() -> Unit)?,
    onClick: () -> Unit
): KeyPadState {
    val haptic = LocalHapticFeedback.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedColor = remember { Animatable(if (specialBg) keyPadSettings.specialColor else keyPadSettings.defaultColor) }
    val animatedTextColor = remember { Animatable(keyPadSettings.defaultTextColor) }
    val animatedRoundedCorner = remember { Animatable(50f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
            launch { animatedRoundedCorner.snapTo(12f) }
            launch { animatedColor.snapTo(keyPadSettings.pressedColor) }
            launch { animatedTextColor.snapTo(keyPadSettings.pressedTextColor) }
        } else {
            launch {
                animatedRoundedCorner.animateTo(
                    targetValue = 50f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                animatedColor.animateTo(
                    targetValue = if (specialBg) keyPadSettings.specialColor else keyPadSettings.defaultColor,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                animatedTextColor.animateTo(
                    targetValue = keyPadSettings.defaultTextColor,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }

    val modifier = Modifier
        .size(85.dp)
        .clip(RoundedCornerShape(animatedRoundedCorner.value.roundToInt()))
        .background(animatedColor.value.copy(keyPadSettings.keyTransparency))
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
            interactionSource = interactionSource,
            indication = null
        )

    return KeyPadState(
        isPressed = isPressed,
        animatedColor = animatedColor,
        animatedTextColor = animatedTextColor,
        modifier = modifier
    )
}
