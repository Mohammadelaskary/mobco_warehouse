package com.example.mobco.Model.ApiRequestBody

data class ItemsReceivingBody(
    val employee_id: Int,
    val po_header_id: Int,
    val po_lines: List<PoLine>,
    val po_org_id: String,
    val program_application_id: Int,
    val program_id: Int,
    val transaction_date: String,
    val user_id: Int
)