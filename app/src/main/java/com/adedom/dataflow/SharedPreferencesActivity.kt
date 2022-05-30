package com.adedom.dataflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adedom.dataflow.databinding.ActivityDataFlowBinding

class SharedPreferencesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title
    }
}