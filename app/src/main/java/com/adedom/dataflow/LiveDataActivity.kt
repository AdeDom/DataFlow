package com.adedom.dataflow

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adedom.dataflow.databinding.ActivityDataFlowBinding

class LiveDataActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<LiveDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        viewModel.dataLiveData.observe(this) { data ->
            binding.tvData.text = data
        }

        binding.btnSendData.setOnClickListener {
            val data = System.currentTimeMillis().toString()
            viewModel.sendData(data)
        }
    }
}

class LiveDataViewModel : ViewModel() {

    private val _dataLiveData = MutableLiveData<String>()
    val dataLiveData: LiveData<String> = _dataLiveData

    fun sendData(data: String) {
        _dataLiveData.value = data
    }
}