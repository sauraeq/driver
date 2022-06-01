package com.abc.taxidriver.YourEarning

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.API.APIUtils
import com.abc.taxidriver.Dashboard.Model.TodayEarningResponse
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_help_support.*
import kotlinx.android.synthetic.main.activity_your_earning.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.HashMap

class YourEarning : AppCompatActivity() {

    lateinit var adapter: EarningAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_earning)
        llDetail.visibility= View.GONE

        tvTitle.setText("Your Earning")
        onclick()

        TodayEarning()
        WeeklyEarning()

        rvEarning.layoutManager = LinearLayoutManager(this)
        rvEarning.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }


    private fun TodayEarning() {


        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        var request = HashMap<String, String>()

        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }
     //   request.put("driver_id", "1")

        var call = APIUtils.getServiceAPI()!!.TodayHistory(request)

        call.enqueue(object : Callback<TodayEarningResponse> {
            override fun onResponse(
                call: Call<TodayEarningResponse>,
                response: Response<TodayEarningResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()


                        if (response.body()!!.success.equals("true", true)) {
                            tvNorecordFound.visibility= View.GONE
                            llDetail.visibility= View.VISIBLE



                            var list = ArrayList<TodayEarningResponse.Data>()
                            list.addAll(response.body()!!.data)

                            // Log.d("size...","${list.size}")
                            if (list != null) {

                                rvEarning.layoutManager =
                                    LinearLayoutManager(
                                        this@YourEarning,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                rvEarning.adapter =
                                    EarningAdapter(this@YourEarning, list)


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

            override fun onFailure(call: Call<TodayEarningResponse>, t: Throwable) {
                dialog.dismiss()
                tvNorecordFound.visibility= View.VISIBLE
            }
        })


    }

    private fun WeeklyEarning() {


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

        var call = APIUtils.getServiceAPI()!!.AllHistory(request)

        call.enqueue(object : Callback<TodayEarningResponse> {
            override fun onResponse(
                call: Call<TodayEarningResponse>,
                response: Response<TodayEarningResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()


                        if (response.body()!!.success.equals("true", true)) {

                            tvTotalride.setText(response.body()!!.dataall[0].total_ride)
                            tvTotalamount.setText("$"+response.body()!!.dataall[0].amount)
                            tvOnlinehours.setText(response.body()!!.dataall[0].time)
                            tvTotalearning.setText("$"+response.body()!!.dataall[0].amount)




                            var list = ArrayList<TodayEarningResponse.Data>()
                            list.addAll(response.body()!!.data)

                            // Log.d("size...","${list.size}")
                            if (list != null) {

                                rvEarning.layoutManager =
                                    LinearLayoutManager(
                                        this@YourEarning,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )

                                //  recycler_View.adapter = context?.let { StoneAdapter(it, list) }
                                rvEarning.adapter =
                                    EarningAdapter(this@YourEarning, list)


                            }


                            Toast.makeText(
                                this@YourEarning,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(
                                this@YourEarning,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            this@YourEarning,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<TodayEarningResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(this@YourEarning, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }




    private fun onclick() {

        rlBack.setOnClickListener {
            onBackPressed()
        }

        tvToday.setOnClickListener {
            view_today.setBackgroundColor(Color.BLACK);
            view_weekly.setBackgroundColor(Color.parseColor("#AEAEAE"));
            TodayEarning()
        }

        tvWeekly.setOnClickListener {
            view_weekly.setBackgroundColor(Color.BLACK);
            view_today.setBackgroundColor(Color.parseColor("#AEAEAE"));
//                tvWeekly.setOnClickListener() {
//                    view_weekly.setTextColor(resources.getColor(R.color.gray4))
//                        tvToday.setTextColor(resources.getColor(R.color.black))

            WeeklyEarning()

        }
    }


}


