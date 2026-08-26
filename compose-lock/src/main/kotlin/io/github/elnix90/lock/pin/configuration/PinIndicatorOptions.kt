package io.github.elnix90.lock.pin.configuration

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import io.github.elnix90.lock.pin.pinMaterialShapes

@Immutable
public data class PinIndicatorOptions(
    val pinShapes: Set<RoundedPolygon>,
    val shapesSize: Dp,
    val shapesSpacing: Dp
) {
    public companion object {
        public val defaultPinIndicatorOptions: PinIndicatorOptions
            get() = PinIndicatorOptions(
                pinShapes = pinMaterialShapes,
                shapesSize = 15.dp,
                shapesSpacing = 15.dp
            )
    }
}
