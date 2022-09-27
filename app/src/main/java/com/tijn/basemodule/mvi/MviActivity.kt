package com.tijn.basemodule.mvi

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tijn.basemodule.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MviActivity : AppCompatActivity() {
    private val viewModel: MviViewModel by lazy {
        ViewModelProvider(this).get(MviViewModel::class.java)
    }
    private val userTv: TextView by lazy { findViewById(R.id.userTv) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvi)
        initView()
        observeViewModel()
    }

    private fun initView() {
        userTv.setOnClickListener {
            lifecycleScope.launch {
                viewModel.userIntent.send(ViewIntent.FetchUser)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is ViewState.Idle -> {
                    }
                    is ViewState.Loading -> {
                        Toast.makeText(this@MviActivity, "Loading", Toast.LENGTH_LONG).show()
                    }

                    is ViewState.Users -> {
                        userTv.text = it.user[0].name
                    }
                    is ViewState.Error -> {
                        Toast.makeText(this@MviActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}