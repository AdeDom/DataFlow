package com.adedom.dataflow

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.adedom.dataflow.databinding.ActivityDataFlowBinding

class SavedStateHandleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<SavedStateHandleViewModel>()

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

class SavedStateHandleViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val dataLiveData = savedStateHandle.getLiveData<String>("data")

    fun sendData(data: String) {
        savedStateHandle.set("data", data)
    }
}