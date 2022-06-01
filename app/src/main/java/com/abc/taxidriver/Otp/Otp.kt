package com.abc.taxidriver.Otp

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.API.APIUtils
import com.abc.taxidriver.Dashboard.Map_DashBoard

import com.abc.taxidriver.Signup.Model.SigninResponse
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.NetworkUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.Utils.ToastUtil
import com.chaos.view.PinView
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class Otp : AppCompatActivity() {

    var otp = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        otp=intent.getStringExtra("otp").toString()
        setOtp()

        tvVerify.setOnClickListener {

            var otp2=etCode1.text.toString()+etCode2.text.toString()+etCode3.text.toString()+etCode4.text.toString()
            Log.d("otp11",otp2+" "+otp)
           if(otp2.isNullOrEmpty()){
               ToastUtil.toast_Long(this,resources.getString(R.string.plzotp))
           }else if(!otp2.equals(otp)){
               ToastUtil.toast_Long(this,resources.getString(R.string.plzvalidotp))
           }else{

               SharedPreferenceUtils.getInstance(this)
                   ?.setStringValue(ConstantUtils.IS_LOGIN, "true")
               var intent = Intent(this, Map_DashBoard::class.java)
               intent.putExtra("status", "OTP")
               startActivity(intent)
               finishAffinity()
           }


        }

    }

    private fun setOtp() {
            etCode1.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().length > 0) {
                        etCode2.requestFocus()
                    }
                }
            })

            etCode2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p0.toString().length > 0) {
                        etCode3.requestFocus()
                    }
                }
            })

            etCode3.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p0.toString().length > 0) {
                        etCode4.requestFocus()
                    }
                }
            })

            etCode2.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    etCode2.setText("")
                    etCode1.requestFocus()
                }
                false
            }

            etCode3.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    etCode3.setText("")
                    etCode2.requestFocus()
                }
                false
            }
            etCode4.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    etCode4.setText("")
                    etCode3.requestFocus()
                }
                false
            }

        }
/*
    fun Login() {
        val request = HashMap<String, String>()
        request.put("mobile", mobile)
        request.put("device_tokanid", devicetokanid)
        rlLoader.visibility = View.VISIBLE

        var login: Call<SigninResponse> =
            APIUtils.getServiceAPI()!!.otpVerify(request)


        login.enqueue(object : Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                try {

                    rlLoader.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        if (!response.body()!!.data.driver_id.isNullOrEmpty()) {
                            Toast.makeText(
                                this@SignupActivity,
                                response.body()!!.data.otp,
                                Toast.LENGTH_LONG
                            ).show()
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.DRIVER_ID, response.body()!!.data.driver_id
                                )
                        }
                        if (!response.body()!!.data.mobile.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.MOBILE, response.body()!!.data.mobile
                                )
                        }
                        if (!response.body()!!.data.name.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.NAME, response.body()!!.data.name
                                )
                        }
                        if (!response.body()!!.data.email.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.EMAIL, response.body()!!.data.email
                                )
                        }

                        if (!response.body()!!.data.address.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.ADDRESS, response.body()!!.data.address
                                )
                        }
                        if (!response.body()!!.data.gender.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.GENDER, response.body()!!.data.gender
                                )
                        }

                        if (!response.body()!!.data.profile_photo.isNullOrEmpty()) {
                            SharedPreferenceUtils.getInstance(this@SignupActivity)
                                ?.setStringValue(
                                    ConstantUtils.PROFILE_PHOTO,
                                    response.body()!!.data.profile_photo
                                )
                        }

                        var intent = Intent(this@SignupActivity, Otp::class.java)
                        intent.putExtra("otp", response.body()!!.data.otp)
                        startActivity(intent)

                    } else {

                        Toast.makeText(
                            this@SignupActivity, response.body()!!.msg, Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Log.e("Nitish", e.toString())
                    Toast.makeText(
                        this@SignupActivity, response.body()!!.msg, Toast.LENGTH_LONG
                    ).show()
                    rlLoader.visibility = View.GONE

                }

            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.e("Nitish", t.message.toString())
                rlLoader.visibility = View.GONE
                Toast.makeText(this@SignupActivity,"this phone number does not exist",Toast.LENGTH_LONG).show()

            }

        })
    }
*/

    }







