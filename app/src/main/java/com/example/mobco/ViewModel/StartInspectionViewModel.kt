package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import com.example.mobco.Repository.ReceivingRepository
import com.example.mobco.Tools
import com.example.mobco.Tools.back
import com.example.mobco.Ui.StartInspectionFragment
import com.example.mobco.Util.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartInspectionViewModel(app:Application,val activity: Activity) : BaseViewModel(app) {
    val receivingRepository = ReceivingRepository(activity)
    fun InspectMaterial(
        loadingDialog: LoadingDialog,
        poHeaderId: Int,
        poLineId: Int,
        receiptNo: String,
        shipToOrganizationId: Int,
        acceptedQty: Int,
        transactionDate:String,
        startInspectionFragment: StartInspectionFragment
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.InspectMaterial(
                poHeaderId = poHeaderId,
                poLineId = poLineId,
                receiptNo = receiptNo,
                shipToOrganizationId = shipToOrganizationId,
                acceptedQty = acceptedQty,
                transactionDate = transactionDate
            )
            withContext(Dispatchers.Main) {
                loadingDialog.hideDialog()
                if (response.isSuccessful) {
                    if (response.body()?.responseStatus?.isSuccess!!) {
                        Tools.showSuccessAlerter(message = response.body()?.responseStatus?.statusMessage.toString(),activity= activity)
                        back(startInspectionFragment)
                    } else {
                        Tools.warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                    }
                } else {
                    Tools.warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                }
            }
        }
    }
}