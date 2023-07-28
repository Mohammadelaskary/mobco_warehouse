package com.example.mobco.Model.ApiResponse

import com.example.mobco.Model.ResponseStatus
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class BaseResponse<T> {
    @SerializedName("responseStatus")
    @Expose
    var responseStatus: ResponseStatus? = null

    abstract fun getData(): T

}