package com.coding.keyboard.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.coding.keyboard.service.CodingKeyboardService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1B1B1F)
                ) {
                    SetupScreen()
                }
            }
        }
    }
}

@Composable
fun SetupScreen() {
    val context = LocalContext.current
    var isKeyboardEnabled by remember { mutableStateOf(checkIsKeyboardEnabled(context)) }
    var isKeyboardSelected by remember { mutableStateOf(checkIsKeyboardSelected(context)) }
    var testText by remember { mutableStateOf("fun main() {\n    println(\"Hello Coding Keyboard!\")\n}") }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isKeyboardEnabled = checkIsKeyboardEnabled(context)
                isKeyboardSelected = checkIsKeyboardSelected(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B30)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF3D7EFF), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Coding Keyboard",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Keyboard Khusus Programming & Text Editing",
                        color = Color(0xFFB0B0B0),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Setup Steps
        Text(
            text = "Langkah Aktivasi Keyboard",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Step 1: Enable
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isKeyboardEnabled) Color(0xFF1E3A29) else Color(0xFF2B2B30)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isKeyboardEnabled) Icons.Default.CheckCircle else Icons.Default.Settings,
                    contentDescription = null,
                    tint = if (isKeyboardEnabled) Color(0xFF4CAF50) else Color(0xFF3D7EFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "1. Aktifkan di Pengaturan",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (isKeyboardEnabled) "Sudah diaktifkan" else "Izin input method belum diaktifkan",
                        color = Color(0xFFA0A0A0),
                        fontSize = 12.sp
                    )
                }
                if (!isKeyboardEnabled) {
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D7EFF))
                    ) {
                        Text("Buka")
                    }
                }
            }
        }

        // Step 2: Select
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isKeyboardSelected) Color(0xFF1E3A29) else Color(0xFF2B2B30)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isKeyboardSelected) Icons.Default.CheckCircle else Icons.Default.Keyboard,
                    contentDescription = null,
                    tint = if (isKeyboardSelected) Color(0xFF4CAF50) else Color(0xFF3D7EFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2. Pilih Sebagai Keyboard Aktif",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (isKeyboardSelected) "Sedang digunakan" else "Ketuk untuk memilih keyboard",
                        color = Color(0xFFA0A0A0),
                        fontSize = 12.sp
                    )
                }
                Button(
                    onClick = {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showInputMethodPicker()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D7EFF))
                ) {
                    Text("Pilih")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Playground
        Text(
            text = "Area Uji Coba (Code Playground)",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B30)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ketuk di bawah ini untuk membuka Coding Keyboard:",
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = testText,
                    onValueChange = { testText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E1E22),
                        unfocusedContainerColor = Color(0xFF1E1E22),
                        focusedBorderColor = Color(0xFF3D7EFF),
                        unfocusedBorderColor = Color(0xFF44444C),
                        focusedTextColor = Color(0xFFE0E0E0),
                        unfocusedTextColor = Color(0xFFE0E0E0)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                )
            }
        }

        // Features Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF232328)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fitur Utama Coding Keyboard:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Bar Toolbar Simbol: Auto-close kurung {}, [], (), \"\", ''", color = Color(0xFFB0B0B0), fontSize = 12.sp)
                Text("• Modifier Key: Modus Ctrl, Alt, Meta untuk shortcut Ctrl+A/C/V/Z", color = Color(0xFFB0B0B0), fontSize = 12.sp)
                Text("• Gesture Spacebar: Usap (Swipe) Spacebar ke kiri/kanan/atas/bawah untuk navigasi kursor cepat", color = Color(0xFFB0B0B0), fontSize = 12.sp)
                Text("• Function Keys: Tombol F1 - F12, Esc, Tab, Home, End, PgUp, PgDn", color = Color(0xFFB0B0B0), fontSize = 12.sp)
            }
        }
    }
}

private fun checkIsKeyboardEnabled(context: Context): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val enabledMethods = imm.enabledInputMethodList
    val packageName = context.packageName
    return enabledMethods.any { it.packageName == packageName }
}

private fun checkIsKeyboardSelected(context: Context): Boolean {
    val currentIme = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.DEFAULT_INPUT_METHOD
    )
    val packageName = context.packageName
    return currentIme != null && currentIme.contains(packageName)
}
