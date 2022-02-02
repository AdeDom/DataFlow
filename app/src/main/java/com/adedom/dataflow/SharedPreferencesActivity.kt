package com.adedom.dataflow

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SharedPreferencesActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

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