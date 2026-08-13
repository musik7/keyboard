package com.coding.keyboard.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.coding.keyboard.core.Constants
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardMode
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.ui.components.IconKey
import com.coding.keyboard.ui.components.ModifierKey
import com.coding.keyboard.ui.components.SpacebarKey
import com.coding.keyboard.ui.components.TextKey
import com.coding.keyboard.ui.theme.KeyboardTheme

@Composable
fun BottomRow(
    controller: KeyboardController,
    state: KeyboardState
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        
        // Mode Switcher Button
        val modeSwitcherLabel = when(state.keyboardMode.value) {
            KeyboardMode.QWERTY -> Constants.LABEL_SYM
            KeyboardMode.SYMBOLS -> "F1-12"
            KeyboardMode.F_KEYS -> "ABC"
        }

        TextKey(
            label = modeSwitcherLabel,
            modifier = Modifier.weight(1.5f),
            isAction = true,
            textStyle = KeyboardTheme.typography.label
        ) {
            when(state.keyboardMode.value) {
                KeyboardMode.QWERTY -> state.switchMode(KeyboardMode.SYMBOLS)
                KeyboardMode.SYMBOLS -> state.switchMode(KeyboardMode.F_KEYS)
                KeyboardMode.F_KEYS -> state.switchMode(KeyboardMode.QWERTY)
            }
        }

        ModifierKey(
            label = Constants.LABEL_CTRL,
            isActive = state.isCtrlActive.value,
            modifier = Modifier.weight(1.5f)
        ) {
            state.toggleCtrl()
        }

        TextKey(
            label = Constants.LABEL_COMMA, 
            modifier = Modifier.weight(1f)
        ) {
            controller.commitText(Constants.LABEL_COMMA)
        }

        SpacebarKey(
            modifier = Modifier.weight(4f),
            onTap = { controller.commitText(" ") },
            onMoveCursorLeftRight = { offset -> controller.moveCursor(offset) },
            onMoveCursorUpDown = { offset -> 
                if (offset < 0) controller.moveCursorUp() 
                else controller.moveCursorDown() 
            }
        )

        TextKey(
            label = Constants.LABEL_DOT, 
            modifier = Modifier.weight(1f)
        ) {
            controller.commitText(Constants.LABEL_DOT)
        }

        IconKey(
            icon = Icons.AutoMirrored.Filled.KeyboardReturn,
            modifier = Modifier.weight(1.5f),
            isAction = true
        ) {
            controller.commitText("\n")
        }
    }
}
