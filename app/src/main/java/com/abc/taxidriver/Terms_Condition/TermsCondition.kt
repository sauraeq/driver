package com.abc.taxidriver.Terms_Condition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.API.APIUtils
import com.abc.taxidriver.R
import com.abc.taxidriver.Terms_Condition.Model.TermsConditionsResponse
import com.abc.taxidriver.Utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_terms_condition.*
import kotlinx.android.synthetic.main.header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TermsCondition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        tvTitle.setText("Terms & Condition")
        rlBack.setOnClickListener {
            onBackPressed()
        }

            if (NetworkUtils.checkInternetConnection(this)) {
                termsCondition()
            }
        }

    fun termsCondition() {
        rlLoaderterms.visibility = View.VISIBLE
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("description", tvDescription.text.toString())

        var termsconditions: Call<TermsConditionsResponse> = APIUtils.getServiceAPI()!!.termsConditionApi(stringStringHashMap)

        termsconditions.enqueue(object : Callback<TermsConditionsResponse> {
            override fun onResponse(
                call: Call<TermsConditionsResponse>,
                response: Response<TermsConditionsResponse>
            ) {
                try {

                    rlLoaderterms.visibility = View.GONE
                    if (response.body()!!.success.equals("true")) {

                        tvDescription.setText(Html.fromHtml(response.body()!!.data[0].description))

                    } else {

                        rlLoaderterms.visibility = View.GONE

                    }

                } catch (e: Exception) {
                    Log.e("nitish", e.toString())
                    Toast.makeText(this@TermsCondition, e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    rlLoaderterms.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<TermsConditionsResponse>, t: Throwable) {
                Log.e("nitish", t.message.toString())
                Toast.makeText(this@TermsCondition, t.message.toString(), Toast.LENGTH_LONG).show()
                rlLoaderterms.visibility = View.GONE

            }

        })
    }

}