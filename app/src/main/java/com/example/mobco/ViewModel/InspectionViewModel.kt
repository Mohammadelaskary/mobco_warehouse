package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.R
import com.example.mobco.Repository.ReceivingRepository
import com.example.mobco.Tools
import com.example.mobco.Util.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class InspectionViewModel(app: Application,val activity: Activity) : BaseViewModel(app) {
    val receivingRepository = ReceivingRepository(activity)
    var orgNum:String? = null
    var poNum:String? = null
    var receiptNo:String? = null
    val poDetailsItemsLiveData = MutableLiveData<List<PODetailsItem2>>()
    fun getPurchaseOrderReceiptNoList(
        loadingDialog: LoadingDialog,
        orgNum: String,
        poNum: String,
        receiptNo: String,
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPurchaseOrderReceiptNoList(orgNum,poNum,receiptNo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body()?.getData()!!.isNotEmpty()) {
                        val poDetailsItems = response.body()?.getData()!!
                        poDetailsItemsLiveData.value = poDetailsItems
                    } else {
                        Tools.warningDialog(
                            activity,
                            activity.getString(R.string.no_purchase_requisitions_found)
                        )
                    }
                    loadingDialog.hideDialog()
                } else {
                    Tools.warningDialog(
                        activity,
                        response.body()?.responseStatus?.statusMessage!!
                    )
                    loadingDialog.hideDialog()
                }
            }
        }
    }
}