package com.abc.taxidriver.Utils

import android.content.Context
import android.util.Patterns

class StringUtil {
    companion object {
        fun capString(str: String): String {
            try {
                return str[0].uppercaseChar() + str.substring(1).lowercase()
            } catch (e: Exception) {
                return ""
            }


        }

        fun getDriverId(context: Context): String {
            var userId = SharedPreferenceUtils.getInstance(context)
                ?.getStringValue(ConstantUtils.DRIVER_ID, "")
            return userId!!

        }
        fun isEmailValid(email: String?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }
}