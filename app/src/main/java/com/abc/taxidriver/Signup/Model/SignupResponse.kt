package com.abc.taxidriver.Signup.Model

data class SignupResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
){
    data class Data(
        val address: String,
        val driving_license: String,
        val email: String,
        val gender: String,
        val name: String,
        val otp: String,
        val mobile: String,
        val driver_id: String,
        val profile_photo: String
    )
}