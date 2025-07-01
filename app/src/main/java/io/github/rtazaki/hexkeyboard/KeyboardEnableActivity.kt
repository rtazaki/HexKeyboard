package io.github.rtazaki.hexkeyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import io.github.rtazaki.hexkeyboard.databinding.ActivityKeyboardEnableBinding

class KeyboardEnableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKeyboardEnableBinding
    private var hasCheckedIME = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKeyboardEnableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKeyboardEnable.setOnClickListener {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        // IME有効にしない限り元の画面には戻らないようにする。
        if (hasFocus && !hasCheckedIME) {
            hasCheckedIME = true
            if (IME.isMyInputMethodEnabled(this)) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else {
                hasCheckedIME = false
            }
        }
    }
}