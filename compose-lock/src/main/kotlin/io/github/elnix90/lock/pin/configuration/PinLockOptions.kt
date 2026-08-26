package io.github.elnix90.lock.pin.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
public data class PinLockOptions(
    val pinIndicatorOptions: PinIndicatorOptions,
    val keyPadSettings: KeyPadSettings,
    val keysSpacingVertical: Dp,
    val keysSpacingHorizontal: Dp,
    val indicatorAndKeysSpacing: Dp,
    val maxChars: Int
) {
    public companion object {
        public val defaultPinLockOptions: PinLockOptions
            @Composable
            get() = PinLockOptions(
                pinIndicatorOptions = PinIndicatorOptions.defaultPinIndicatorOptions,
                keyPadSettings = KeyPadSettings.defaultKeyPadSettings,
                keysSpacingVertical = 15.dp,
                keysSpacingHorizontal = 25.dp,
                indicatorAndKeysSpacing = 60.dp,
                maxChars = Int.MAX_VALUE
            )
    }
}
