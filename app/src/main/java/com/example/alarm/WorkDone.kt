package com.example.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class WorkDone : AppCompatActivity() {

    var TAG = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        //contact 1
        val sharedPreferencess1 = getSharedPreferences("emergencyContact1", Context.MODE_PRIVATE)
        val value1 = sharedPreferencess1.getString("value1", "")
        Log.d(TAG, ""+value1)

        //contact 2
        val sharedPreferencess2 = getSharedPreferences("emergencyContact2", Context.MODE_PRIVATE)
        val value2 = sharedPreferencess2.getString("value2", "")
        Log.d(TAG, ""+value2)

        //contact 3
        val sharedPreferencess3 = getSharedPreferences("emergencyContact3", Context.MODE_PRIVATE)
        val value3 = sharedPreferencess3.getString("value3", "")
        Log.d(TAG, ""+value3)

        //sms
        val sharedPreferencess4 = getSharedPreferences("sms", Context.MODE_PRIVATE)
        val value4 = sharedPreferencess4.getString("value4", "")
        Log.d(TAG, ""+value4)

        // sms 1
        Log.d(TAG, "sms")
        val phoneNumber = ""+value1
        val message = ""+value4
        val smsManager = SmsManager.getDefault()
        val parts = smsManager.divideMessage(message)
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)

        if (value2!!.isEmpty())
        {
            Log.d(TAG, "contact 2 empty")
        }
        else
        {
            // sms 2
            Log.d(TAG, "sms")
            val phoneNumber = ""+value2
            val message = ""+value4
            val smsManager = SmsManager.getDefault()
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
        }

        if (value3!!.isEmpty())
        {
            Log.d(TAG, "contact 2 empty")
        }
        else
        {
            // sms 3
            Log.d(TAG, "sms")
            val phoneNumber = ""+value3
            val message = ""+value4
            val smsManager = SmsManager.getDefault()
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
        }

        onDestroy()

        val intent = Intent(this@WorkDone, MainActivity::class.java)
        startActivity(intent)
    }
}
