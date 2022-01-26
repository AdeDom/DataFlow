package com.adedom.dataflow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import kotlinx.coroutines.flow.*

class KotlinFlowActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<KotlinFlowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        viewModel.dataStateFlow
            .onEach { data ->
                binding.tvData.text = data
            }
            .catch { e ->
                Toast.makeText(baseContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)

        binding.btnSendData.setOnClickListener {
            val data = System.currentTimeMillis().toString()
            viewModel.sendData(data)
        }
    }
}

class KotlinFlowViewModel : ViewModel() {

    private val _dataStateFlow = MutableStateFlow("Kotlin flow")
    val dataStateFlow: StateFlow<String> = _dataStateFlow

    fun sendData(data: String) {
        _dataStateFlow.value = data
    }
}