package com.coding.keyboard.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object KeyboardTheme {
    object colors {
        val background = Color(0xFF141414)      // Gboard dark background
        val keyBackground = Color(0xFF2B2C2F)   // Tombol huruf/angka normal
        val keyBackgroundAction = Color(0xFF1F2023) // Tombol aksi (Shift, Backspace, ?123, Spacebar)
        val keyText = Color(0xFFE2E2E2)
        val keyTextAction = Color(0xFFAAAAAA)
        val accent = Color(0xFF8AB4F8)          // Warna aksen (Gboard Blue) saat Ctrl aktif
        val accentText = Color(0xFF141414)      // Warna teks di atas tombol aksen
    }

    object dimens {
        val keyHeight = 54.dp
        val toolbarHeight = 44.dp
        val keyCornerRadius = 6.dp
        val keyHorizontalPadding = 3.dp // Jarak antar tombol (horizontal)
        val keyVerticalPadding = 4.dp   // Jarak antar tombol (vertical)
    }

    object typography {
        val mainChar = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal
        )
        val label = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        val toolbar = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
