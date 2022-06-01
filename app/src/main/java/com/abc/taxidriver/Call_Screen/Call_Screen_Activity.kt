package com.abc.taxidriver.Call_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.abc.taxidriver.Dashboard.Map_DashBoard
import com.abc.taxidriver.R
import kotlinx.android.synthetic.main.activity_call_screen.*

class Call_Screen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_screen)

//        var phone_cut = findViewById<ImageView>(R.id.phone_cut)

        phone_cut.setOnClickListener {
            onBackPressed()
        }
    }
}

