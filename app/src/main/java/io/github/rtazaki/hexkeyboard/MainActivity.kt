package io.github.rtazaki.hexkeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.rtazaki.hexkeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var hasShownPicker = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // キーボード有効化
        if (!IME.isMyInputMethodEnabled(this)) {
            val intent = Intent(this, KeyboardEnableActivity::class.java)
            startActivity(intent)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        // キーボード切り替え画面を催促するのは1回だけにする。
        if (hasFocus && !hasShownPicker) {
            if (!IME.isMyInputMethodCurrent(this)) {
                hasShownPicker = true
                AlertDialog.Builder(this)
                    .setTitle("キーボード切り替え")
                    .setMessage("入力方法として[HexKeyBoard]を選択してください")
                    .setPositiveButton("OK") { _, _ ->
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showInputMethodPicker()
                    }.show()
            }
        }
    }
}