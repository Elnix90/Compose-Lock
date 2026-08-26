package io.github.elnix90.lock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.elnix90.lock.pin.KeypadButton
import io.github.elnix90.lock.pin.PinIndicator
import io.github.elnix90.lock.pin.configuration.PinLockOptions

private val rows = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9")
)

@Composable
public fun PinLock(
    modifier: Modifier,
    pinLockOptions: PinLockOptions = PinLockOptions.defaultPinLockOptions,
    onValidate: (pin: String) -> Unit
) {
    var pinValue by retain { mutableStateOf("") }
    fun onDigit(digit: String) {
        if (pinValue.length >= pinLockOptions.maxChars) return
        pinValue += digit
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
    ) {
        PinIndicator(pinValue, pinLockOptions.pinIndicatorOptions)
        Spacer(Modifier.height(pinLockOptions.indicatorAndKeysSpacing))
        Column(verticalArrangement = Arrangement.spacedBy(pinLockOptions.keysSpacingVertical)) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(pinLockOptions.keysSpacingHorizontal, Alignment.CenterHorizontally)
                ) {
                    row.forEach { digit ->
                        KeypadButton(
                            text = digit,
                            keyPadSettings = pinLockOptions.keyPadSettings,
                            onClick = ::onDigit
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pinLockOptions.keysSpacingHorizontal, Alignment.CenterHorizontally)
            ) {
                KeypadButton(
                    icon = R.drawable.backspace,
                    keyPadSettings = pinLockOptions.keyPadSettings,
                    onClick = { pinValue = pinValue.dropLast(1) },
                    onLongClick = { pinValue = "" }
                )

                KeypadButton(
                    text = "0",
                    keyPadSettings = pinLockOptions.keyPadSettings,
                    onClick = ::onDigit
                )

                KeypadButton(
                    icon = R.drawable.start,
                    keyPadSettings = pinLockOptions.keyPadSettings,
                    onLongClick = null,
                    onClick = {
                        onValidate(pinValue)
                        pinValue = ""
                    }
                )
            }
        }
    }
}
