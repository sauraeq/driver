package com.abc.taxidriver.Dashboard.Model

data class AcceptrideResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
){
    data class Data(
        val booking_id: String,
        val driver_id: String
    )
}