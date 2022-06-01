package com.abc.taxidriver.Dashboard

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.API.APIUtils
import com.abc.taxidriver.Chat_Screen.Chat_Screen_Activity
import com.abc.taxidriver.Dashboard.CancelReasonModels.CancelReasonResponse
import com.abc.taxidriver.Dashboard.Model.*
import com.abc.taxidriver.Notification.Notification_Activity
import com.abc.taxidriver.Payment.Payment_Activity
import com.abc.taxidriver.Profile_Overview.Profile_Overview
import com.abc.taxidriver.R
import com.abc.taxidriver.Services.ServiceTaskNew
import com.abc.taxidriver.Settings.SettingMain
import com.abc.taxidriver.Signup.SignupActivity
import com.abc.taxidriver.Utils.*
import com.abc.taxidriver.YourEarning.YourEarning
import com.abc.taxidriver.databinding.ActivityMapDashBoardBinding
import com.example.customnavigationdrawerexample.ClickListener
import com.example.customnavigationdrawerexample.RecyclerTouchListener
import com.geelong.user.Model.NavigationItemModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_map_dash_board.*
import kotlinx.android.synthetic.main.complete_ride.*
import kotlinx.android.synthetic.main.offline_footer.*
import kotlinx.android.synthetic.main.onlinescreen_footer.*
import kotlinx.android.synthetic.main.ridecancel_footer.*
import kotlinx.android.synthetic.main.sidebar_header.*
import kotlinx.android.synthetic.main.start_ride.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Map_DashBoard : AppCompatActivity(), OnMapReadyCallback {

    var profile_profile: String = ""
    var custMobileNo: String = ""
    var bookingotp: String = ""
    var custName: String = ""
    var pickupAddress: String = ""
    var dropAddress: String = ""
    var bookingPrice: String = ""
    var bookingId = ""
    var cancelReason = ""


    private lateinit var mMap: GoogleMap
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMapDashBoardBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var ivMenu: ImageView


    lateinit var currentLocation: Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var REQUEST_CODE: Int = 101

    private var rlOnline: RelativeLayout? = null
    private var rl_Ofline: RelativeLayout? = null
    private var rl_ridecancel: RelativeLayout? = null

    /*    private var rl_confirm_cancel: RelativeLayout? = null*/
    private var rl_arrived: RelativeLayout? = null
    private var rlCompleteRide: RelativeLayout? = null
    private var navigation_rv: RecyclerView? = null
    private lateinit var adapter: NavigationRVAdapter

    private var items = arrayListOf(
        NavigationItemModel(R.drawable.profile_icon, "Profile Overview"),
        NavigationItemModel(R.drawable.your_earning_icon, "Your Earnings"),
//        NavigationItemModel(R.drawable.bank_details_icon, "Bank Details"),
        NavigationItemModel(R.drawable.setting_icon, "Settings"),
        NavigationItemModel(R.drawable.notification_icon, "Notification"),
        NavigationItemModel(R.drawable.logout_icon, "Logout")
        // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )

    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.profile_icon_white, "Profile Overview"),
        NavigationItemModel(R.drawable.your_earning_white, "Your Earnings"),
//        NavigationItemModel(R.drawable.bank_details_white, "Bank Details"),
        NavigationItemModel(R.drawable.settings_icon_white, "Notification"),
        NavigationItemModel(R.drawable.notification_icon_white, "Settings"),
        NavigationItemModel(R.drawable.logout_white, "Logout")
        // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        PermissionUtils.checkLocationPermissions(this)
        var serviceIntent = Intent(this, ServiceTaskNew::class.java)
        startService(serviceIntent)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        navigation_rv = findViewById(R.id.navigation_rv) as RecyclerView
        navigation_rv?.layoutManager = LinearLayoutManager(this)
        navigation_rv?.setHasFixedSize(true)
        drawerLayout = findViewById(R.id.drawerLayout)
        ivMenu = findViewById(R.id.ivMenu)
        supportActionBar?.hide()
        llCancelReason.removeAllViews()

        click()
        onClick()
        fetchLocation()
        offlinedetails()
        getCancelReasons()






        if (NetworkUtils.checkInternetConnection(this)) {


        }
        init()
        if (!SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.BOOKING_ID, "")?.isEmpty()!!)
        {
            rl_arrived!!.visibility = View.VISIBLE
            rlOnline!!.visibility = View.GONE
            bookingotp = SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                ?.getStringValue(ConstantUtils.BOOKING_OTP, "")!!

            custMobileNo = SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                ?.getStringValue(ConstantUtils.CUST_MOBILENO, "")!!
          //  etOTP.setText(bookingotp)
            tvCustName.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.CUST_NAME, "")
            )

            tvCustName2.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.CUST_NAME, "")
            )
            tvCustPickAddress.setText(
                "Pick Up: " + SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.PICK_LOCATION, "")
            )
            tvCustPickAddress2.setText(
                "Pick Up: " + SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.PICK_LOCATION, "")
            )
            tvBookingDist.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_DIS, "") + "Km"
            )
           /* tvBookingDist2.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_DIS, "") + "Km"
            )*/
            tvBookingTime.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_Time, "")
            )
           /* tvBookingTime2.setText(
                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_Time, "")
            )*/
            tvBookingPrice.setText(
                "$" + SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_AMOUNT, "")
            )
           /* tvBookingPrice2.setText(
                "$" + SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                    ?.getStringValue(ConstantUtils.BOOKING_AMOUNT, "")
            )*/
            if (SharedPreferenceUtils.getInstance(this)
                    ?.getBoolanValue(ConstantUtils.IS_PAYMENT, false)!!
            ) {
                var intent = Intent(this, Payment_Activity::class.java)
                startActivity(intent)
            }
        }
        else  if (SharedPreferenceUtils.getInstance(this)?.getBoolanValue(ConstantUtils.IS_ONLINE, false)!!)
        {
            rlOnline!!.visibility = View.VISIBLE
            rl_Ofline!!.visibility = View.GONE
            rlOnlineCustDetail.visibility=View.GONE
            callupdateProfile()
        }

    }

    override fun onStart() {
        super.onStart()
        checkGPSLocationStatus()

    }
    fun checkGPSLocationStatus() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }
    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Your GPS seems to be disabled, Please enable it.")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alert = builder.create()
        alert.show()
    }


    private fun init() {
        profile_profile =
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PROFILE_PHOTO, "")
                .toString()
        try {
            var picasso = Picasso.get()
            picasso.load(profile_profile).placeholder(R.drawable.default_profile).into(ivProfilePic)
        } catch (e: java.lang.Exception) {

        }
        tvName.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.NAME, ""
            )?.let { StringUtil.capString(it) }
        )
        tvNum.setText(
            SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.PHONE_NO, ""
            )
        )
        try {
            val picasso = Picasso.get()
            picasso.load(profile_profile).placeholder(R.drawable.default_profile).into(ivProfilePic)
        } catch (e: java.lang.Exception) {

        }


        try {
            val status = intent.getStringExtra("status")
            if (status.equals("Payment")) {

                rlOnline!!.visibility = View.VISIBLE
                rl_Ofline!!.visibility = View.GONE
            } else {

            }
        } catch (e: Exception) {

        }


        rlOnline = findViewById(R.id.rlOnline) as RelativeLayout
        rl_Ofline = findViewById(R.id.rl_Ofline) as RelativeLayout
        rl_ridecancel = findViewById(R.id.rl_ridecancel) as RelativeLayout
//        rl_confirm_cancel = findViewById(R.id.rl_confirm_cancel) as RelativeLayout
        rl_arrived = findViewById(R.id.rl_arrived) as RelativeLayout
        rlCompleteRide = findViewById(R.id.rlCompleteRide) as RelativeLayout


        ///-------------Navigation Drawer---------///

        navigation_rv?.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        val intent = Intent(this@Map_DashBoard, Profile_Overview::class.java)
                        startActivity(intent)
                    }

                    1 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        val intent = Intent(this@Map_DashBoard, YourEarning::class.java)
                        startActivity(intent)
                    }

                    2 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        var intent = Intent(this@Map_DashBoard, Notification_Activity::class.java)
                        startActivity(intent)
                        closeDrower()
                    }

                    3 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        var intent =
                            Intent(this@Map_DashBoard, SettingMain::class.java)
                        startActivity(intent)
                        closeDrower()
                    }

                    4 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                         callLocationApi("1",true)
                        closeDrower()
                    }

                    6 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)

                    }
                }

                updateAdapter(position)
                if (position != 5 && position != 3) {

                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))

        updateAdapter(0)

    }

    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items, items1, highlightItemPos)
        navigation_rv?.adapter = adapter
        adapter?.notifyDataSetChanged()

    }

    private fun click() {

        ivCloseMenu!!.setOnClickListener() {
            closeDrower()
        }

        buttonCompleteRide.setOnClickListener() {
            var intent = Intent(this, Payment_Activity::class.java)
            /* intent.putExtra("bookingId", bookingId)
             intent.putExtra("custName", custName)
             intent.putExtra("dropAddress", dropAddress)
             intent.putExtra("pickupAddress", pickupAddress)
             intent.putExtra("bookingPrice", bookingPrice)*/
            startActivity(intent)
        }

        ivCall!!.setOnClickListener() {

            if (!custMobileNo.isNullOrEmpty()) {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" + custMobileNo) //change the number
                startActivity(callIntent)
            }

        }

        ivMsg!!.setOnClickListener() {
            var intent = Intent(this, Chat_Screen_Activity::class.java)
            startActivity(intent)
        }
//
//        button_complete!!.setOnClickListener() {
//            var intent = Intent(this, Payment_Activity::class.java)
//            startActivity(intent)
//        }

        ///-------------visibility---------///

        iv_go.setOnClickListener() {


            //callupdateProfile()
            callLocationApi("2",false)
        }

        iv_stop.setOnClickListener() {


            callLocationApi("1",false)
        }

/*        buttonCancelRide.setOnClickListener() {

            rl_Ofline!!.visibility = View.VISIBLE
            rl_ridecancel!!.visibility = View.GONE
        }

        button_yes_cancel.setOnClickListener() {

            rlOnline!!.visibility = View.VISIBLE
            rl_confirm_cancel!!.visibility = View.GONE
        }

        button_no.setOnClickListener() {

            rlOnline!!.visibility = View.VISIBLE
            rl_confirm_cancel!!.visibility = View.GONE
        }*/

        button_accept.setOnClickListener() {

            acceptrideApi()
        }

        buttonStartRide.setOnClickListener() {
            Log.d("bookingotp", bookingotp + "  " + etOTP.text.toString())
            if (etOTP.text.toString().isEmpty()) {
                ToastUtil.toast_Long(this, resources.getString(R.string.enterotp))
            } else if (!bookingotp.equals(etOTP.text.toString())) {
                ToastUtil.toast_Long(this, resources.getString(R.string.plzvalidotp))
            } else {
                rlCompleteRide!!.visibility = View.VISIBLE
                rl_arrived!!.visibility = View.GONE
            }


        }





        button_cancel.setOnClickListener() {

            rl_ridecancel!!.visibility = View.VISIBLE
            rlOnline!!.visibility = View.GONE

        }

        buttonCancelRide.setOnClickListener {
            if (cancelReason.isEmpty()) {
                ToastUtil.toast_Long(this, resources.getString(R.string.plzreason))
            } else {
                /*  rl_Ofline!!.visibility = View.VISIBLE
                  rl_ridecancel!!.visibility = View.GONE*/
                cancelrideAPi()
            }

        }

        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }

    private fun cancelrideAPi() {
        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        var request = HashMap<String, String>()
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }
        request.put("booking_id", bookingId)
        request.put("description", cancelReason)

        var call = APIUtils.getServiceAPI()!!.rejectTrip2(request)

        call.enqueue(object : Callback<RejectrideResponse> {
            override fun onResponse(
                call: Call<RejectrideResponse>,
                response: Response<RejectrideResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()


                        if (response.body()!!.success.equals("true", true)) {
                            SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                ?.setStringValue(ConstantUtils.BOOKING_ID, "")
                            var intent = Intent(this@Map_DashBoard, Map_DashBoard::class.java)
                            intent.putExtra("status", "")
                            startActivity(intent)
                            finishAffinity()

                            Toast.makeText(
                                this@Map_DashBoard,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(
                                this@Map_DashBoard,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            this@Map_DashBoard,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RejectrideResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(this@Map_DashBoard, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun acceptrideApi() {

        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        var request = HashMap<String, String>()

        request.put("booking_id", bookingId)
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }

        var call = APIUtils.getServiceAPI()!!.accepttrip(request)

        call.enqueue(object : Callback<AcceptrideResponse> {
            override fun onResponse(
                call: Call<AcceptrideResponse>,
                response: Response<AcceptrideResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()

                        if (response.body()!!.success.equals("true", true)) {

                            rl_arrived!!.visibility = View.VISIBLE
                            rlOnline!!.visibility = View.GONE
                            SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                ?.setStringValue(ConstantUtils.BOOKING_ID, bookingId)

                        } else {
                            Toast.makeText(
                                this@Map_DashBoard,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            this@Map_DashBoard,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AcceptrideResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(this@Map_DashBoard, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun callLocationApi(status: String,isLogout:Boolean) {


        val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        var request = HashMap<String, String>()

        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.CURENT_LAT, ""
        )?.let {
            request.put(
                "latitude", it
            )

        }
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.CURENT_LNG, ""
        )?.let {
            request.put(
                "longitude", it
            )

        }
        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.CURRENTLOCATION, ""
        )?.let {
            request.put(
                "location_name", it
            )

        }
        request.put("status", status)


        var call = APIUtils.getServiceAPI()!!.sendDriverLocation(request)

        call.enqueue(object : Callback<DriverLocationResponse> {
            override fun onResponse(
                call: Call<DriverLocationResponse>,
                response: Response<DriverLocationResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        dialog.dismiss()

                        if (response.body()!!.success.equals("true", true)) {
                            if(isLogout){
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)?.clear()
                                var intent = Intent(this@Map_DashBoard, SignupActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(
                                    this@Map_DashBoard,
                                    response.body()!!.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            if (status.equals("2")) {
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)?.setBoolanValue(ConstantUtils.IS_ONLINE,true)

                                rlOnline!!.visibility = View.VISIBLE
                                rl_Ofline!!.visibility = View.GONE
                                rlOnlineCustDetail.visibility=View.GONE
                                callupdateProfile()
                            }else{
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)?.setBoolanValue(ConstantUtils.IS_ONLINE,false)
                                rl_Ofline!!.visibility = View.VISIBLE
                                rlOnline!!.visibility = View.GONE

                            }



                        } else {
                            Toast.makeText(
                                this@Map_DashBoard,
                                response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            this@Map_DashBoard,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<DriverLocationResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(this@Map_DashBoard, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun closeDrower() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener(OnSuccessListener<Location> { location ->
            if (location != null) {
                currentLocation = location
                /* Toast.makeText(
                     applicationContext,
                     currentLocation.latitude.toString() + "" + currentLocation.longitude,
                     Toast.LENGTH_SHORT
                 ).show()*/
                val supportMapFragment =
                    (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    fun onClick() {

        rlRider.setOnClickListener() {

            cancelRide()
            ivRider.setImageDrawable(getResources().getDrawable(R.drawable.black_circleoption));
            ivWrong.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivCharge.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivLocation.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));

        }

        rlWrong.setOnClickListener() {
            //  ivRider.setBackgroundResource(getResources().getDrawable(R.drawable.black_circleoption))
            ivRider.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivWrong.setImageDrawable(getResources().getDrawable(R.drawable.black_circleoption));
            ivCharge.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivLocation.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
        }

        rlCharge.setOnClickListener() {
            //  ivRider.setBackgroundResource(getResources().getDrawable(R.drawable.black_circleoption))
            ivRider.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivWrong.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivCharge.setImageDrawable(getResources().getDrawable(R.drawable.black_circleoption));
            ivLocation.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
        }

        rlLocation.setOnClickListener() {
            //  ivRider.setBackgroundResource(getResources().getDrawable(R.drawable.black_circleoption))
            ivRider.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivWrong.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivCharge.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
            ivLocation.setImageDrawable(getResources().getDrawable(R.drawable.black_circleoption));
        }

    }

    private fun cancelRide() {

    }

    override fun onBackPressed() {

       /* if (rlOnline!!.visibility == View.VISIBLE) {
            rlOnline!!.visibility = View.GONE
            rl_Ofline!!.visibility = View.VISIBLE

        } else*/ if (rl_ridecancel!!.visibility == View.VISIBLE) {
            rl_ridecancel!!.visibility = View.GONE
            rlOnline!!.visibility = View.VISIBLE

        } else if (rl_arrived!!.visibility == View.VISIBLE) {
          /*  rl_arrived!!.visibility = View.GONE
            rlOnline!!.visibility = View.VISIBLE*/
            super.onBackPressed()

        } else if (rlCompleteRide!!.visibility == View.VISIBLE) {
            rlCompleteRide!!.visibility = View.GONE
            rl_arrived!!.visibility = View.VISIBLE

        } else {
            super.onBackPressed()
        }

        /* if (doubleBackToExitPressedOnce) {
             finishAffinity();
             return
         }

         this.doubleBackToExitPressedOnce = true
         Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

         Handler(Looper.getMainLooper()).postDelayed(Runnable {
             doubleBackToExitPressedOnce = false
         }, 2000)*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (::currentLocation.isInitialized) {
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("I am here!")
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
            googleMap.addMarker(markerOptions)

            if (::mMap.isInitialized) {

                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            currentLocation.latitude,
                            currentLocation.longitude
                        ), 17.0f
                    )
                )
            }
        }
    }

    private fun callupdateProfile() {

        rlLoaderDashboard.visibility = View.VISIBLE
        val request = HashMap<String, String>()

        SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DRIVER_ID, "")
            ?.let { request.put("driver_id", it) }
        var call: Call<DriverTripResponse> =
            APIUtils.getServiceAPI()!!.driverTripApi(request)

        call.enqueue(object : Callback<DriverTripResponse> {
            override fun onResponse(
                call: Call<DriverTripResponse>,
                response: Response<DriverTripResponse>
            ) {
                try {
                    if (response.body()!!.success.equals("true")) {
                        if (response.body()!!.data != null) {
                            if (response.body()!!.data.size > 0) {
                                rlOnlineCustDetail.visibility=View.VISIBLE

                                bookingId = response.body()!!.data[0].id

                                tvAmount.setText("$ " + response.body()!!.data[0].amount)
                                tvBookingPrice.setText("$ " + response.body()!!.data[0].amount)
                                tvBookingPrice2.setText("$ " + response.body()!!.data[0].amount)
                                tvTime.setText(response.body()!!.data[0].time+" Min.")
                                tvBookingTime.setText(response.body()!!.data[0].time)
                                tvBookingTime2.setText(response.body()!!.data[0].time)
                                tvDistance.setText(response.body()!!.data[0].distance + " Km")
                                tvBookingDist.setText(response.body()!!.data[0].distance + " Km")
                                tvBookingDist2.setText(response.body()!!.data[0].distance + " Km")
                                tvpickup_address.setText(response.body()!!.data[0].pickup_address)
                                tvCustPickAddress.setText("Pick Up: " + response.body()!!.data[0].pickup_address)
                                tvCustPickAddress2.setText("Pick Up: " + response.body()!!.data[0].pickup_address)
                                tvdrop_address.setText(response.body()!!.data[0].drop_address)
                                tvCustName.setText(response.body()!!.data[0].user_name)
                                tvCustName2.setText(response.body()!!.data[0].user_name)

                                custMobileNo = response.body()!!.data[0].user_phone
                                bookingotp = response.body()!!.data[0].otp
                               // etOTP.setText(bookingotp)
                                custName = response.body()!!.data[0].user_name
                                pickupAddress = response.body()!!.data[0].pickup_address
                                dropAddress = response.body()!!.data[0].drop_address
                                bookingPrice = response.body()!!.data[0].amount

                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.CUST_NAME, custName)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.PICK_LOCATION, pickupAddress)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.DROP_LOCATION, dropAddress)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.BOOKING_AMOUNT, bookingPrice)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.CUST_MOBILENO, custMobileNo)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(ConstantUtils.BOOKING_OTP, bookingotp)
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(
                                        ConstantUtils.BOOKING_DIS,
                                        response.body()!!.data[0].distance
                                    )
                                SharedPreferenceUtils.getInstance(this@Map_DashBoard)
                                    ?.setStringValue(
                                        ConstantUtils.BOOKING_Time,
                                        response.body()!!.data[0].time
                                    )

                                try {
                                    var picasso = Picasso.get()
                                    picasso.load(response.body()!!.data[0].user_photo).placeholder(R.drawable.default_profile).into(ivCust)
                                    picasso.load(response.body()!!.data[0].user_photo).placeholder(R.drawable.default_profile).into(ivCust2)

                                } catch (e: java.lang.Exception) {

                                }
                            }
                        }


                        //-------null condition to be implemented ---//
                    } else {
                        Toast.makeText(
                            this@Map_DashBoard, response.body()!!.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    rlLoaderDashboard.visibility = View.GONE
                } catch (e: Exception) {
                    Log.e("nitish", e.toString())

                    Toast.makeText(this@Map_DashBoard, e.toString(), Toast.LENGTH_LONG).show()
                    rlLoaderDashboard.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<DriverTripResponse>, t: Throwable) {
                Log.e("nitish", t.message.toString())
                rlLoaderDashboard.visibility = View.GONE
                Toast.makeText(this@Map_DashBoard, t.toString(), Toast.LENGTH_LONG).show()

            }

        })

    }

    private fun offlinedetails() {

        rlLoaderDashboard.visibility = View.VISIBLE
        val request = HashMap<String, String>()

        SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils.DRIVER_ID, ""
        )?.let {
            request.put(
                "driver_id", it
            )

        }
        var call: Call<DriverOfflineDetailResponse> =
            APIUtils.getServiceAPI()!!.offlinedetailsApi(request)

        call.enqueue(object : Callback<DriverOfflineDetailResponse> {
            override fun onResponse(
                call: Call<DriverOfflineDetailResponse>,
                response: Response<DriverOfflineDetailResponse>
            ) {
                try {
                    if (response.body()!!.success.equals("true")) {

                        tvAcp.setText(response.body()!!.data.acp + " %")
                        tvRating.setText(response.body()!!.data.rating)
                        tvCancel.setText(response.body()!!.data.cancelation + " %")

                        //-------null condition to be implemented ---//

                    } else {
                        Toast.makeText(
                            this@Map_DashBoard, response.body()!!.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    rlLoaderDashboard.visibility = View.GONE
                } catch (e: Exception) {
                    Log.e("nitish", e.toString())

                    Toast.makeText(this@Map_DashBoard, e.toString(), Toast.LENGTH_LONG).show()
                    rlLoaderDashboard.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<DriverOfflineDetailResponse>, t: Throwable) {
                Log.e("nitish", t.message.toString())
                rlLoaderDashboard.visibility = View.GONE
                Toast.makeText(this@Map_DashBoard, t.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getCancelReasons() {


        var call = APIUtils.getServiceAPI()!!.getCancelReason()

        call.enqueue(object : Callback<CancelReasonResponse> {
            override fun onResponse(
                call: Call<CancelReasonResponse>,
                response: Response<CancelReasonResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        if (response.body()!!.success.equals("true", true)) {
                            if (response.body()!!.data != null) {
                                if (response.body()!!.data.size > 0) {
                                    var selectList: ArrayList<ImageView>
                                    selectList = ArrayList()
                                    for (data1 in response.body()!!.data) {
                                        val inflater =
                                            getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                        var view: View =
                                            inflater.inflate(R.layout.cancel_reason_row, null)
                                        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
                                        var ivSelect: ImageView = view.findViewById(R.id.ivSelect)
                                        var rlReasons: RelativeLayout =
                                            view.findViewById(R.id.rlReasons)
                                        tvTitle.setText(data1.description)
                                        selectList.add(ivSelect)
                                        rlReasons.setOnClickListener {
                                            for (image in selectList) {
                                                image.setImageDrawable(getResources().getDrawable(R.drawable.white_circleoption));
                                            }
                                            ivSelect.setImageDrawable(getResources().getDrawable(R.drawable.black_circleoption));
                                            cancelReason = data1.description
                                        }
                                        llCancelReason.addView(view)

                                    }


                                }
                            }

                        } else {


                        }
                    } else {

                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<CancelReasonResponse>, t: Throwable) {

            }
        })

    }


}




