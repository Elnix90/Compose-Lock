package io.github.elnix90.lock.patttern

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
public data class PatternLockOptions(
    val showSensibility: Boolean,
    val dimension: Int,
    val sensitivity: Dp,
    val dotsSize: Dp,
    val dotAnimationInitialSize: Dp,
    val linesStroke: Dp,
    val dotsColor: Color,
    val linesColor: Color,
    val animationDurationMs: Int,
    val animationDelayMs: Long,
) {
    init {
        require(dimension >= 2) { "Dimension must be at least 2" }
    }

    public companion object {
        public val defaultPatternLockOptions: PatternLockOptions
            @Composable
            get() = PatternLockOptions(
                showSensibility = false,
                dimension = 3,
                sensitivity = 100.dp,
                dotsSize = 5.dp,
                dotAnimationInitialSize = 20.dp * 1.8f,
                linesStroke = 5.dp,
                dotsColor = MaterialTheme.colorScheme.primary,
                linesColor = MaterialTheme.colorScheme.secondary,
                animationDurationMs = 200,
                animationDelayMs = 100
            )
    }
}