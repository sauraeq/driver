package com.abc.taxidriver.Profile_Overview.ModelEditProfile

data class EditProfileResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
{
    data class Data(
        val address: String,
        val driving_license: String,
        val email: String,
        val gender: String,
        val name: String,
        val step: Int
    )
}