package com.abc.taxidriver.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.abc.taxidriver.Dashboard.Map_DashBoard
import com.abc.taxidriver.Language.Language_Activity
import com.abc.taxidriver.Options.Options_Activity
import com.abc.taxidriver.R
import com.abc.taxidriver.Signup.SignupActivity
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils

@Suppress("DEPRECATION")

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        Handler().postDelayed({
            if (SharedPreferenceUtils.getInstance(this)?.getStringValue(
                    ConstantUtils.IS_LOGIN, ""
                )?.equals("true")!!
            ) {
                val intent = Intent(this, Map_DashBoard::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

//        Handler().postDelayed({
//            val intent = Intent(this, Language_Activity::class.java)
//            startActivity(intent)
//            finish()
        //  }, 3000) // 3000 is the delayed time in milliseconds.
    }
}