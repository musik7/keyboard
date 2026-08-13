package com.coding.keyboard.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardCapslock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.logic.ShiftMode
import com.coding.keyboard.ui.components.IconKey
import com.coding.keyboard.ui.components.TextKey

@Composable
fun QwertyRows(
    controller: KeyboardController,
    state: KeyboardState
) {
    val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    val row3 = listOf("z", "x", "c", "v", "b", "n", "m")

    val isUppercase = state.shiftMode.value != ShiftMode.LOWERCASE

    // Row 1 (q-p)
    Row(modifier = Modifier.fillMaxWidth()) {
        row1.forEach { char ->
            TextKey(
                label = if (isUppercase) char.uppercase() else char,
                modifier = Modifier.weight(1f)
            ) {
                controller.commitText(char)
            }
        }
    }

    // Row 2 (a-l) dengan Spacer di ujung agar agak masuk ke tengah
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.5f))
        row2.forEach { char ->
            TextKey(
                label = if (isUppercase) char.uppercase() else char,
                modifier = Modifier.weight(1f)
            ) {
                controller.commitText(char)
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

    // Row 3 (Shift, z-m, Backspace)
    Row(modifier = Modifier.fillMaxWidth()) {
        // Tombol Shift
        val shiftIcon = if (state.shiftMode.value == ShiftMode.CAPS_LOCK) {
            Icons.Default.KeyboardCapslock
        } else {
            Icons.Default.KeyboardArrowUp
        }
        val isShiftActive = state.shiftMode.value != ShiftMode.LOWERCASE
        
        IconKey(
            icon = shiftIcon,
            modifier = Modifier.weight(1.5f),
            isAction = true,
            isActive = isShiftActive // Kita manfaatkan parameter isActive untuk mewarnai tombol
        ) {
            state.toggleShift()
        }

        row3.forEach { char ->
            TextKey(
                label = if (isUppercase) char.uppercase() else char,
                modifier = Modifier.weight(1f)
            ) {
                controller.commitText(char)
            }
        }

        // Tombol Backspace
        IconKey(
            icon = Icons.AutoMirrored.Filled.Backspace,
            modifier = Modifier.weight(1.5f),
            isAction = true
        ) {
            controller.deleteBackward()
        }
    }
}
