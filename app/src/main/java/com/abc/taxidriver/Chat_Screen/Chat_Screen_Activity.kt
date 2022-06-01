package com.abc.taxidriver.Chat_Screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.abc.taxidriver.R
import kotlinx.android.synthetic.main.header_chat.*

class Chat_Screen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)
//        var rl_back = findViewById<RelativeLayout>(R.id.rl_back)

        rl_back.setOnClickListener {
            onBackPressed()
        }
    }
}