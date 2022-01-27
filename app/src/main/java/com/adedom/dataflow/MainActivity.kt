package com.adedom.dataflow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adedom.dataflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSavedStateHandle.setOnClickListener {
            val intent = Intent(this, SavedStateHandleActivity::class.java)
            intent.putExtra("title", "Saved state handle")
            startActivity(intent)
        }

        binding.btnSharedPreferences.setOnClickListener {
            val intent = Intent(this, SharedPreferencesActivity::class.java)
            intent.putExtra("title", "Shared preferences")
            startActivity(intent)
        }

        binding.btnSharedPreferencesV2.setOnClickListener {
            val intent = Intent(this, SharedPreferencesV2Activity::class.java)
            intent.putExtra("title", "Shared preferences v2")
            startActivity(intent)
        }

        binding.btnDatabase.setOnClickListener {
            val intent = Intent(this, DatabaseActivity::class.java)
            intent.putExtra("title", "Database")
            startActivity(intent)
        }

        binding.btnLiveData.setOnClickListener {
            val intent = Intent(this, LiveDataActivity::class.java)
            intent.putExtra("title", "Live data")
            startActivity(intent)
        }

        binding.btnKotlinFlow.setOnClickListener {
            val intent = Intent(this, KotlinFlowActivity::class.java)
            intent.putExtra("title", "Kotlin flow")
            startActivity(intent)
        }
    }
}