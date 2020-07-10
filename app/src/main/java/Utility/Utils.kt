package Utility

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.Context.CONNECTIVITY_SERVICE
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.waqar.travel_guide.GetLocation_Service


fun ViewGroup.inflate(resId: Int): View {
    return LayoutInflater.from(this.context).inflate(resId, this, false)
}

fun GPS_LocationCheck(context: Context)
{
    var manager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        var builder = AlertDialog.Builder(context)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes")
            { dialog: DialogInterface?, which: Int ->
                context.startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            .setNegativeButton("No")
            { dialog, which ->
                dialog.cancel()
            }
        var alert = builder.create()
        alert.show()
    }
}


val serviceConnection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            val name = className.className
//            if (name.endsWith("GetLocation_Service")) {
//                gpsService = (service as GetLocation_Service.LocationServiceBinder).service
//            }
    }

    override fun onServiceDisconnected(className: ComponentName) {
//            if (className.className == "GetLocation_Service") {
//                gpsService = null
//            }
    }
}


fun Activity.startLocationService() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//        printLog("START LOCATION SERVICE ********")
        startService(Intent(this, GetLocation_Service::class.java))
    } else {
//        printLog("START LOCATION SERVICE WITH BIND ******")
        val intent = Intent(this, GetLocation_Service::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}

fun Activity.stopLocationService() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//        printLog("START LOCATION SERVICE ********")
        stopService(Intent(this, GetLocation_Service::class.java))
    } else {
//        printLog("START LOCATION SERVICE WITH BIND ******")
        val intent = Intent(this, GetLocation_Service::class.java)
        stopService(intent)
        unbindService(serviceConnection)
    }
}


fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}


fun chkStatus(context: Context) : Int {
    val connMgr =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    if (wifi.isConnectedOrConnecting) {
        //Toast.makeText(context, "Wifi", Toast.LENGTH_LONG).show()
        return 0
    } else if (mobile.isConnectedOrConnecting) {
       //Toast.makeText(context, "Mobile 3G ", Toast.LENGTH_LONG).show()
        return 1
    } else {
        //Toast.makeText(context, "No Network ", Toast.LENGTH_LONG).show()
        return -1
    }
}



