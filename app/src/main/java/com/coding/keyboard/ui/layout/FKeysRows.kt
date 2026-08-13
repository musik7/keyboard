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
import com.coding.keyboard.ui.theme.KeyboardTheme

@Composable
fun FKeysRows(
    controller: KeyboardController,
    state: KeyboardState
) {
    val row1 = (1..10).toList() // F1 to F10
    val row2 = listOf("F11", "F12", "Home", "End", "PgUp", "PgDn")

    // Row 1 (F1 - F10)
    Row(modifier = Modifier.fillMaxWidth()) {
        row1.forEach { num ->
            TextKey(
                label = "F$num", 
                modifier = Modifier.weight(1f),
                textStyle = KeyboardTheme.typography.label
            ) {
                controller.sendFunctionKey(num)
            }
        }
    }

    // Row 2 (F11, F12, Home, End, PgUp, PgDn)
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.5f))
        row2.forEach { action ->
            TextKey(
                label = action, 
                modifier = Modifier.weight(1.5f),
                textStyle = KeyboardTheme.typography.label
            ) {
                if (action.startsWith("F")) {
                    controller.sendFunctionKey(action.substring(1).toInt())
                } else {
                    controller.sendActionKey(action)
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

    // Row 3 (Meta, empty space, Backspace)
    Row(modifier = Modifier.fillMaxWidth()) {
        ModifierKey(
            label = "Meta",
            isActive = state.isMetaActive.value,
            modifier = Modifier.weight(1.5f)
        ) {
            state.toggleMeta()
        }

        Spacer(modifier = Modifier.weight(7f))

        IconKey(
            icon = Icons.AutoMirrored.Filled.Backspace,
            modifier = Modifier.weight(1.5f),
            isAction = true
        ) {
            controller.deleteBackward()
        }
    }
}
