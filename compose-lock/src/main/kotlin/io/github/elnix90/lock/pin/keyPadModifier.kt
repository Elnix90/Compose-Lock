package io.github.elnix90.lock.pin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.rectangle

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun Modifier.keyPadModifier(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()


    val circle = remember {
        RoundedPolygon.circle(10).normalized()
    }


    val square = remember {
        RoundedPolygon.rectangle(
            rounding = CornerRounding(0.2f, 1f)
        ).normalized()
    }

    val morph = Morph(circle, square)


    val progress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f
    )

    val shape = remember(morph, progress) {
        MorphPolygonShape(morph, progress)
    }

    return this
        .aspectRatio(1f)
        .clip(shape)
        .clickable(
            enabled = enabled,
            onClick = onClick,
            interactionSource = interactionSource
        )
        .background(MaterialTheme.colorScheme.surface.copy(if (enabled) 1f else 0.5f))
        .padding(15.dp)
}
