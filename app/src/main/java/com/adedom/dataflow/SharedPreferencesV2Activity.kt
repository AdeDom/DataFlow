package com.adedom.dataflow

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.adedom.dataflow.databinding.ActivitySharedPreferenceV2Binding
import com.adedom.dataflow.databinding.FragmentSharedPreferenceV2Binding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SharedPreferencesV2Activity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySharedPreferenceV2Binding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<SharedPreferencesV2ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(binding.frameLayoutTop.id, FrameLayoutTopFragment())
                replace(binding.frameLayoutBottom.id, FrameLayoutBottomFragment())
            }
        }

        binding.btnSendData.setOnClickListener {
            val data = System.currentTimeMillis().toString()
            viewModel.sendData(data)
        }
    }
}

@AndroidEntryPoint
class FrameLayoutTopFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentSharedPreferenceV2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSharedPreferenceV2Binding.inflate(inflater, container, false)
        return binding.root
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

@AndroidEntryPoint
class FrameLayoutBottomFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentSharedPreferenceV2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSharedPreferenceV2Binding.inflate(inflater, container, false)
        return binding.root
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

@HiltViewModel
class SharedPreferencesV2ViewModel @Inject constructor(
    private val defaultRepository: DefaultRepository,
) : ViewModel() {

    fun sendData(data: String) {
        defaultRepository.saveData(data)
    }
}

interface DefaultRepository {

    fun saveData(data: String)
}

class DefaultRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : DefaultRepository {

    override fun saveData(data: String) {
        sharedPref.data = data
    }
}

interface SharedPref {

    var data: String?
}

class SharedPrefImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPref {

    override var data: String?
        get() = sharedPreferences.getString("data", "")
        set(value) {
            sharedPreferences.edit {
                putString("data", value)
            }
        }
}