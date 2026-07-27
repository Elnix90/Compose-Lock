package io.github.elnix90.lock

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun ComposeLock(
    modifier: Modifier = Modifier,
    dimension: Int,
    sensitivity: Float,
    dotsColor: Color,
    dotsSize: Float,
    linesColor: Color,
    linesStroke: Float,
    animationDuration: Int = 200,
    animationDelay: Long = 100,
    callback: ComposeLockCallback
) {
    require(dimension >= 2) { "Dimension must be >= 2" }

    val scope = rememberCoroutineScope()

    val dotsList = remember(dimension) { mutableListOf<Dot>() }
    val connectedLines = remember(dimension) { mutableListOf<Line>() }
    val connectedDots = remember(dimension) { mutableListOf<Dot>() }
    var currentOffset: Offset? by remember { mutableStateOf(null) }

    Canvas(
        modifier
            .aspectRatio(1f)
            .pointerInput(dimension) {
                detectDragGestures(
                    onDragStart = {},
                    onDragEnd = {
                        callback.onResult(connectedDots)
                        connectedLines.clear()
                        connectedDots.clear()
                    },
                    onDragCancel = {
                        connectedLines.clear()
                        connectedDots.clear()
                    },
                    onDrag = { change, _ ->
                        val pos = change.position

                        currentOffset = pos

                        for (dot in dotsList) {
                            if (dot.id !in connectedDots.map { it.id }) {
                                if (
                                    pos.x in (dot.offset.x - sensitivity)..(dot.offset.x + sensitivity) &&
                                    pos.y in (dot.offset.y - sensitivity)..(dot.offset.y + sensitivity)
                                ) {
                                    if (connectedDots.isNotEmpty()) {
                                        connectedLines.add(
                                            Line(
                                                start = connectedDots.last().offset,
                                                end = dot.offset
                                            )
                                        )
                                    }
                                    callback.onDotConnected(dot)
                                    connectedDots.add(dot)
                                    scope.launch {
                                        dot.size.animateTo(
                                            (dotsSize * 1.8).toFloat(),
                                            tween(animationDuration)
                                        )
                                        delay(animationDelay.milliseconds)
                                        dot.size.animateTo(dotsSize, tween(animationDuration))
                                    }
                                }
                            }
                        }
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val size = layoutCoordinates.size
                val realDimension = dimension + 1

                val spaceBetweenWidthDots = (size.width / realDimension).toFloat()
                val spaceBetweenHeightDots = (size.height / realDimension).toFloat()

                val dotsOnWidth = arrayOfNulls<Int>(realDimension * realDimension)
                val dotsOnHeight = arrayOfNulls<Int>(realDimension * realDimension)

                dotsOnWidth.forEachIndexed { widthIndex, _ ->
                    val readWidthIndex = widthIndex + 1
                    dotsOnHeight.forEachIndexed { heightIndex, _ ->
                        val readHeightIndex = heightIndex + 1
                        if (readWidthIndex < realDimension && readHeightIndex < realDimension) {
                            if (dotsList.count() < dimension * dimension) {

                                val dotOffset = Offset(
                                    x = spaceBetweenWidthDots * readWidthIndex,
                                    y = spaceBetweenHeightDots * readHeightIndex
                                )

                                dotsList.add(
                                    Dot(
                                        id = heightIndex * dimension + widthIndex,
                                        offset = dotOffset,
                                        size = Animatable(dotsSize)
                                    )
                                )
                            }
                        }
                    }
                }
            }
    ) {
        if (currentOffset != null && connectedDots.isNotEmpty()) {
            drawLine(
                color = linesColor,
                start = connectedDots.last().offset,
                end = currentOffset!!,
                strokeWidth = linesStroke,
                cap = StrokeCap.Round
            )
        }

        for (dot in dotsList) {
            drawCircle(
                color = dotsColor,
                radius = dot.size.value,
                center = dot.offset
            )
        }

        for (line in connectedLines) {
            drawLine(
                color = linesColor,
                start = line.start,
                end = line.end,
                strokeWidth = linesStroke,
                cap = StrokeCap.Round
            )
        }
    }
}
