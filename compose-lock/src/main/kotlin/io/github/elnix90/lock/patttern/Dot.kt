package io.github.elnix90.lock.patttern

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.Offset

internal data class Dot (
    val id: Int,
    val offset: Offset,
    val size:Animatable<Float,AnimationVector1D>
)