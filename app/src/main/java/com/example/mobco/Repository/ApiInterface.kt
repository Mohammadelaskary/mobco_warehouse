package com.example.mobco.Repository

import com.example.mobco.Model.ApiRequestBody.InspectMaterialBody
import com.example.mobco.Model.ApiRequestBody.ItemsReceivingBody
import com.example.mobco.Model.ApiRequestBody.PutawayMaterialBody
import com.example.mobco.Model.ApiResponse.NoDataResponse
import com.example.mobco.Model.ApiResponse.PurchaseOrderLinesGetByIDResponse
import com.example.mobco.Model.ApiResponse.PurchaseOrderGetByIDResponse
import com.example.mobco.Model.ApiResponse.PurchaseOrderReceiptNoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @GET("PurchaseOrderGetByID")
    suspend fun getPurchaseOrdersList(
        @Query("UserID")  userId : Int,
        @Query("DeviceSerialNo")  DeviceSerialNo : String,
        @Query("applang")  appLang : String,
        @Query("org_id")  orgId : String,
        @Query("po_header_id")  poHeaderId : String,
    ) : Response<PurchaseOrderGetByIDResponse>?

    @GET("PurchaseOrderLinesGetByID")
    suspend fun getPurchaseOrderDetailsList(
        @Query("UserID")  userId : Int,
        @Query("DeviceSerialNo")  DeviceSerialNo : String,
        @Query("applang")  appLang : String,
        @Query("org_id")  orgId : String,
        @Query("po_header_id")  poHeaderId : String,
    ) : Response<PurchaseOrderLinesGetByIDResponse>

    @POST("ReceiveMaterial_Multi")
    suspend fun ItemReceiving(
        @Body  body : ItemsReceivingBody
    ) : Response<NoDataResponse>

    @GET("PurchaseOrderReceiptNoList")
    suspend fun getPurchaseOrderReceiptNoList(
        @Query("org_id")  orgId : String,
        @Query("pono")  poNo : String,
        @Query("receiptno")  receiptNo : String,
    ) : Response<PurchaseOrderReceiptNoListResponse>

    @POST("InspectMaterial")
    suspend fun InspectMaterial(
        @Body  body : InspectMaterialBody
    ) : Response<NoDataResponse>
    @POST("PutawayMaterial")
    suspend fun PutawayMaterial(
        @Body  body : PutawayMaterialBody
    ) : Response<NoDataResponse>
}