package com.example.alarm


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


class MyService : Service() {

    var TAG = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "service")
        var intent = Intent(applicationContext, WorkDone::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


        return START_STICKY
    }


}