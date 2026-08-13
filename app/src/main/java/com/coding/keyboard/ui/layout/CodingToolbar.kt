package com.coding.keyboard.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.keyboard.core.Constants
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.ui.components.ModifierKey
import com.coding.keyboard.ui.components.TextKey
import com.coding.keyboard.ui.theme.KeyboardTheme

@Composable
fun CodingToolbar(
    controller: KeyboardController,
    state: KeyboardState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(KeyboardTheme.dimens.toolbarHeight)
            .background(KeyboardTheme.colors.background)
    ) {
        // 1. Tombol Navigasi Kiri (Tab & Esc & Alt)
        TextKey(
            label = Constants.LABEL_TAB,
            modifier = Modifier.width(50.dp),
            isAction = true,
            textStyle = KeyboardTheme.typography.label
        ) {
            controller.commitText("\t")
        }
        
        TextKey(
            label = Constants.LABEL_ESC,
            modifier = Modifier.width(50.dp),
            isAction = true,
            textStyle = KeyboardTheme.typography.label
        ) {
            controller.sendActionKey("Esc")
        }

        ModifierKey(
            label = "Alt",
            isActive = state.isAltActive.value,
            modifier = Modifier.width(50.dp)
        ) {
            state.toggleAlt()
        }

        // 2. Daftar Simbol Koding (Horizontal Scrollable)
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState())
        ) {
            Constants.CODING_SYMBOLS.forEach { symbol ->
                TextKey(
                    label = symbol,
                    modifier = Modifier.width(45.dp),
                    isAction = false,
                    textStyle = KeyboardTheme.typography.toolbar
                ) {
                    controller.commitCodingSymbol(symbol)
                }
            }
        }
    }
}
