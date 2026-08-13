package com.coding.keyboard.core

object Constants {
    // Label Baris Bawah
    const val LABEL_SYM = "?123"
    const val LABEL_CTRL = "Ctrl"
    const val LABEL_COMMA = ","
    const val LABEL_SPACE = "Space"
    const val LABEL_DOT = "."
    const val LABEL_ENTER = "Enter"

    // Label Toolbar Koding (Navigasi)
    const val LABEL_TAB = "Tab"
    const val LABEL_ESC = "Esc"

    // Daftar Simbol yang sering dipakai koding untuk di-render di Scrollable Toolbar
    val CODING_SYMBOLS = listOf(
        "{", "}", "[", "]", "(", ")",
        "=", ";", "\"", "'", "`",
        "<", ">", "/", "\\", "|", "&", "!", "$", "*", "-", "+"
    )

    // Konfigurasi
    const val SWIPE_CURSOR_THRESHOLD_PX = 40f // Sensitivitas geser kursor di spacebar
}
