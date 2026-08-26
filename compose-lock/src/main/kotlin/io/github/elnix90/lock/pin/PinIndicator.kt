package io.github.elnix90.lock.pin

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.graphics.shapes.Morph
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.elnix90.lock.pin.ShapeDecay.Companion.newDecay
import io.github.elnix90.lock.pin.configuration.PinIndicatorOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.time.Duration.Companion.milliseconds


private class PinIndicatorSystem(
    private val scope: CoroutineScope,
    private val pinIndicatorOptions: PinIndicatorOptions
) {
    var oldPin = ""

    private val _shapes: MutableStateFlow<List<ShapeDecay>> = MutableStateFlow(emptyList())
    val shapes = _shapes.asStateFlow()


    private var clearJob: Job? = null
    private var removeLastJob: Job? = null
    private var addNewJobs: MutableList<Job> = mutableListOf()

    fun updatePin(newPin: String) {
        clearJob?.cancel()
        removeLastJob?.cancel()

        if (newPin.isEmpty()) {
            clearJob = scope.launch(Dispatchers.Default) {
                try {
                    var delayMillis = 20
                    val job = launch {
                        _shapes.value.reversed().forEach { shapeDecay ->
                            // Trigger the decay of this shape async
                            launch { shapeDecay.decay() }
                            if (delayMillis >= 1) {
                                delay(delayMillis.milliseconds)
                                delayMillis -= 1
                            }
                        }
                    }
                    job.join()
                    _shapes.value = emptyList()

                } catch (_: CancellationException) {
                    // Clear the shapes list on cancellation, to provide instant UI updates,
                    // when user has pressed delete all, and instantly pressed again another button
                    _shapes.value = emptyList()
                }
            }
        }

        val isNewCharacter = oldPin.length < newPin.length
        if (isNewCharacter) {

            // This needs to be a loop, as sometimes this function gets called with more than one new character
            // If the user press 2 or more buttons at the time, the new pin could have more than 1 new char
            // The iterator (each new chars) are not used though because here we only care about the new shapes, that are random
            repeat(newPin.length - oldPin.length) {
                val newShapeDecay = pinIndicatorOptions.pinShapes.random().newDecay()
                _shapes.value += newShapeDecay

                addNewJobs += scope.launch {
                    launch {
                        newShapeDecay.scaleAnimation.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }

                    launch {
                        newShapeDecay.shapeProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(500)
                        )
                    }
                }
            }
        } else if (_shapes.value.isNotEmpty()) {
            removeLastJob = scope.launch {
                val lastShape = _shapes.value.last()
                try {
                    lastShape.decay()
                    _shapes.value = _shapes.value.filterNot { it.id == lastShape.id }
                } catch (_: CancellationException) {
                    _shapes.value = _shapes.value.filterNot { it.id == lastShape.id }
                }
            }
        }

        oldPin = newPin
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
public fun PinIndicator(
    pin: String,
    pinIndicatorOptions: PinIndicatorOptions = PinIndicatorOptions.defaultPinIndicatorOptions
) {
    val scope = rememberCoroutineScope()

    val shapesSpacing = pinIndicatorOptions.shapesSpacing
    val shapesSize = pinIndicatorOptions.shapesSize

    val pinIndicatorSystem = retain { PinIndicatorSystem(scope + SupervisorJob(), pinIndicatorOptions) }
    val shapes by pinIndicatorSystem.shapes.collectAsStateWithLifecycle()
    LaunchedEffect(pin) {
        pinIndicatorSystem.updatePin(pin)
    }

    RightAlignedRow(
        spacing = shapesSpacing,
        modifier = Modifier.height(shapesSize)
    ) {
        shapes.forEach { shapeDecay ->
            val morph = remember { Morph(shapeDecay.polygon, MaterialShapes.Circle) }
            val shape = remember(morph, shapeDecay.shapeProgress.value) {
                MorphPolygonShape(morph, shapeDecay.shapeProgress.value)
            }

            if (shapeDecay.scaleAnimation.value > 0f) {
                Box(
                    modifier = Modifier
                        .size(shapesSize)
                        .scale(shapeDecay.scaleAnimation.value)
                        .background(
                            color = MaterialTheme.colorScheme.outline,
                            shape = shape
                        )
                )
            }
        }
    }
}


/**
 * Custom Layout that reproduces the behavior of a row, but spaces the items from the right
 *
 * This allows me to have a file-grained control over what's drawn to the screen, and therefore better display them!!!
 *
 * @param spacing Each item are spaced by this much space
 */
@Composable
private fun RightAlignedRow(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        // Calculate total width (including spacing)
        val totalWidth = placeables.sumOf { it.width } + spacing.roundToPx() * (placeables.size - 1)

        // Center the layout, but limit its size to the maxWidth, this way the items are at most placed on the right
        var x = ((constraints.maxWidth / 2) + totalWidth / 2).coerceAtMost(constraints.maxWidth)
        var lastRepeat = false

        layout(constraints.maxWidth, placeables.maxOfOrNull { it.height } ?: 0) {
            val invertedList = placeables.asReversed()

            for (placeable in invertedList) {

                // Prevent drawing too much of the elements, by overflowing on the left by only 1
                if (lastRepeat) return@layout
                if (x - placeable.width < constraints.minWidth) lastRepeat = true

                x -= placeable.width
                placeable.place(x, 0)
                x -= spacing.roundToPx()
            }
        }
    }
}