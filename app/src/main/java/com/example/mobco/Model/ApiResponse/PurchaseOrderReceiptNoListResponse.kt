package com.example.mobco.Model.ApiResponse

import com.example.mobco.Model.PODetailsItem2
import com.example.mobco.Model.PurchaseOrder
import com.google.gson.annotations.SerializedName

data class PurchaseOrderReceiptNoListResponse(
    @SerializedName("getList")
    val poList: List<PODetailsItem2>
) : BaseResponse<List<PODetailsItem2>>() {
    override fun getData(): List<PODetailsItem2> = poList
}