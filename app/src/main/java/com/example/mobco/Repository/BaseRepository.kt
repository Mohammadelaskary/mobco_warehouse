package com.example.mobco.Repository

import android.app.Activity
import android.content.Context
import android.provider.Settings
import com.example.mobco.Ui.SignInFragment
import com.example.mobco.Util.LocaleHelper

open class BaseRepository (
        val activity: Activity?
        ) {
    var apiInterface = ApiFactory.getInstance()?.create(ApiInterface::class.java)!!
    val lang = LocaleHelper.getLanguage(activity!!)!!
    val deviceSerialNo = Settings.Secure.getString(
        activity?.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    val localStorage = LocalStorage(activity!!)
    val userId = SignInFragment.USER_ID
}

