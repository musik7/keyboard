package com.coding.keyboard.logic

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

enum class ShiftMode {
    LOWERCASE, SHIFTED, CAPS_LOCK
}

enum class KeyboardMode {
    QWERTY, SYMBOLS, F_KEYS
}

class KeyboardState {
    private val _keyboardMode = mutableStateOf(KeyboardMode.QWERTY)
    val keyboardMode: State<KeyboardMode> = _keyboardMode

    private val _isCtrlActive = mutableStateOf(false)
    val isCtrlActive: State<Boolean> = _isCtrlActive

    private val _isAltActive = mutableStateOf(false)
    val isAltActive: State<Boolean> = _isAltActive

    private val _isMetaActive = mutableStateOf(false)
    val isMetaActive: State<Boolean> = _isMetaActive

    private val _shiftMode = mutableStateOf(ShiftMode.LOWERCASE)
    val shiftMode: State<ShiftMode> = _shiftMode

    fun switchMode(mode: KeyboardMode) {
        _keyboardMode.value = mode
    }

    fun toggleCtrl() {
        _isCtrlActive.value = !_isCtrlActive.value
    }

    fun toggleAlt() {
        _isAltActive.value = !_isAltActive.value
    }

    fun toggleMeta() {
        _isMetaActive.value = !_isMetaActive.value
    }

    fun toggleShift() {
        _shiftMode.value = when (_shiftMode.value) {
            ShiftMode.LOWERCASE -> ShiftMode.SHIFTED
            ShiftMode.SHIFTED -> ShiftMode.CAPS_LOCK
            ShiftMode.CAPS_LOCK -> ShiftMode.LOWERCASE
        }
    }

    // Called after a character is typed. If it's single shift, return to lowercase.
    fun resetShiftIfNeeded() {
        if (_shiftMode.value == ShiftMode.SHIFTED) {
            _shiftMode.value = ShiftMode.LOWERCASE
        }
    }
    
    fun resetModifiers() {
        _isCtrlActive.value = false
        _isAltActive.value = false
        _isMetaActive.value = false
        _shiftMode.value = ShiftMode.LOWERCASE
    }
}
