package com.example.mobco.Model.ApiResponse

import com.google.gson.annotations.SerializedName

class NoDataResponse {
    @SerializedName("responseStatus" ) var responseStatus : ResponseStatusX? = ResponseStatusX()
}