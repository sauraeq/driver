package com.abc.taxidriver.Profile_Overview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.Utils.StringUtil
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_profile_overview.*
import kotlinx.android.synthetic.main.activity_profile_overview.ivProfile
import kotlinx.android.synthetic.main.activity_profile_overview.rlImage
import kotlinx.android.synthetic.main.activity_profile_overview.tvGender
import kotlinx.android.synthetic.main.header.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class Profile_Overview : AppCompatActivity() {
    var profile_profile:String=""
    var driving_license:String=""
    var image1path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_overview)
        tvTitle.setText("Profile Overview")


        rlImage.setOnClickListener {

            var intent = Intent(this, EditProfile::class.java)
            startActivity(intent)

        }
        rlBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onStart() {
        super.onStart()
        init()
    }


    private fun init() {
        profile_profile=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PROFILE_PHOTO,"").toString()
        tvName.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.NAME, ""
            )?.let { StringUtil.capString(it) }
        )
        SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PHONE_NO, "")
            ?.let { Log.d("mobileno", it) }
        tvMobNum.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.PHONE_NO, ""
            )
        )
        tvAddress.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.ADDRESS, ""
            )
        )
        tvEmail.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.EMAIL, ""
            )
        )
        tvVehicleType.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_TYPE, ""
            )
        )
        tvVehicleno.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_NO, ""
            )
        )
        tvLicence.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.DRIVING_LICENSE, ""
            )

        )
        tvVehicleLicence.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.VEHICLE_LICENSE, ""
            )

        )
        tvGender.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.GENDER, ""
            )

        )

        try {
            val picasso= Picasso.get()
            picasso.load(profile_profile).placeholder(R.drawable.default_profile).into(ivProfile)
        } catch (e:java.lang.Exception)
        {

        }
    }
}