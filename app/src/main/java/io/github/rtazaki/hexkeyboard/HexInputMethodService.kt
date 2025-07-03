package io.github.rtazaki.hexkeyboard

import android.inputmethodservice.InputMethodService
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedTextRequest
import com.google.android.material.R.style
import io.github.rtazaki.hexkeyboard.databinding.KeyboardLayoutBinding

class HexInputMethodService : InputMethodService() {

    private var _binding: KeyboardLayoutBinding? = null
    private val binding get() = _binding!!

    private var isShiftLock = true

    override fun onEvaluateFullscreenMode() = false

    override fun onCreateInputView(): View {
        val themedContext = ContextThemeWrapper(this, style.Theme_Material3_DayNight)
        val inflater = LayoutInflater.from(themedContext)
        _binding = KeyboardLayoutBinding.inflate(inflater)

        val numButtons = listOf(
            "0" to binding.btn0,
            "1" to binding.btn1,
            "2" to binding.btn2,
            "3" to binding.btn3,
            "4" to binding.btn4,
            "5" to binding.btn5,
            "6" to binding.btn6,
            "7" to binding.btn7,
            "8" to binding.btn8,
            "9" to binding.btn9,
        )

        numButtons.forEach { (char, button) ->
            button.setOnClickListener {
                currentInputConnection.commitText(char, 1)
            }
        }

        val alphaButtons = listOf(
            "A" to binding.btnA,
            "B" to binding.btnB,
            "C" to binding.btnC,
            "D" to binding.btnD,
            "E" to binding.btnE,
            "F" to binding.btnF,
        )

        alphaButtons.forEach { (char, button) ->
            button.setOnClickListener {
                currentInputConnection.commitText(char, 1)
            }
        }

        binding.btnBs.setOnClickListener {
            val extractedText = currentInputConnection.getExtractedText(ExtractedTextRequest(), 0)
            val selectionStart = extractedText.selectionStart
            val selectionEnd = extractedText.selectionEnd
            if (selectionStart != selectionEnd) {
                // 選択範囲がある場合、その範囲を空文字で置き換えて削除
                currentInputConnection.setSelection(selectionStart, selectionEnd)
                currentInputConnection.commitText("", 1)
            } else {
                // 選択範囲がなければカーソル直前の1文字を削除
                currentInputConnection.deleteSurroundingText(1, 0)
            }
        }

        binding.btnEnter.setOnClickListener {
            val imeOptions = currentInputEditorInfo.imeOptions
            val action = imeOptions and EditorInfo.IME_MASK_ACTION

            if (action != EditorInfo.IME_ACTION_NONE) {
                currentInputConnection?.performEditorAction(action)
            } else {
                currentInputConnection?.commitText("\n", 1)
            }
        }

        binding.btnShift.setOnClickListener {
            isShiftLock = !isShiftLock
            binding.btnShift.text = if (isShiftLock) "⬆️" else "⬆"
            alphaButtons.forEach { (char, button) ->
                val text = if (isShiftLock) char.uppercase() else char.lowercase()
                button.text = text
                button.setOnClickListener {
                    currentInputConnection.commitText(text, 1)
                }
            }
        }

        binding.btnSpace.setOnClickListener {
            currentInputConnection.commitText(" ", 1)
        }

        binding.btnCursorLeft.setOnClickListener {
            currentInputConnection.apply {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT))
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT))
            }
        }

        binding.btnCursorRight.setOnClickListener {
            currentInputConnection.apply {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT))
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT))
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
