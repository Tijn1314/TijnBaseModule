package com.tijn.basemodule.net.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tijn.netlibrary.model.NetResult
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val homeRepository= HomeRepository()
    private val bannerLiveData = MutableLiveData<List<Banner>>()

    fun getBannerLiveData(): MutableLiveData<List<Banner>> {
        return bannerLiveData
    }


    fun getBanner() {

        viewModelScope.launch {
            val banner = homeRepository.getBanner()
            if (banner is NetResult.Success) {
                bannerLiveData.postValue(banner.data)
            } else if (banner is NetResult.Error) {
//                Toast.makeText(
//                    BaseContext.instance.getContext(),
//                    banner.exception.msg,
//                    Toast.LENGTH_LONG
//                ).show()
            }
        }

    }

}