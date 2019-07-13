package com.muthu.googlesmsapi

import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.muthu.googlesmsapi.helper.AppSignatureHashHelper
import com.muthu.googlesmsapi.util.SMSReceiver

import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), SMSReceiver.OTPReceiveListener {

    private lateinit var smsReceiver: SMSReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        getHashKey()

        startSMSListener()
    }


    /**
     * listen sms receiver
     */
    private fun startSMSListener() {
        try {
            smsReceiver = SMSReceiver()
            smsReceiver.setOTPListener(this)

            val intentFilter = IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter)

            val client = SmsRetriever.getClient(this)

            val task = client.startSmsRetriever();
            task.addOnSuccessListener {

            }

            task.addOnFailureListener {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    /**
     * to get your hash key
     */
    private fun getHashKey() {
        Log.d("APP_HASH_KEY ", "Apps Hash Key: " + AppSignatureHashHelper(this).appSignatures[0])
    }


    override fun onOTPReceived(otp: String) {
        Log.d("APP_OTP_RECEIVED ", "OTP===> $otp ")
    }

    override fun onOTPTimeOut() {
        Log.d("APP_OTP_TIME_OUT ", "TIME OUT")
    }

    override fun onOTPReceivedError(error: String) {
        Log.d("APP_OTP_ERROR ", error)
    }


    override fun onDestroy() {
        super.onDestroy()

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver)
        }

    }

}
