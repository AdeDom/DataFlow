package com.adedom.dataflow

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.adedom.dataflow.databinding.ActivityDataFlowBinding

class SharedPreferencesActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        sharedPreferences = getSharedPreferences("SharedPreferencesFile", Activity.MODE_PRIVATE)

        binding.btnSendData.setOnClickListener {
            val data = System.currentTimeMillis().toString()
            sendData(data)
        }
    }

    private fun sendData(data: String) {
        sharedPreferences.edit {
            putString("data", data)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "data") {
            val data = sharedPreferences?.getString("data", "")
            binding.tvData.text = data
        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}