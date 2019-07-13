package com.muthu.googlesmsapi.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSReceiver : BroadcastReceiver() {

    private var otpListener: OTPReceiveListener? = null

    /**
     * @param otpListener
     */
    fun setOTPListener(otpListener: OTPReceiveListener) {
        this.otpListener = otpListener
    }


    /**
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    //This is the full message
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String

                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/
                    ;
                    //Extract the OTP code and send to the listener

                    if (otpListener != null) {
                        otpListener?.onOTPReceived(parseCode(message))
                    }
                }
                CommonStatusCodes.TIMEOUT ->
                    // Waiting for SMS timed out (5 minutes)
                    if (otpListener != null) {
                        otpListener?.onOTPTimeOut()
                    }

                CommonStatusCodes.API_NOT_CONNECTED ->

                    if (otpListener != null) {
                        otpListener?.onOTPReceivedError("API NOT CONNECTED")
                    }

                CommonStatusCodes.NETWORK_ERROR ->

                    if (otpListener != null) {
                        otpListener?.onOTPReceivedError("NETWORK ERROR")
                    }

                CommonStatusCodes.ERROR ->

                    if (otpListener != null) {
                        otpListener?.onOTPReceivedError("SOME THING WENT WRONG")
                    }
            }
        }
    }

    private fun parseCode(msg: String): String {
        var upToNCharacters: String? = null
        try {
            var message = msg
            var part = arrayOf<String>()
            try {
                part = message.split("(?<=\\D)(?=\\d)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            message = message.replace("FLAG", "")

            upToNCharacters = part[1].substring(0, Math.min(part[1].length, 6))
            Log.i("---->", upToNCharacters)
        } catch (e: Exception) {
        }
        return upToNCharacters!!
    }



    /**
     * interface
     */
    interface OTPReceiveListener {

        fun onOTPReceived(otp: String)

        fun onOTPTimeOut()

        fun onOTPReceivedError(error: String)
    }
}
