package io.github.rtazaki.hexkeyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.rtazaki.hexkeyboard.databinding.ActivityTestInputBinding

class TestInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}