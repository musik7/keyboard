package com.coding.keyboard.service

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.coding.keyboard.logic.KeyboardController
import com.coding.keyboard.logic.KeyboardState
import com.coding.keyboard.ui.layout.KeyboardScreen
import com.coding.keyboard.ui.theme.KeyboardTheme

/**
 * Service utama IME (Input Method Service) untuk Keyboard Koding.
 * Berperan sebagai jembatan antara Android OS dan Jetpack Compose UI.
 */
class CodingKeyboardService : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    // Komponen Lifecycle khusus untuk Jetpack Compose di dalam Service
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    // State dan Controller untuk Keyboard
    private val keyboardState = KeyboardState()
    private lateinit var keyboardController: KeyboardController

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        
        // Inisialisasi controller dengan null dulu, akan diupdate saat onStartInput
        keyboardController = KeyboardController(null, keyboardState)
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            // Setup properti Lifecycle untuk Compose agar bisa berjalan di luar Activity
            ViewTreeLifecycleOwner.set(this, this@CodingKeyboardService)
            ViewTreeViewModelStoreOwner.set(this, this@CodingKeyboardService)
            ViewTreeSavedStateRegistryOwner.set(this, this@CodingKeyboardService)

            setContent {
                KeyboardScreen(
                    controller = keyboardController,
                    state = keyboardState
                )
            }
        }
        
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        // Update inputConnection agar controller bisa ngetik ke aplikasi target
        keyboardController.updateInputConnection(currentInputConnection)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        // Reset state jika perlu saat keyboard ditutup
        if (!keyboardState.isCtrlActive.value && !keyboardState.isAltActive.value) {
            keyboardState.resetModifiers()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = store

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
}
