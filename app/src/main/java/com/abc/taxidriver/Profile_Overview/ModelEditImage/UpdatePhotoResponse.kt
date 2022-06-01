package com.abc.taxidriver.Profile_Overview.ModelEditImage

data class UpdatePhotoResponse(
    val `data`: List<Any>,
    val error: Int,
    val image: String,
    val msg: String,
    val service: String,
    val success: String
)