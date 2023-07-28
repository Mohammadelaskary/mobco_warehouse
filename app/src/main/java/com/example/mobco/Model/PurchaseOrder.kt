package com.example.mobco.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PurchaseOrder(
    @SerializedName("po_header_id"  ) var poHeaderId    : Int?    = null,
    @SerializedName("creation_date" ) var creationDate  : String? = null,
    @SerializedName("potype"        ) var poType        : String? = null,
    @SerializedName("operatingunit" ) var operatingUnit : String? = null,
    @SerializedName("pono"          ) var poNo          : String? = null,
    @SerializedName("supplier"      ) var supplier      : String? = null,
    @SerializedName("site"          ) var site          : String? = null,
    @SerializedName("shipto"        ) var shipTo        : String? = null,
    @SerializedName("billto"        ) var billTo        : String? = null,
    @SerializedName("buyer"         ) var buyer         : String? = null,
    @SerializedName("currency"      ) var currency      : String? = null
    ) {
    fun toJson (purchaseOrder: PurchaseOrder) :String = Gson().toJson(purchaseOrder).toString()
    fun fromJson (purchaseOrder: String) :PurchaseOrder = Gson().fromJson(purchaseOrder,PurchaseOrder::class.java)
}
