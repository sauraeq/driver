package com.abc.taxidriver.HelpandSupport.Model

data class QuickContactResponse(
    val `data`: Int,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)