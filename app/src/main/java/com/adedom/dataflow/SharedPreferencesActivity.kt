package com.adedom.dataflow

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val Context.dataStoreFile by preferencesDataStore("file")

class SharedPreferencesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private val currentTime = longPreferencesKey("currentTime")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        dataStoreFile.data.onEach { preferences ->
            binding.tvData.text = preferences[currentTime].toString()
        }.launchIn(lifecycleScope)

        binding.btnSendData.setOnClickListener {
            lifecycleScope.launch {
                dataStoreFile.edit { preferences ->
                    preferences[currentTime] = System.currentTimeMillis()
                }
            }
        }
    }
}