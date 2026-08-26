package io.github.elnix90.lock

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import io.github.elnix90.lock.patttern.Dot
import io.github.elnix90.lock.patttern.Line
import io.github.elnix90.lock.patttern.PatternLockOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.milliseconds

private class PatternLockSystem(
    private val scope: CoroutineScope,
    private val patternLockOptions: PatternLockOptions,
    density: Density
) {
    private val _dots: MutableStateFlow<List<Dot>> = MutableStateFlow(emptyList())
    val dots = _dots.asStateFlow()

    private val _connectedLines: MutableStateFlow<List<Line>> = MutableStateFlow(emptyList())
    val connectedLines = _connectedLines.asStateFlow()

    private val _connectedDotsIds: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())
    val connectedDotsIds = _connectedDotsIds.asStateFlow()

    private val dimension = patternLockOptions.dimension
    private val sensitivity = with(density) { patternLockOptions.sensitivity.toPx() }
    private val dotsSize = with(density) { patternLockOptions.dotsSize.toPx() }


    private var hasInitialized = false
    private val mutex = Mutex()

    fun init(layoutCoordinates: LayoutCoordinates) {
        if (hasInitialized) return
        scope.launch {
            mutex.withLock {
                val width = layoutCoordinates.size.width

                val realDimension = dimension + 1
                val spaceBetweenDots = (width / realDimension).toFloat()

                repeat(dimension) { widthIndex ->
                    repeat(dimension) { heightIndex ->
                        val dotOffset = Offset(
                            x = spaceBetweenDots * (widthIndex + 1),
                            y = spaceBetweenDots * (heightIndex + 1)
                        )

                        _dots.value += Dot(
                            id = heightIndex * patternLockOptions.dimension + widthIndex,
                            offset = dotOffset,
                            size = Animatable(dotsSize)
                        )
                    }
                }
                hasInitialized = true
            }
        }
    }


    fun clear() {
        _connectedDotsIds.value = emptyList()
        _connectedLines.value = emptyList()

        _dots.value.forEach { dot ->
            scope.launch {
                dot.size.animateTo(
                    targetValue = dotsSize,
                    animationSpec = tween(patternLockOptions.animationDurationMs)
                )
            }
        }
    }

    fun onDrag(pos: Offset) {
        for (dot in _dots.value) {
            if (dot.id !in _connectedDotsIds.value) {
                if (
                    pos.x in (dot.offset.x - sensitivity)..(dot.offset.x + sensitivity) &&
                    pos.y in (dot.offset.y - sensitivity)..(dot.offset.y + sensitivity)
                ) {
                    if (_connectedDotsIds.value.isNotEmpty()) {
                        _connectedLines.value += Line(
                            start = _dots.value.first { it.id == _connectedDotsIds.value.last() }.offset,
                            end = dot.offset
                        )
                    }
                    _connectedDotsIds.value += dot.id

                    scope.launch {
                        dot.size.animateTo(
                            targetValue = (dotsSize * 1.8f),
                            animationSpec = tween(patternLockOptions.animationDurationMs)
                        )
                        delay(patternLockOptions.animationDelayMs.milliseconds)
                        dot.size.animateTo(dotsSize, tween(patternLockOptions.animationDurationMs))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun PatternLock(
    modifier: Modifier = Modifier,
    patternLockOptions: PatternLockOptions,
    onFinished: (String) -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val system = remember(patternLockOptions) {
        PatternLockSystem(
            scope = scope + SupervisorJob(),
            patternLockOptions = patternLockOptions,
            density = density
        )
    }

    val dotsList by system.dots.collectAsState()
    val connectedLines by system.connectedLines.collectAsState()
    val connectedDotsIds by system.connectedDotsIds.collectAsState()

    var currentOffset: Offset? by remember { mutableStateOf(null) }

    Canvas(
        modifier
            .aspectRatio(1f)
            .pointerInput(patternLockOptions) {
                detectDragGestures(
                    onDragStart = {},
                    onDragEnd = {
                        if (connectedDotsIds.isNotEmpty()) {
                            system.clear()
                            onFinished(connectedDotsIds.joinToString("") { it.toString() })
                        }
                    },
                    onDragCancel = {
                        system.clear()
                    },
                    onDrag = { change, _ ->
                        currentOffset = change.position
                        system.onDrag(change.position)
                    }
                )
            }
            .onGloballyPositioned(system::init)
    ) {
        val stroke = patternLockOptions.linesStroke.toPx()

        if (currentOffset != null && connectedDotsIds.isNotEmpty()) {
            drawLine(
                color = patternLockOptions.linesColor,
                start = dotsList.first { it.id == connectedDotsIds.last() }.offset,
                end = currentOffset!!,
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }

        for (dot in dotsList) {
            drawCircle(
                color = patternLockOptions.dotsColor,
                radius = dot.size.value,
                center = dot.offset
            )

            if (patternLockOptions.showSensibility) {
                val sensiPx = patternLockOptions.sensitivity.toPx() * 2
                val size = Size(sensiPx, sensiPx)

                drawRect(
                    color = patternLockOptions.dotsColor.copy(0.5f),
                    topLeft = dot.offset - Offset(size.width / 2, size.height / 2),
                    size = size
                )
            }
        }

        for (line in connectedLines) {
            drawLine(
                color = patternLockOptions.linesColor,
                start = line.start,
                end = line.end,
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }
    }
}
