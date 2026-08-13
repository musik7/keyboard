package com.coding.keyboard.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.ui.components.IconKey
import com.coding.keyboard.ui.components.ModifierKey
import com.coding.keyboard.ui.components.TextKey

@Composable
fun SymbolRows(
    controller: KeyboardController,
    state: KeyboardState
) {
    val row1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val row2 = listOf("@", "#", "%", "^", "~", "_", "+", "-", "*")
    val row3 = listOf(":", "?", "\\", "/", "£", "¢", "€")

    // Row 1 (Numbers)
    Row(modifier = Modifier.fillMaxWidth()) {
        row1.forEach { char ->
            TextKey(label = char, modifier = Modifier.weight(1f)) {
                controller.commitText(char)
            }
        }
    }

    // Row 2 (Symbols)
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.5f))
        row2.forEach { char ->
            TextKey(label = char, modifier = Modifier.weight(1f)) {
                controller.commitText(char)
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

    // Row 3 (Meta, extra symbols, Backspace)
    Row(modifier = Modifier.fillMaxWidth()) {
        // Tombol Meta (Menggantikan Alt di layout ini)
        ModifierKey(
            label = "Meta",
            isActive = state.isMetaActive.value,
            modifier = Modifier.weight(1.5f)
        ) {
            state.toggleMeta()
        }

        row3.forEach { char ->
            TextKey(label = char, modifier = Modifier.weight(1f)) {
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
