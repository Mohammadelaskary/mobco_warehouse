package com.example.mobco.ViewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mobco.Model.ApiRequestBody.PoLine
import com.example.mobco.Model.PODetailsItem
import com.example.mobco.R
import com.example.mobco.Tools.back
import com.example.mobco.Tools.showSuccessAlerter
import com.example.mobco.Tools.warningDialog
import com.example.mobco.Ui.StartReceiveFragment
import com.example.mobco.Util.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartReceiveViewModel (app: Application, val activity: Activity) : ReceivingViewModel(app,activity) {
    val itemsList = MutableLiveData<List<PODetailsItem>>()

    fun getPurchaseOrderDetailsList(
        loadingDialog: LoadingDialog,
        orgNum:String,poNum:String
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPurchaseOrderDetailsList(orgNum, poNum)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body()?.responseStatus?.isSuccess!!) {
                        itemsList.value = response.body()?.getData()!!
                    } else {
                        warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                    }
                    loadingDialog.hideDialog()
                } else {
                    warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                    loadingDialog.hideDialog()
                }
            }
        }
    }

    fun ItemsReceiving(
        StartReceiveFragment: StartReceiveFragment,
        loadingDialog: LoadingDialog,
        poHeaderId: Int,
        poLines: List<PoLine>,
        poOrgId: String,
        transactionDate: String
    ) {
        loadingDialog.showDialog()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.ItemReceiving(poHeaderId,poLines, poOrgId,transactionDate)
            withContext(Dispatchers.Main) {
                loadingDialog.hideDialog()
                if (response.isSuccessful) {
                    if (response.body()?.responseStatus?.isSuccess!!) {
                        showSuccessAlerter(message = response.body()?.responseStatus?.statusMessage.toString(),activity= activity)
                        back(StartReceiveFragment)
                    } else {
                        warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                    }
                } else {
                    warningDialog(activity, activity.getString(R.string.error_in_connection))
                }
            }
        }
    }
}