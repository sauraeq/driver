package com.abc.taxidriver.Dashboard.Model

data class DriverOfflineDetailResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
{
    data class Data(
        val acp: String,
        val cancelation: String,
        val rating: String
    )
}