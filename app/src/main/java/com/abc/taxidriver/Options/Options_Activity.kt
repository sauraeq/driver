package com.abc.taxidriver.Options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.abc.taxidriver.R
import com.abc.taxidriver.Signup.SignupActivity
import kotlinx.android.synthetic.main.activity_options.*

class Options_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

//        var tv_login=findViewById<TextView>(R.id.tv_login)

        tv_login.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }

        var get_startedd=findViewById<RelativeLayout>(R.id.get_started)

        get_startedd.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }
    }
}