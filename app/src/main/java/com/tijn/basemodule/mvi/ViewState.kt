package com.tijn.basemodule.mvi


sealed class ViewState {

    object Idle : ViewState()
    object Loading : ViewState()
    data class Users(val user: List<User>) : ViewState()
    data class Error(val error: String?) : ViewState()

}

