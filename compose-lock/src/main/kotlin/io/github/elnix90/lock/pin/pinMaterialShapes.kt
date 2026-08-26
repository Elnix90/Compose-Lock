package io.github.elnix90.lock.pin

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.graphics.shapes.RoundedPolygon

/**
 * A selection of shapes for the pin that makes them pretty. not all of the [MaterialShapes] fit
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
public val pinMaterialShapes: Set<RoundedPolygon> = setOf(
    MaterialShapes.Slanted,
    MaterialShapes.Arrow,
    MaterialShapes.Oval,
    MaterialShapes.Pill,
    MaterialShapes.Triangle,
    MaterialShapes.Diamond,
    MaterialShapes.Pentagon,
    MaterialShapes.Gem,
    MaterialShapes.Cookie4Sided,
    MaterialShapes.Cookie7Sided,
    MaterialShapes.Cookie9Sided,
    MaterialShapes.Cookie12Sided,
    MaterialShapes.SoftBurst,
    MaterialShapes.Cookie12Sided,
    MaterialShapes.Flower,
    MaterialShapes.Clover4Leaf,
    MaterialShapes.Sunny,
    MaterialShapes.VerySunny,
    MaterialShapes.Clover4Leaf
)