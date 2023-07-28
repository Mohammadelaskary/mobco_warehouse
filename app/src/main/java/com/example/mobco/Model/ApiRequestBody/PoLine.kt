package com.example.mobco.Model.ApiRequestBody

data class PoLine(
    val po_line_id: Int,
    val quantity_received: Int,
    val ship_to_location_id: Int,
    val ship_to_organization_id: Int
)