package io.github.elnix90.lock.pin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.graphics.shapes.RoundedPolygon
import java.util.UUID

internal data class ShapeDecay(
    val id: UUID,
    val polygon: RoundedPolygon,
    val scaleAnimation: Animatable<Float, AnimationVector1D> = Animatable(2f),
    val shapeProgress: Animatable<Float, AnimationVector1D> = Animatable(0f)
) {

    suspend fun decay() {
        scaleAnimation.animateTo(
            targetValue = 0f,
            animationSpec = tween(100)
        )
    }

    companion object {
        internal fun RoundedPolygon.newDecay(): ShapeDecay = ShapeDecay(
            id = UUID.randomUUID(),
            polygon = this
        )
    }
}