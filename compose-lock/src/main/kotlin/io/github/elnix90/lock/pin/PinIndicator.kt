package io.github.elnix90.lock.pin

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty
import kotlin.collections.lastIndex

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
public fun PinIndicator(
    shapes: List<RoundedPolygon>
) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    LaunchedEffect(shapes.size) {
        if (shapes.isNotEmpty()){
            scope.launch { lazyState.scrollToItem(shapes.lastIndex) }
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = lazyState
    ) {
        items(shapes) { shape ->
            var scaleTarget by remember { mutableFloatStateOf(0f) }

            // Trigger visibility only once when shape is added
            // I find this genius
            LaunchedEffect(shape) {
                scaleTarget = 1f
            }

            val scale by animateFloatAsState(
                targetValue = scaleTarget,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = shape.toShape()
                    )
            )
        }
    }
}

