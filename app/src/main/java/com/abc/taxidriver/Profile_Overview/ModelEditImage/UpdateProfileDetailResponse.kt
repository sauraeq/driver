package com.abc.taxidriver.Profile_Overview.ModelEditImage

data class UpdateProfileDetailResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)