package com.tijn.basemodule.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 * 单向数据流动有助于实现以下几点：
 *1. 数据一致性。界面只有一个可信来源。
 *2. 可测试性。状态来源是独立的，因此可独立于界面进行测试。
 *3. 可维护性。状态的更改遵循明确定义的模式，即状态更改是用户事件及其数据拉取来源共同作用的结果。
 */
class MviViewModel : ViewModel() {
    val userIntent = Channel<ViewIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<ViewState>(ViewState.Idle)
    val state: StateFlow<ViewState>
        get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ViewIntent.FetchUser -> fetchUser()
                }
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            _state.value = try {
                ViewState.Users(arrayListOf(User(1, "1", "1", "1")))
            } catch (e: Exception) {
                ViewState.Error(e.localizedMessage)
            }
        }
    }
}

