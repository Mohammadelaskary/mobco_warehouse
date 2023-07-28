package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import com.example.mobco.Repository.ReceivingRepository

open class ReceivingViewModel(application: Application,activity: Activity):BaseViewModel(application) {
    val receivingRepository = ReceivingRepository(activity)
}