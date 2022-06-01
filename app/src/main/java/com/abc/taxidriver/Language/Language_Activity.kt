package com.abc.taxidriver.Language

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.abc.taxidriver.Options.Options_Activity
import com.abc.taxidriver.R
import com.abc.taxidriver.Utils.ConstantUtils
import com.abc.taxidriver.Utils.SharedPreferenceUtils
import com.abc.taxidriver.databinding.ActivityLanguageBinding
import java.util.HashMap


class Language_Activity : AppCompatActivity() {

     lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_language)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_language)
        binding.rlNext.setOnClickListener {
            var intent = Intent(this, Options_Activity::class.java)
            startActivity(intent)
        }
        onClick()

    }

    fun onClick(){
        binding.rlEnglish.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "en")
            setLanguages()
            binding.ivEng.setImageResource(R.drawable.select)
            Toast.makeText(this, SharedPreferenceUtils.getInstance(this)?.getStringValue(
                ConstantUtils.LANGUAG, "").toString(), Toast.LENGTH_SHORT).show()
        }
        binding.rlGer.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "gf")
            setLanguages()
            binding.ivGer.setImageResource(R.drawable.select)
        }
        binding.rlItalian.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "it")
            setLanguages()
            binding.ivItal.setImageResource(R.drawable.select)
        }
        binding.rlRussian.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "ru")
            setLanguages()
            binding.ivRussian.setImageResource(R.drawable.select)
        }
        binding.rlArabic.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "ar")
            setLanguages()
            binding.ivArab.setImageResource(R.drawable.select)
        }
        binding.rlSpanish.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "sa")
            setLanguages()
            binding.ivSpanish.setImageResource(R.drawable.select)
        }
        binding.rlPolish.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)
                ?.setStringValue(ConstantUtils.LANGUAG, "po")
            setLanguages()
            binding.ivPolish.setImageResource(R.drawable.select)
        }
    }
    fun setLanguages(){
        binding.ivEng.setImageResource(R.drawable.unselect)
        binding.ivGer.setImageResource(R.drawable.unselect)
        binding.ivItal.setImageResource(R.drawable.unselect)
        binding.ivRussian.setImageResource(R.drawable.unselect)
        binding.ivArab.setImageResource(R.drawable.unselect)
        binding.ivSpanish.setImageResource(R.drawable.unselect)
        binding.ivPolish.setImageResource(R.drawable.unselect)
    }

}