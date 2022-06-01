package com.abc.taxidriver.Bankdetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.abc.taxidriver.Profile_Overview.EditProfile
import com.abc.taxidriver.R
import kotlinx.android.synthetic.main.activity_bankdetails.*
import kotlinx.android.synthetic.main.activity_help_support.*

class Bankdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bankdetails)
//        var tvTitle = findViewById<TextView>(R.id.tvTitle)
//        var rlBack = findViewById<RelativeLayout>(R.id.rlBack)


        tvTitle.setText("Bank Details")

        rlBack.setOnClickListener {
            onBackPressed()
        }

//        var llAddAccount = findViewById<LinearLayout>(R.id.llAddAccount)
        llAddAccount.setOnClickListener() {
            showDialog()

        }

    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.getWindow()!!
            .setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );
        lateinit var cvOkay: CardView
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_account_popup)
        cvOkay = dialog.findViewById(R.id.cvOkay)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

//        dialog.window?.setLayout(1050, 1100)
        cvOkay.setOnClickListener {
            dialog.dismiss()
        }
    }
}
