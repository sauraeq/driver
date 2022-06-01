package com.abc.taxidriver.Settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abc.taxidriver.HelpandSupport.Help_Support
import com.abc.taxidriver.Privacy_Policy.Privacy_Policy
import com.abc.taxidriver.R
import com.abc.taxidriver.Terms_Condition.TermsCondition
import kotlinx.android.synthetic.main.activity_help_support.*
import kotlinx.android.synthetic.main.activity_setting_main.*

class SettingMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_main)
//        var tvTitle = findViewById<TextView>(R.id.tvTitle)
//        var rlBack = findViewById<RelativeLayout>(R.id.rlBack)
//        var rlTerms = findViewById<RelativeLayout>(R.id.rlTerms)
//        var rl_privacy = findViewById<RelativeLayout>(R.id.rl_privacy)
//        var rlHelp = findViewById<RelativeLayout>(R.id.rlHelp)


        tvTitle.setText("Settings")

        rlTerms.setOnClickListener {
            var intent = Intent(this, TermsCondition::class.java)
            startActivity(intent)
        }

        rl_privacy.setOnClickListener {
            var intent = Intent(this, Privacy_Policy::class.java)
            startActivity(intent)
        }

        rlHelp.setOnClickListener {
            var intent = Intent(this, Help_Support::class.java)
            startActivity(intent)
        }

        rlBack.setOnClickListener {
            onBackPressed()
        }
    }

}