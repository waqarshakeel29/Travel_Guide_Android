package Utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connectivity
{
    internal var context: Context? = null

    companion object
    {
        var Result: Int = 0
        var INTERNET_RESPONSE_TIME = 0


        fun getNetworkInfo(context: Context): NetworkInfo?
        {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }

        fun isConnected(context: Context): Boolean
        {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected
        }

        //********* Internet Available ********
        fun IsNetAvailable(context: Context): Boolean
        {
            val lStartTime = System.currentTimeMillis()
            //some tasks
            Thread(Runnable {
                try
                {
                    val runtime = Runtime.getRuntime()
                    val process = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                    Result = process.waitFor()
                }
                catch (e: Exception)
                {
                    //					Toast.makeText(context, "Exp \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).start()
            val lEndTime = System.currentTimeMillis()

            INTERNET_RESPONSE_TIME = (lEndTime - lStartTime).toInt()

            //        if (context.getResources().getBoolean(R.bool.IsLocal))
            //        {
            //            return true;
            //        }
            //        else
            //        {
            //            return (Result == 0);
            //        }
            return true
        }
    }
}