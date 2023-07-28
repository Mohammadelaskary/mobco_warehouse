package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.MutableLiveData
import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.R
import com.example.mobco.Tools
import com.example.mobco.Util.LoadingDialog
import kotlinx.coroutines.*

class ReceivePOViewModel(application: Application,val activity: Activity) :
    ReceivingViewModel(application,activity) {

    var organizationNumber :String?=null
    var poNumber :String?=null

    val purchaseOrderLiveData = MutableLiveData<PurchaseOrder>()


     fun getPurchaseOrdersList(
         loadingDialog: LoadingDialog,
         dataGroup: Group,
         orgNum: String,
         poNum: String
     ) {
         loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPurchaseOrdersList(orgNum,poNum)
            withContext(Dispatchers.Main) {
                if (response!=null) {
                    if (response.isSuccessful) {
                        if (response.body()?.getData()!!.isNotEmpty()) {
                            dataGroup.visibility = VISIBLE
                            val purchaseOrder = response.body()?.getData()!![0]
                            purchaseOrderLiveData.value = purchaseOrder
                        } else {
                            dataGroup.visibility = GONE
                            Tools.warningDialog(
                                activity,
                                activity.getString(R.string.no_purchase_requisitions_found)
                            )
                        }
                        loadingDialog.hideDialog()
                    } else {
                        dataGroup.visibility = GONE
                        Tools.warningDialog(
                            activity,
                            response.body()?.responseStatus?.statusMessage!!
                        )
                        loadingDialog.hideDialog()
                    }
                } else {
                    dataGroup.visibility = GONE
                    Tools.warningDialog(
                        activity,
                        activity.getString(R.string.error_in_connection)
                    )
                    loadingDialog.hideDialog()
                }
            }
        }
    }
}