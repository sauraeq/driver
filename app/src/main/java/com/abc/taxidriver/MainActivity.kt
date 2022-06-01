package com.abc.taxidriver

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tvTerms)
        val text = "By clicking start, you agree to our Terms &amp; Conditions "
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 37, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView!!.text = spannableString
    }
}

