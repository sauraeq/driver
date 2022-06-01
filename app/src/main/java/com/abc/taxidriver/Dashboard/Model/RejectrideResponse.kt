package com.abc.taxidriver.Dashboard.Model

data class RejectrideResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)