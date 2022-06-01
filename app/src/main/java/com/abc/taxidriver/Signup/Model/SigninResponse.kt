package com.abc.taxidriver.Signup.Model

data class SigninResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
{
    data class Data(
        val address: String,
        val email: String,
        val gender: String,
        val mobile: String,
        val name: String,
        val otp: String,
        val profile_photo: String,
        val id: String,
        val vehicle_permit: String,
        val vehicle_no: String,
        val vehicle_name : String,
        val driving_license: String
    )
}