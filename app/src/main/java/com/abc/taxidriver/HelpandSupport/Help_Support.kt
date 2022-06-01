package com.abc.taxidriver.HelpandSupport

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.API.APIUtils
import com.API.APIUtils.Companion.getServiceAPI
import com.abc.taxidriver.HelpandSupport.Model.QuickContactResponse
import com.abc.taxidriver.Otp.Otp
import com.abc.taxidriver.R
import com.abc.taxidriver.Signup.Model.SigninResponse
import com.abc.taxidriver.Terms_Condition.Model.TermsConditionsResponse
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.NetworkUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_help_support.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_terms_condition.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Help_Support : AppCompatActivity() {
    var name = ""
    var email = ""
    var message = ""
    var driver_id = "dfdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)
        name = SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.NAME, "")
            .toString()
        email = SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.EMAIL, "")
            .toString()
        message = SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.MESSAGE, "")
            .toString()
        driver_id =
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DRIVER_ID, "")
                .toString()

//        var rlBack = findViewById<RelativeLayout>(R.id.rlBack)
//        var tvSubmit = findViewById<LinearLayout>(R.id.tvSubmit)

        tvSubmit.setOnClickListener {
            message = etMsg.text.toString()
            if (licence_edit_text.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzname),
                    Toast.LENGTH_LONG
                ).show()

            } else if (ietEmail.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzemail),
                    Toast.LENGTH_LONG
                ).show()

            } else if (etMsg.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this, resources.getString(com.abc.taxidriver.R.string.plzmsg),
                    Toast.LENGTH_LONG
                ).show()
            } else {

                helpsupport()
            }
        }

        rlBack.setOnClickListener {
            onBackPressed()
        }
    }



    fun helpsupport() {
        rlLoaderHelpSupport.visibility=View.VISIBLE
        val request = HashMap<String, String>()
        request.put("name", name)
        request.put("email", email)
        request.put("message", message)

        request.put("driver_id", driver_id)
        rlLoaderHelpSupport.visibility = View.VISIBLE

        var helpsupport: Call<QuickContactResponse> =
            getServiceAPI()!!.driverQuickContactApi(request)


        helpsupport.enqueue(object : Callback<QuickContactResponse> {
            override fun onResponse(
                call: Call<QuickContactResponse>,
                response: Response<QuickContactResponse>
            ) {
                try {

                    rlLoaderHelpSupport.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        var dialog = Dialog(this@Help_Support)
                        dialog.getWindow()!!
                            .setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            );
                        lateinit var cvOkk: CardView
                        dialog.setCancelable(true)
                        dialog.setContentView(R.layout.help_support_popup)
                        cvOkk = dialog.findViewById(R.id.cvOkk)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        dialog.show()

//        dialog.window?.setLayout(1050, 1100)
                        cvOkk.setOnClickListener {
                            licence_edit_text.setText("")
                            ietEmail.setText("")
                            etMsg.setText("")
                            getCurrentFocus()?.clearFocus()
                            onBackPressed()
                        }
                    }
                    else {

                        Toast.makeText(
                            this@Help_Support,
                            response.body()!!.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Log.e("Nitish", e.toString())
                    rlLoaderHelpSupport.visibility = View.GONE

                }

            }

            override fun onFailure(call: Call<QuickContactResponse>, t: Throwable) {
                Log.e("Nitish", t.message.toString())
                rlLoaderHelpSupport.visibility = View.GONE

            }

        })
    }


}




