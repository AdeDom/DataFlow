package com.adedom.dataflow

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.adedom.dataflow.databinding.ActivitySharedPreferenceV2Binding
import com.adedom.dataflow.databinding.FragmentSharedPreferenceV2Binding
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

abstract class BaseFragment : Fragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            context?.getSharedPreferences("SharedPreferencesFile", Activity.MODE_PRIVATE)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}

class SharedPreferencesV2Activity : BaseActivity() {

    private val binding by lazy {
        ActivitySharedPreferenceV2Binding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<SharedPreferencesV2ViewModel>()

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
            viewModel.sendData()
        }
    }
}

class FrameLayoutTopFragment : BaseFragment() {

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
}

class FrameLayoutBottomFragment : BaseFragment() {

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