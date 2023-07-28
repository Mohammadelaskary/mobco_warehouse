package com.example.mobco.Model.ApiResponse

import com.example.mobco.Model.PODetailsItem
import com.google.gson.annotations.SerializedName

data class ItemReceivingResponse(
    @SerializedName("getPoDetails"   )val getPoDetails: PODetailsItem,
): BaseResponse<PODetailsItem>() {
    override fun getData() = getPoDetails
}