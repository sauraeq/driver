package com.abc.taxidriver.Payment.CompeteBookingModels

data class CompleteBookingResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)