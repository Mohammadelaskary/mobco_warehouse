package com.example.mobco.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PODetailsItem2 (
    @SerializedName("po_header_id"            ) var poHeaderId           : Int?    = null,
    @SerializedName("po_line_id"              ) var poLineId             : Int?    = null,
    @SerializedName("ship_to_organization_id" ) var shipToOrganizationId : Int?    = null,
    @SerializedName("pono"                    ) var pono                 : String? = null,
    @SerializedName("supplier"                ) var supplier             : String? = null,
    @SerializedName("receiver"                ) var receiver             : String? = null,
    @SerializedName("receiptno"               ) var receiptno            : String? = null,
    @SerializedName("receiptdate"             ) var receiptdate          : String? = null,
    @SerializedName("itemcode"                ) var itemcode             : String? = null,
    @SerializedName("itemcategory"            ) var itemcategory         : String? = null,
    @SerializedName("itemdesc"                ) var itemdesc             : String? = null,
    @SerializedName("itemuom"                 ) var itemuom              : String? = null,
    @SerializedName("itemqty"                 ) var itemqty              : Int?    = null,
    @SerializedName("itemqtyreceived"         ) var itemqtyreceived      : Int?    = null,
    @SerializedName("itemqtyaccepted"         ) var itemqtyaccepted      : Int?    = null,
    @SerializedName("itemqtyrejected"         ) var itemqtyrejected      : Int?    = null,
    @SerializedName("itemqtydelivered"        ) var itemqtydelivered     : Int?    = null,
    @SerializedName("isinspected"             ) var isinspected          : String? = null,
    @SerializedName("isdelivered"             ) var isdelivered          : String? = null
){
    companion object{
        fun toJson (poDetailsItem2: PODetailsItem2):String{
            return Gson().toJson(poDetailsItem2)
        }
        fun fromJson(poDetailsItem2: String):PODetailsItem2{
            return Gson().fromJson(poDetailsItem2,PODetailsItem2::class.java)
        }
    }
}