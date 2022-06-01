package com.abc.taxidriver.Language.Model

data class LanguageListResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
{
    data class Data(
        val create_date: String,
        val id: String,
        val name: String
    )
}