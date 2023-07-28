package com.example.mobco.Model

import com.google.gson.annotations.SerializedName

data class PODetailsItem(
    @SerializedName("po_line_id"              ) var poLineId             : Int?    = null,
    @SerializedName("shipment_num"            ) var shipmentNum          : Int?    = null,
    @SerializedName("ship_to_organization_id" ) var shipToOrganizationId : Int?    = null,
    @SerializedName("ship_to_location_id"     ) var shipToLocationId     : Int?    = null,
    @SerializedName("itemlineno"              ) var itemLineNo           : Int?    = null,
    @SerializedName("shipto"                  ) var shipTo               : String? = null,
    @SerializedName("itemcode"                ) var itemCode             : String? = null,
    @SerializedName("itemcategory"            ) var itemCategory         : String? = null,
    @SerializedName("itemdesc"                ) var itemDesc             : String? = null,
    @SerializedName("itemuom"                 ) var itemUOM              : String? = null,
    @SerializedName("itemqty"                 ) var itemQty              : Int,
    @SerializedName("itemqtyreceived"         ) var itemQtyReceived      : Int,
    @SerializedName("itemqtyaccepted"         ) var itemQtyAccepted      : Int?    = null,
    @SerializedName("itemqtyrejected"         ) var itemQtyRejected      : Int?    = null,
    @SerializedName("itemqtybilled"           ) var itemQtyBilled        : Int?    = null,
    @SerializedName("itemqtycancelled"        ) var itemQtyCancelled     : Int?    = null,
    @SerializedName("itemprice"               ) var itemPrice            : Int?    = null,
    @SerializedName("need_by_date"            ) var needByDate           : String? = null
) {
    val remainingQty:Int
        get() = itemQty - itemQtyReceived
}