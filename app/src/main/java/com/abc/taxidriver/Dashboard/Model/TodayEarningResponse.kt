package com.abc.taxidriver.Dashboard.Model

data class TodayEarningResponse(
    val `data`: ArrayList<Data>,
    val dataall: List<Dataall>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
){

    data class Data(
        val amount: String,
        val cancel: String,
        val cancel_description: String,
        val created_date: String,
        val cupon_code: String,
        val device_tokanid: String,
        val distance: String,
        val driver_id: String,
        val drop_address: String,
        val drop_latitude: String,
        val drop_longitude: String,
        val id: String,
        val otp: String,
        val pickup_address: String,
        val pickup_latitude: String,
        val pickup_longitude: String,
        val rejected_trip: Any,
        val status: String,
        val time: String,
        val uid: String,
        val user_name: String,
        val user_photo: String
    )
}