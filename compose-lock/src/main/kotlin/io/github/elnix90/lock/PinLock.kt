package io.github.elnix90.lock

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.elnix90.lock.pin.KeypadButton

private val rows  = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9")
)

@Composable
public fun PinLock(
    modifier: Modifier,
    validateEnabled: Boolean,
    backSpaceOrClose: Boolean,
    spacing: Dp = 20.dp,
    onDigit: (String) -> Unit,
    onValidate: () -> Unit,
    onClear: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEach { digit ->
                    KeypadButton(
                        text = digit,
                        modifier = Modifier.weight(1f),
                        onClick = onDigit
                    )
                }
            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            AnimatedContent(
                targetState = backSpaceOrClose,
                modifier = Modifier.weight(1f)
            ) {

                KeypadButton(
                    icon = if (it) R.drawable.backspace else R.drawable.close,
                    tint = MaterialTheme.colorScheme.error,
                    onClick = onClear
                )
            }

            KeypadButton(
                text = "0",
                modifier = Modifier.weight(1f),
                onClick = onDigit
            )

            KeypadButton(
                icon = R.drawable.check,
                tint = Color.Green,
                modifier = Modifier.weight(1f),
                onClick = onValidate,
                enabled = validateEnabled
            )
        }
    }
}
