package com.coding.keyboard.logic

import android.view.KeyEvent
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection

class KeyboardController(
    private var inputConnection: InputConnection?,
    private val state: KeyboardState
) {
    fun updateInputConnection(ic: InputConnection?) {
        this.inputConnection = ic
    }

    fun commitText(text: String) {
        // 1. Intercept shortcut if Ctrl, Alt, or Meta is Active
        if ((state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) && text.length == 1) {
            handleShortcut(text.lowercase())
            state.resetModifiers() // Matikan modifier setelah eksekusi
            return
        }

        // 2. Normal Typing
        var finalText = text
        
        // Cek jika teks adalah huruf tunggal, aplikasikan logika Shift
        if (text.length == 1 && text.first().isLetter()) {
            if (state.shiftMode.value != ShiftMode.LOWERCASE) {
                finalText = text.uppercase()
            }
            // Auto reset shift jika mode-nya bukan CAPS_LOCK
            state.resetShiftIfNeeded()
        }

        inputConnection?.commitText(finalText, 1)
    }

    // --- Shortcut Logic (Ctrl/Alt + Key) ---
    private fun handleShortcut(key: String) {
        val ic = inputConnection ?: return
        
        // Mapping string ke KeyCode Android
        val keyCode = when (key) {
            "a" -> KeyEvent.KEYCODE_A
            "b" -> KeyEvent.KEYCODE_B
            "c" -> KeyEvent.KEYCODE_C
            "d" -> KeyEvent.KEYCODE_D
            "e" -> KeyEvent.KEYCODE_E
            "f" -> KeyEvent.KEYCODE_F
            "g" -> KeyEvent.KEYCODE_G
            "h" -> KeyEvent.KEYCODE_H
            "i" -> KeyEvent.KEYCODE_I
            "j" -> KeyEvent.KEYCODE_J
            "k" -> KeyEvent.KEYCODE_K
            "l" -> KeyEvent.KEYCODE_L
            "m" -> KeyEvent.KEYCODE_M
            "n" -> KeyEvent.KEYCODE_N
            "o" -> KeyEvent.KEYCODE_O
            "p" -> KeyEvent.KEYCODE_P
            "q" -> KeyEvent.KEYCODE_Q
            "r" -> KeyEvent.KEYCODE_R
            "s" -> KeyEvent.KEYCODE_S
            "t" -> KeyEvent.KEYCODE_T
            "u" -> KeyEvent.KEYCODE_U
            "v" -> KeyEvent.KEYCODE_V
            "w" -> KeyEvent.KEYCODE_W
            "x" -> KeyEvent.KEYCODE_X
            "y" -> KeyEvent.KEYCODE_Y
            "z" -> KeyEvent.KEYCODE_Z
            "1" -> KeyEvent.KEYCODE_1
            "2" -> KeyEvent.KEYCODE_2
            "3" -> KeyEvent.KEYCODE_3
            "4" -> KeyEvent.KEYCODE_4
            "5" -> KeyEvent.KEYCODE_5
            "6" -> KeyEvent.KEYCODE_6
            "7" -> KeyEvent.KEYCODE_7
            "8" -> KeyEvent.KEYCODE_8
            "9" -> KeyEvent.KEYCODE_9
            "0" -> KeyEvent.KEYCODE_0
            else -> null
        }

        if (keyCode != null) {
            // Strategi 1: Kirim Hardware KeyEvent murni (Untuk Code Editor & Termux)
            sendModifierKeyEvent(keyCode)

            // Strategi 2: Fallback Android Context Menu (Jika hanya Ctrl yang aktif dan huruf spesifik)
            if (state.isCtrlActive.value && !state.isAltActive.value) {
                when (key) {
                    "a" -> ic.performContextMenuAction(android.R.id.selectAll)
                    "c" -> ic.performContextMenuAction(android.R.id.copy)
                    "v" -> ic.performContextMenuAction(android.R.id.paste)
                    "x" -> ic.performContextMenuAction(android.R.id.cut)
                    "z" -> ic.performContextMenuAction(android.R.id.undo)
                }
            }
        }
    }

    fun sendFunctionKey(fNumber: Int) {
        val keyCode = when (fNumber) {
            1 -> KeyEvent.KEYCODE_F1
            2 -> KeyEvent.KEYCODE_F2
            3 -> KeyEvent.KEYCODE_F3
            4 -> KeyEvent.KEYCODE_F4
            5 -> KeyEvent.KEYCODE_F5
            6 -> KeyEvent.KEYCODE_F6
            7 -> KeyEvent.KEYCODE_F7
            8 -> KeyEvent.KEYCODE_F8
            9 -> KeyEvent.KEYCODE_F9
            10 -> KeyEvent.KEYCODE_F10
            11 -> KeyEvent.KEYCODE_F11
            12 -> KeyEvent.KEYCODE_F12
            else -> return
        }
        sendModifierKeyEvent(keyCode)
        if (state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) state.resetModifiers()
    }

    fun sendActionKey(action: String) {
        val keyCode = when (action) {
            "Home" -> KeyEvent.KEYCODE_MOVE_HOME
            "End" -> KeyEvent.KEYCODE_MOVE_END
            "PgUp" -> KeyEvent.KEYCODE_PAGE_UP
            "PgDn" -> KeyEvent.KEYCODE_PAGE_DOWN
            "Esc" -> KeyEvent.KEYCODE_ESCAPE
            else -> return
        }
        sendModifierKeyEvent(keyCode)
        if (state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) state.resetModifiers()
    }

    fun moveCursorUp() {
        sendModifierKeyEvent(KeyEvent.KEYCODE_DPAD_UP)
        resetModifiersIfActive()
    }

    fun moveCursorDown() {
        sendModifierKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN)
        resetModifiersIfActive()
    }

    private fun resetModifiersIfActive() {
        if (state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) {
            state.resetModifiers()
        }
    }

    private fun sendHardwareKeyEvent(keyCode: Int, metaState: Int = 0) {
        val ic = inputConnection ?: return
        
        val downEvent = KeyEvent(
            0, 0, KeyEvent.ACTION_DOWN, keyCode, 0, metaState
        )
        val upEvent = KeyEvent(
            0, 0, KeyEvent.ACTION_UP, keyCode, 0, metaState
        )

        ic.sendKeyEvent(downEvent)
        ic.sendKeyEvent(upEvent)
    }

    private fun sendModifierKeyEvent(keyCode: Int) {
        var metaState = 0
        if (state.isCtrlActive.value) metaState = metaState or KeyEvent.META_CTRL_ON or KeyEvent.META_CTRL_LEFT_ON
        if (state.isAltActive.value) metaState = metaState or KeyEvent.META_ALT_ON or KeyEvent.META_ALT_LEFT_ON
        if (state.isMetaActive.value) metaState = metaState or KeyEvent.META_META_ON or KeyEvent.META_META_LEFT_ON
        sendHardwareKeyEvent(keyCode, metaState)
    }

    fun deleteBackward() {
        inputConnection?.deleteSurroundingText(1, 0)
    }

    // Fungsi khusus untuk Toolbar: Auto-pair brackets
    fun commitCodingSymbol(symbol: String) {
        val pair = when (symbol) {
            "{" -> "{}"
            "[" -> "[]"
            "(" -> "()"
            "\"" -> "\"\""
            "'" -> "''"
            "`" -> "``"
            else -> symbol
        }

        inputConnection?.commitText(pair, 1)
        
        // Jika simbol adalah pasangan (length > 1), pindahkan kursor 1 langkah ke kiri
        if (pair.length == 2) {
            moveCursor(-1)
        }
    }

    fun moveCursor(offsetChars: Int) {
        // Jika ada modifier (Ctrl/Alt/Meta) yang aktif, gunakan Hardware KeyEvent (D-Pad Left/Right)
        // Ini berguna untuk fitur seperti "Jump Word" (Ctrl+Left) atau "Select Word" (Shift+Ctrl+Left)
        if (state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) {
            val keyCode = if (offsetChars > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
            sendModifierKeyEvent(keyCode)
            resetModifiersIfActive()
            return
        }

        // Jika tidak ada modifier, gunakan smooth selection (Pindah per 1 karakter)
        val ic = inputConnection ?: return
        val extractedText = ic.getExtractedText(ExtractedTextRequest(), 0) ?: return
        
        val currentPosition = extractedText.selectionStart
        val newPosition = (currentPosition + offsetChars).coerceIn(0, extractedText.text.length)
        
        ic.setSelection(newPosition, newPosition)
    }
}
