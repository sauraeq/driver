package com.abc.taxidriver.Dashboard.CancelReasonModels

data class CancelReasonResponse(
    val `data`: ArrayList<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)