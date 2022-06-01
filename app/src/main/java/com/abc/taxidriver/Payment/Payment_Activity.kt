package com.abc.taxidriver.Payment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.API.APIUtils
import com.abc.taxidriver.Dashboard.Map_DashBoard
import com.abc.taxidriver.Dashboard.Model.AcceptrideResponse
import com.abc.taxidriver.Payment.CompeteBookingModels.CompleteBookingResponse
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.Utils.StringUtil
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Payment_Activity : AppCompatActivity() {
    var custName: String = ""
    var pickupAddress: String = ""
    var dropAddress: String = ""
    var bookingPrice: String = ""
    var bookingId=""
    var isCompleted:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
       /* bookingId=intent.getStringExtra("bookingId").toString()
        custName=intent.getStringExtra("custName").toString()
        dropAddress=intent.getStringExtra("dropAddress").toString()
        pickupAddress=intent.getStringExtra("pickupAddress").toString()
        bookingPrice=intent.getStringExtra("bookingPrice").toString()*/
        SharedPreferenceUtils.getInstance(this)?.setBoolanValue(ConstantUtils.IS_PAYMENT,true)

        bookingId=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.BOOKING_ID,"").toString()
        custName=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.CUST_NAME,"").toString()
        pickupAddress=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PICK_LOCATION,"").toString()
        dropAddress=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DROP_LOCATION,"").toString()
        bookingPrice=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.BOOKING_AMOUNT,"")
                .toString()

        tvName.setText(StringUtil.capString(custName)+" Hi,")
        tvpickupAddress.setText(pickupAddress)
        tvdropAddress.setText(dropAddress)
        tvBookingPrice.setText("$ "+bookingPrice)
        var rl_back = findViewById<RelativeLayout>(R.id.rl_back)

        rl_back.setOnClickListener {
            onBackPressed()
        }
        tvDone.setOnClickListener() {
            completeRide()
        }
    }



    private fun completeRide() {
        rlLoader.visibility=View.VISIBLE
        var request = HashMap<String, String>()

        request.put("booking_id", bookingId)
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }

        var call = APIUtils.getServiceAPI()!!.completeRide(request)

        call.enqueue(object : Callback<CompleteBookingResponse> {
            override fun onResponse(
                call: Call<CompleteBookingResponse>,
                response: Response<CompleteBookingResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        rlLoader.visibility=View.GONE

                        if (response.body()!!.success.equals("true", true)) {
                            SharedPreferenceUtils.getInstance(this@Payment_Activity)?.setStringValue(ConstantUtils.BOOKING_ID,"")
                            SharedPreferenceUtils.getInstance(this@Payment_Activity)?.setBoolanValue(ConstantUtils.IS_PAYMENT,false)
                            isCompleted=true
                            val dialog = Dialog(this@Payment_Activity)
                            dialog.getWindow()!!
                                .setLayout(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                );
                            lateinit var cvOk: CardView
                            dialog.setCancelable(true)
                            dialog.setContentView(R.layout.payment_popup)
                            cvOk = dialog.findViewById(R.id.cvOk)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            dialog.show()

//        dialog.window?.setLayout(1050, 1100)
                            cvOk.setOnClickListener {
                              onBackPressed()
                            }


                        } else {
                            Toast.makeText(
                                this@Payment_Activity,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        rlLoader.visibility=View.GONE
                        Toast.makeText(
                            this@Payment_Activity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    rlLoader.visibility=View.GONE
                }
            }

            override fun onFailure(call: Call<CompleteBookingResponse>, t: Throwable) {
                rlLoader.visibility=View.GONE
                Toast.makeText(this@Payment_Activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        if(isCompleted){
            var intent = Intent(this@Payment_Activity, Map_DashBoard::class.java)
            intent.putExtra("status", "")
            startActivity(intent)
            finishAffinity()
        }else{
            finishAffinity()
        }
    }

}