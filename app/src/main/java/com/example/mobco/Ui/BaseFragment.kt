package com.example.mobco.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobco.Util.LoadingDialog

open class BaseFragment: Fragment() {
    public lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity())
    }
}