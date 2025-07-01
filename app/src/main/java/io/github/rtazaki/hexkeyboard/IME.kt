package io.github.rtazaki.hexkeyboard

import android.content.Context
import android.provider.Settings
import android.view.inputmethod.InputMethodManager

object IME {
    fun isMyInputMethodEnabled(context: Context): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.enabledInputMethodList.any { it.packageName == context.packageName }
    }

    fun isMyInputMethodCurrent(context: Context): Boolean {
        val currentIME = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        )
        return currentIME?.startsWith(context.packageName) == true
    }
}