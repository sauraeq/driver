package com.abc.taxidriver.Privacy_Policy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.API.APIUtils
import com.abc.taxidriver.Privacy_Policy.Model.PrivacyPolicyResponse
import com.abc.taxidriver.R
import com.abc.taxidriver.Terms_Condition.Model.TermsConditionsResponse
import com.abc.taxidriver.Utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_privacy_policy.*

import kotlinx.android.synthetic.main.header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Privacy_Policy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
//        var tvTitle = findViewById<TextView>(R.id.tvTitle)
//        var rlBack = findViewById<RelativeLayout>(R.id.rlBack)

        tvTitle.setText("Privacy & Policy")
        rlBack.setOnClickListener {
            onBackPressed()
        }
        if (NetworkUtils.checkInternetConnection(this)) {
            PrivacyPolicy()
        }
    }

    fun PrivacyPolicy() {
        rlLoaderprivacy.visibility = View.VISIBLE
        val request = HashMap<String, String>()

        var privacypolicy: Call<PrivacyPolicyResponse> =
            APIUtils.getServiceAPI()!!.privacyPolicyApi(request)

        privacypolicy.enqueue(object : Callback<PrivacyPolicyResponse> {
            override fun onResponse(
                call: Call<PrivacyPolicyResponse>,
                response: Response<PrivacyPolicyResponse>
            ) {
                try {

                    rlLoaderprivacy.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        tv_Description.setText(Html.fromHtml(response.body()!!.data[0].description))

                    } else {

                        rlLoaderprivacy.visibility = View.GONE

                    }

                } catch (e: Exception) {
                    Log.e("nitish", e.toString())
                    Toast.makeText(this@Privacy_Policy, e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    rlLoaderprivacy.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<PrivacyPolicyResponse>, t: Throwable) {
                Log.e("nitish", t.message.toString())
                Toast.makeText(this@Privacy_Policy, t.message.toString(), Toast.LENGTH_LONG).show()
                rlLoaderprivacy.visibility = View.GONE

            }

        })
    }

}