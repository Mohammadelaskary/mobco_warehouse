package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mobco.Model.ApiResponse.BaseResponse
import com.example.mobco.Repository.BaseRepository
import com.example.mobco.Repository.ReceivingRepository
import kotlinx.coroutines.Job
import retrofit2.Response

open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}