package com.coding.keyboard.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardMode
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.ui.theme.KeyboardTheme

@Composable
fun KeyboardScreen(
    controller: KeyboardController,
    state: KeyboardState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeyboardTheme.colors.background)
            .navigationBarsPadding() // Mencegah tertutup oleh Gesture Bar / 3-Tombol Navigasi Bawaan Android
            .padding(bottom = 4.dp) // Extra padding for bottom bezel
    ) {
        // Baris 0: Toolbar Koding (Selalu muncul di semua mode)
        CodingToolbar(controller = controller, state = state)

        // Baris 1-3: Swap berdasarkan Mode Aktif
        when (state.keyboardMode.value) {
            KeyboardMode.QWERTY -> QwertyRows(controller = controller, state = state)
            KeyboardMode.SYMBOLS -> SymbolRows(controller = controller, state = state)
            KeyboardMode.F_KEYS -> FKeysRows(controller = controller, state = state)
        }
        
        // Baris 4: Bottom Row (Tombol switch mode ada di dalamnya)
        BottomRow(controller = controller, state = state)
    }
}
