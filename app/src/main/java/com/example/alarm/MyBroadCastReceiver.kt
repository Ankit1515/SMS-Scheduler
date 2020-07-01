package com.example.alarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadCastReceiver : BroadcastReceiver() {

    var TAG = null

    override fun onReceive(p0: Context?, p1: Intent?) {

        Log.d(TAG, "broadcast")

        var i = Intent(p0, MyService::class.java)

        p0?.startService(i)


    }
}