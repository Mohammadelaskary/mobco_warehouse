package com.example.mobco.Model.ApiResponse

import com.example.mobco.Model.PODetailsItem
import com.google.gson.annotations.SerializedName

data class PurchaseOrderLinesGetByIDResponse(
    @SerializedName("getList")
    val getList: List<PODetailsItem>
) : BaseResponse<List<PODetailsItem>>() {
    override fun getData(): List<PODetailsItem> = getList
}