package com.example.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val TAG = null
    private val SMS_REQUEST_CODE = 100
    private val CONTACT_REQUEST_CODE = 102
    val context : Context = this
    private var isReached = false
    val REQUEST_CONTACT = 101
    val REQUEST_CONTACT2 = 102
    val REQUEST_CONTACT3 = 103

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)

        //sms permission
        setPermission()
        //contact permission
        setContactPermission()

        if(contact1.getText().toString() == " contact1 ")
        {
            select_phone2.visibility = View.INVISIBLE
            select_phone3.visibility = View.INVISIBLE
            cancle_phone2.visibility = View.INVISIBLE
            cancle_phone3.visibility = View.INVISIBLE
        }

        select_phone1.setOnClickListener {

            fetchPhoneNo()
        }
        select_phone2.setOnClickListener {

            fetchPhoneNo2()
        }
        select_phone3.setOnClickListener {

            fetchPhoneNo3()
        }

        cancle_phone1.setOnClickListener {

            if(contact1.getText().toString() == " contact1 "){}
            else{contact1.setText(" contact1 ")}

        }
        cancle_phone2.setOnClickListener {

            if(contact2.getText().toString() == " contact2 "){}
            else{contact2.setText(" contact2 ")}

        }
        cancle_phone3.setOnClickListener {

            if(contact3.getText().toString() == " contact3 "){}
            else{contact3.setText(" contact3 ")}
        }

        set.setOnClickListener {

            if(contact1.getText().toString() == " contact1 ") {
                //invalid key
                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                builder.setTitle("atleast one contact required")
                builder.setPositiveButton("Ok")
                { dialog, which ->

                    dialog.cancel()

                }
                val dialog = builder.create()
                dialog.show()
            }
            else
            {

                val call1 = contact1.getText().toString()
                //emergency contact to shared preference so we can retrive on MyService
                val value1: String = call1
                val sharedPref1 = getSharedPreferences("emergencyContact1", Context.MODE_PRIVATE)
                val editor1 = sharedPref1.edit()
                editor1.putString("value1", value1)
                editor1.apply()

                val call2 = contact2.getText().toString()
                //emergency contact to shared preference so we can retrive on MyService
                val value2: String = call2
                val sharedPref2 = getSharedPreferences("emergencyContact2", Context.MODE_PRIVATE)
                val editor2 = sharedPref2.edit()
                editor2.putString("value2", value2)
                editor2.apply()

                val call3 = contact3.getText().toString()
                //emergency contact to shared preference so we can retrive on MyService
                val value3: String = call3
                val sharedPref3 = getSharedPreferences("emergencyContact3", Context.MODE_PRIVATE)
                val editor3 = sharedPref3.edit()
                editor3.putString("value3", value3)
                editor3.apply()

                val sms = sms.getText().toString()
                //emergency contact to shared preference so we can retrive on MyService
                val value4: String = sms
                val sharedPref4 = getSharedPreferences("sms", Context.MODE_PRIVATE)
                val editor4 = sharedPref4.edit()
                editor4.putString("value4", value4)
                editor4.apply()

                /*   var sec = SheduleTime.getText().toString().toInt()
                   var i = Intent(applicationContext, MyBroadCastReceiver::class.java)
                   var pi = PendingIntent.getBroadcast(applicationContext, 111, i, 0)
                   var am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                   am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (sec * 60 * 1000), pi)
                   Toast.makeText(this, "Alarm is set for $sec minutes", Toast.LENGTH_SHORT).show()   */


                var am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour())
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute())
                val myIntent = Intent(this@MainActivity, MyBroadCastReceiver::class.java)
                val pi = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, 0)
                am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi)


                Log.d(TAG, "SET")
                super.onBackPressed()

            }

        }

    }


    // sms permission

    // permission for sms
    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            Log.i(TAG, "Permission to sms denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), SMS_REQUEST_CODE)
    }


    // permission for contact
    private fun setContactPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            Log.i(TAG, "Permission to contact denied")
            makeContactRequest()
        }
    }

    private fun makeContactRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), CONTACT_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SMS_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("you have to allow this permission to make scheduled SMS")
                    builder.setTitle("Permission Required")
                    builder.setPositiveButton("OK")
                    { dialog, which ->
                        Log.d(TAG, "Clicked")

                        makeRequest()

                    }
                    builder.setNegativeButton("cancel")
                    { dialog, which ->
                        Log.d(TAG, "Clicked")
                        dialog.cancel()
                    }
                    val dialog = builder.create()
                    dialog.show()

                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
            CONTACT_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("permission required to get contacts")
                    builder.setTitle("Permission Required")
                    builder.setPositiveButton("OK")
                    { dialog, which ->
                        //Log.d(TAG, "Clicked")

                        makeContactRequest()

                    }
                    builder.setNegativeButton("cancel")
                    { dialog, which ->
                       // Log.d(TAG, "Clicked")
                        dialog.cancel()
                    }
                    val dialog = builder.create()
                    dialog.show()

                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }

        }
    }




    //contact book

    private fun fetchPhoneNo() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CONTACT)
    }

    // 2nd contact
    private fun fetchPhoneNo2() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CONTACT2)
    }

    // 3rd contact
    private fun fetchPhoneNo3() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CONTACT3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONTACT && data?.data != null) {
            val contactUri = data.data;
            val crContacts = contentResolver.query(contactUri!!, null, null, null, null);
            crContacts!!.moveToFirst()
            val id = crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                val crPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", arrayOf(id), null)
                crPhones!!.moveToFirst()
                var phoneNo = crPhones!!.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                crPhones.close()
                //Log.d(TAG, ""+phoneNo)
                contact1.setText(phoneNo)
                select_phone2.visibility = View.VISIBLE
                cancle_phone2.visibility = View.VISIBLE
            }
            crContacts.close()
        }
        if(requestCode == REQUEST_CONTACT2 && data?.data != null) {
            val contactUri = data.data;
            val crContacts = contentResolver.query(contactUri!!, null, null, null, null);
            crContacts!!.moveToFirst()
            val id = crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                val crPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", arrayOf(id), null)
                crPhones!!.moveToFirst()
                var phoneNo = crPhones!!.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                crPhones.close()
                //Log.d(TAG, ""+phoneNo)
                contact2.setText(phoneNo)
                select_phone3.visibility = View.VISIBLE
                cancle_phone3.visibility = View.VISIBLE
            }
            crContacts.close()
        }
        if(requestCode == REQUEST_CONTACT3 && data?.data != null) {
            val contactUri = data.data;
            val crContacts = contentResolver.query(contactUri!!, null, null, null, null);
            crContacts!!.moveToFirst()
            val id = crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                val crPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", arrayOf(id), null)
                crPhones!!.moveToFirst()
                var phoneNo = crPhones!!.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                crPhones.close()
                //Log.d(TAG, ""+phoneNo)
                contact3.setText(phoneNo)
            }
            crContacts.close()
        }

    }
}
