package com.example.mobco.Util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.mobco.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadingDialog(activity: Activity) {
    private lateinit var loadingDialog : AlertDialog

    init {
        loadingDialog = MaterialAlertDialogBuilder(activity)
            .setView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null, false))
            .setCancelable(false)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()
    }
    fun showDialog(){
        loadingDialog.show()
    }
    fun hideDialog(){
        loadingDialog.dismiss()
    }
}