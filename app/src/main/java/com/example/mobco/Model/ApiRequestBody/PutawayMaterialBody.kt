package com.example.mobco.Model.ApiRequestBody

data class PutawayMaterialBody(
    val employee_id: Int,
    val locator_id: String,
    val po_header_id: Int,
    val po_line_id: Int,
    val receiptno: String,
    val ship_to_organization_id: Int,
    val subinventory_code: String,
    val user_id: Int,
    val transaction_date: String
)