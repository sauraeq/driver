package com.abc.taxidriver.Dashboard.Model

data class DriverLocationResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)