package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.Group
import com.example.mobco.Adapters.PoDetailsAdapter
import com.example.mobco.Tools
import com.example.mobco.Util.LoadingDialog
import com.example.mobco.databinding.PoHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PODetailsViewModel(app: Application,val activity: Activity) : ReceivingViewModel(app,activity) {

    fun getPurchaseOrderDetailsList(
        loadingDialog: LoadingDialog,
        poAdapter: PoDetailsAdapter,
        orgNum:String,poNum:String
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPurchaseOrderDetailsList(orgNum,poNum)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    poAdapter.poList = response.body()?.getData()!!
                    loadingDialog.hideDialog()
                } else {
                    Tools.warningDialog(activity,response.body()?.responseStatus?.statusMessage!!)
                    loadingDialog.hideDialog()
                }
            }
        }
    }
}