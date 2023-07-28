package com.example.mobco.Model.ApiRequestBody

data class InspectMaterialBody(
    val employee_id: Int,
    val itemqtyaccepted: Int,
    val po_header_id: Int,
    val po_line_id: Int,
    val receiptno: String,
    val ship_to_organization_id: Int,
    val user_id: Int,
    val transaction_date: String
)