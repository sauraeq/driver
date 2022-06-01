package com.abc.taxidriver.Terms_Condition.Model

data class TermsConditionsResponse(
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