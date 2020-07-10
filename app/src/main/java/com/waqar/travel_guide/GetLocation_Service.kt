package com.waqar.travel_guide

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.tramsun.libs.prefcompat.Pref


class GetLocation_Service : Service() {
    private val binder = LocationServiceBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun GenerateNotification(Message: String): Notification? {
        try {
            val builder = NotificationCompat.Builder(this, "com.com.wooride.wooride.com.wooride.wooride")

            builder.setContentTitle(resources.getString(R.string.app_name)).setContentText(Message).setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo))
                .setVibrate(longArrayOf(Notification.DEFAULT_SOUND.toLong())).priority = Notification.PRIORITY_MAX

            val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = resources.getString(R.string.app_name)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("com.com.wooride.wooride.com.wooride.wooride", name, importance)
                channel.description = Message
                manager.createNotificationChannel(channel)
            }
            builder.setAutoCancel(false)
            val notification = builder.build()
            //            manager.notify(0, notification)
            return notification
        } catch (e: Exception) {

            Log.d("Tag", "GenerateNotification Exp:\n" + e.message)


            return null
        }

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //            val notification = getNotification()
            val notification = GenerateNotification("")
            if (notification != null) {
                startForeground(432, notification)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        init()
        startLocationUpdates()
        return Service.START_NOT_STICKY
    }

    lateinit var mContext: Context

    // bunch of location related apis
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null


    private fun init() {
        mContext = this
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult!!.lastLocation
                Log.d("TAG", "On Location Result");
                updateLocationUI()
            }
        }

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener() {
            Log.i(TAG, "Started location updates!");
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback!!, Looper.myLooper())
            updateLocationUI()

        }.addOnFailureListener() { e ->
            val statusCode = (e as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    val errorMessage =
                        "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                    Log.e(TAG, errorMessage)
                    //                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            updateLocationUI()
        }
    }

    companion object {

        private val TAG = "TAG"
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2000
        val LOCATION_UPDATE_LATITUDE: String = "LocationUpdateLatitude"
        val LOCATION_UPDATE_LONGITUDE: String = "LocationUpdateLongitude"

    }

    private fun updateLocationUI() {
        //Application_Class.instance.socketConnection()
        if (mCurrentLocation != null) {
            Log.d("TAG", "Lat: " + mCurrentLocation!!.latitude + ", " + "Lng: " + mCurrentLocation!!.longitude)
            var intent = Intent("LocationUpdates")
            intent.putExtra("Latitude", mCurrentLocation!!.latitude)
            intent.putExtra("Longitude", mCurrentLocation!!.longitude)
            intent.putExtra("Accuracy", mCurrentLocation!!.accuracy)
            intent.putExtra("Bearing", mCurrentLocation!!.bearing)
            //            LocalBgetInstance(mContext).sendBroadcast(intent)
//            intent.action = "com.example.location"
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)

            Pref.putDouble("LocationUpdateLatitude", mCurrentLocation!!.latitude)
            Pref.putDouble("LocationUpdateLongitude", mCurrentLocation!!.longitude)
            Pref.putFloat("LocationUpdateAccuracy", mCurrentLocation!!.accuracy)
            Pref.putFloat("LocationUpdateBearing", mCurrentLocation!!.bearing)
        } else
            Log.d("TAG", "NULL")
    }

    inner class LocationServiceBinder : Binder() {
        val service: GetLocation_Service
            get() = this@GetLocation_Service
    }

}
