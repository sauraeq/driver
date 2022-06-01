package com.abc.taxidriver.Otp.OtpModels

data class OtpVerifyResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)