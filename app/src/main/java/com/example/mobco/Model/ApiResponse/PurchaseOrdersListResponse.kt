package com.example.mobco.Model.ApiResponse

import com.example.mobco.Model.PurchaseOrder
import com.example.mobco.Model.ResponseStatus
import com.google.gson.annotations.SerializedName

data class PurchaseOrdersListResponse(
    @SerializedName("getList")
    val poList: List<PurchaseOrder>
    ) : BaseResponse<List<PurchaseOrder>>() {
    override fun getData(): List<PurchaseOrder> = poList

}