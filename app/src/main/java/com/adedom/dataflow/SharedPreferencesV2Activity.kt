package com.adedom.dataflow

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("SharedPreferencesFile", Activity.MODE_PRIVATE)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
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

class SharedPreferencesV2Activity : BaseActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<SharedPreferencesV2ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        binding.btnSendData.setOnClickListener {
            viewModel.sendData()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "data") {
            val data = sharedPreferences?.getString("data", "")
            binding.tvData.text = data
        }
    }
}

class SharedPreferencesV2ViewModel(
    private val defaultRepository: DefaultRepository,
) : ViewModel() {

    fun sendData() {
        defaultRepository.saveData()
    }
}

interface DefaultRepository {

    fun saveData()
}

class DefaultRepositoryImpl(
    private val sharedPref: SharedPref
) : DefaultRepository {

    override fun saveData() {
        val data = System.currentTimeMillis().toString()
        sharedPref.data = data
    }
}

interface SharedPref {

    var data: String?
}

class SharedPrefImpl(context: Context) : SharedPref {

    private val sharedPreferences =
        context.getSharedPreferences("SharedPreferencesFile", Activity.MODE_PRIVATE)

    override var data: String?
        get() = sharedPreferences.getString("data", "")
        set(value) {
            sharedPreferences.edit {
                putString("data", value)
            }
        }
}