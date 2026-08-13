package com.coding.keyboard.logic

import android.os.SystemClock
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
        val ic = inputConnection ?: return
        try {
            if ((state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) && text.length == 1) {
                handleShortcut(text.lowercase())
                state.resetModifiers()
                return
            }

            var finalText = text

            if (text.length == 1 && text.first().isLetter()) {
                if (state.shiftMode.value != ShiftMode.LOWERCASE) {
                    finalText = text.uppercase()
                }
                state.resetShiftIfNeeded()
            }

            ic.commitText(finalText, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleShortcut(key: String) {
        val ic = inputConnection ?: return

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
            sendModifierKeyEvent(keyCode)
            if (state.isCtrlActive.value && !state.isAltActive.value) {
                try {
                    when (key) {
                        "a" -> ic.performContextMenuAction(android.R.id.selectAll)
                        "c" -> ic.performContextMenuAction(android.R.id.copy)
                        "v" -> ic.performContextMenuAction(android.R.id.paste)
                        "x" -> ic.performContextMenuAction(android.R.id.cut)
                        "z" -> ic.performContextMenuAction(android.R.id.undo)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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
        try {
            val now = SystemClock.uptimeMillis()
            val downEvent = KeyEvent(
                now, now, KeyEvent.ACTION_DOWN, keyCode, 0, metaState
            )
            val upEvent = KeyEvent(
                now, now, KeyEvent.ACTION_UP, keyCode, 0, metaState
            )
            ic.sendKeyEvent(downEvent)
            ic.sendKeyEvent(upEvent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendModifierKeyEvent(keyCode: Int) {
        var metaState = 0
        if (state.isCtrlActive.value) metaState = metaState or KeyEvent.META_CTRL_ON or KeyEvent.META_CTRL_LEFT_ON
        if (state.isAltActive.value) metaState = metaState or KeyEvent.META_ALT_ON or KeyEvent.META_ALT_LEFT_ON
        if (state.isMetaActive.value) metaState = metaState or KeyEvent.META_META_ON or KeyEvent.META_META_LEFT_ON
        sendHardwareKeyEvent(keyCode, metaState)
    }

    fun deleteBackward() {
        val ic = inputConnection ?: return
        try {
            val selectedText = ic.getSelectedText(0)
            if (!selectedText.isNullOrEmpty()) {
                ic.commitText("", 1)
            } else {
                ic.deleteSurroundingText(1, 0)
            }
        } catch (e: Exception) {
            sendHardwareKeyEvent(KeyEvent.KEYCODE_DEL)
        }
    }

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
        val ic = inputConnection ?: return
        try {
            ic.commitText(pair, 1)
            if (pair.length == 2) {
                moveCursor(-1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun moveCursor(offsetChars: Int) {
        if (state.isCtrlActive.value || state.isAltActive.value || state.isMetaActive.value) {
            val keyCode = if (offsetChars > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
            sendModifierKeyEvent(keyCode)
            resetModifiersIfActive()
            return
        }

        val ic = inputConnection ?: return
        try {
            val extractedText = ic.getExtractedText(ExtractedTextRequest(), 0)
            if (extractedText != null && extractedText.text != null) {
                val text = extractedText.text
                val currentPosition = if (extractedText.selectionStart >= 0) extractedText.selectionStart else 0
                val newPosition = (currentPosition + offsetChars).coerceIn(0, text.length)
                ic.setSelection(newPosition, newPosition)
            } else {
                val keyCode = if (offsetChars > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
                sendHardwareKeyEvent(keyCode)
            }
        } catch (e: Exception) {
            val keyCode = if (offsetChars > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
            sendHardwareKeyEvent(keyCode)
        }
    }
}
