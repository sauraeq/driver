package com.abc.taxidriver.Privacy_Policy.Model

data class PrivacyPolicyResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
{
    data class Data(
        val create_date: String,
        val description: String,
        val id: String
    )
}