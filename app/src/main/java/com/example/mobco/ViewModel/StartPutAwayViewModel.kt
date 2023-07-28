package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mobco.Repository.ReceivingRepository
import com.example.mobco.Tools
import com.example.mobco.Tools.back
import com.example.mobco.Ui.StartPutAwayFragment
import com.example.mobco.Util.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartPutAwayViewModel(app:Application,val activity: Activity) : BaseViewModel(app) {
    val receivingRepository = ReceivingRepository(activity)
    val newPutAwayQtyValue = MutableLiveData<String>()
    fun PutAwayMaterial(
        loadingDialog: LoadingDialog,
        poHeaderId: Int,
        poLineId: Int,
        receiptNo: String,
        shipToOrganizationId: Int,
        locator_id: String,
        subinventory_code:String,
        transactionDate:String,
        startPutAwayFragment: StartPutAwayFragment
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.PutAwayMaterial(
                poHeaderId = poHeaderId,
                poLineId = poLineId,
                receiptNo = receiptNo,
                shipToOrganizationId = shipToOrganizationId,
                locator_id = locator_id,
                subinventory_code = subinventory_code,
                transactionDate = transactionDate
            )
            withContext(Dispatchers.Main) {
                loadingDialog.hideDialog()
                if (response.isSuccessful) {
                    if (response.body()?.responseStatus?.isSuccess!!) {
                        Tools.showSuccessAlerter(message = response.body()?.responseStatus?.statusMessage.toString(),activity= activity)
                        newPutAwayQtyValue.value = response.body()?.responseStatus?.statusMessage.toString().substring(17,18)
                        back(startPutAwayFragment)
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