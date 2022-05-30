package com.adedom.dataflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SharedPreferencesActivity : AppCompatActivity() {

    @Inject
    lateinit var appDataStore: AppDataStore

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        appDataStore.getCurrentTimeFlow().onEach { currentTime ->
            binding.tvData.text = currentTime.toString()
        }.launchIn(lifecycleScope)

        binding.btnSendData.setOnClickListener {
            lifecycleScope.launch {
                appDataStore.setCurrentTime(System.currentTimeMillis())
            }
        }
    }
}

interface AppDataStore {
    suspend fun setCurrentTime(currentTime: Long)
    suspend fun getCurrentTime(): Long?
    fun getCurrentTimeFlow(): Flow<Long?>
}

class AppDataStoreImpl(
    private val dataStoreFile: DataStore<Preferences>,
) : AppDataStore {

    private val currentTime = longPreferencesKey("currentTime")

    override suspend fun setCurrentTime(currentTime: Long) {
        dataStoreFile.edit { preferences ->
            preferences[this.currentTime] = currentTime
        }
    }

    override suspend fun getCurrentTime(): Long? {
        return dataStoreFile.data.first()[currentTime]
    }

    override fun getCurrentTimeFlow(): Flow<Long?> {
        return dataStoreFile.data.map { preferences ->
            preferences[currentTime]
        }
    }
}