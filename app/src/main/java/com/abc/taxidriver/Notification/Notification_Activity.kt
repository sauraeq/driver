package com.abc.taxidriver.Notification

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.abc.taxidriver.Notification.NotificationAdapter

import androidx.recyclerview.widget.RecyclerView
import com.API.APIUtils
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.YourEarning.EarningAdapter
import kotlinx.android.synthetic.main.activity_help_support.*
import kotlinx.android.synthetic.main.activity_notification.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.HashMap

class Notification_Activity : AppCompatActivity() {
    private lateinit var rvNotificaiotn:RecyclerView
    private lateinit var list_notification: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        rvNotificaiotn=findViewById(R.id.rvNotification)
//        var tvTitle = findViewById<TextView>(R.id.tvTitle)
//        var rlBack = findViewById<RelativeLayout>(R.id.rlBack)

        tvTitle.setText("Notification")
        rlBack.setOnClickListener {
            onBackPressed()
        }

        GetnotificationApi()
    }


    private fun GetnotificationApi() {


        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        var request = HashMap<String, String>()

        /*SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }*/

        SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DRIVER_ID,"")
            ?.let {
                request.put("driver_id", it) }

        var call = APIUtils.getServiceAPI()!!.notification(request)

        call.enqueue(object : Callback<notificationResponse> {
            override fun onResponse(
                call: Call<notificationResponse>,
                response: Response<notificationResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()


                        if (response.body()!!.success.equals("true", true)) {



                            var list = ArrayList<notificationResponse.Data>()
                            list.addAll(response.body()!!.data)
                            if(list!=null){
                                if(list.size>0){
                                    tvNorecordFound.visibility= View.GONE



                                    rvNotification.layoutManager =
                                        LinearLayoutManager(
                                            this@Notification_Activity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )

                                    //  recycler_View.adapter = context?.let { StoneAdapter(it, list) }
                                    rvNotification.adapter =
                                        NotificationAdapter(this@Notification_Activity, list)
                                }else{
                                    tvNorecordFound.visibility= View.VISIBLE
                                }
                            }







                        } else {
                            tvNorecordFound.visibility= View.VISIBLE
                        }
                    } else {
                        dialog.dismiss()
                        tvNorecordFound.visibility= View.VISIBLE
                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                    tvNorecordFound.visibility= View.VISIBLE
                }
            }

            override fun onFailure(call: Call<notificationResponse>, t: Throwable) {
                dialog.dismiss()
                tvNorecordFound.visibility= View.VISIBLE
            }
        })


    }

}