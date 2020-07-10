package com.waqar.travel_guide

import Fragments.Main_Menu_Fragment
import Fragments.Nearby_Place_Fragment
import Utility.Connectivity
import Utility.IsLocationAllow
import Utility.onPermissionsResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tramsun.libs.prefcompat.Pref
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var countCurrentFragment = 0

    var InternetReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            if (Connectivity.isConnected(context) && Connectivity.IsNetAvailable(context)) {
                Log.d("TAG", "Internet Arrived")
            }
            else {
//                ShowNoInternetConnection()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Pref.init(this)

        instance = this
        permission()
        if (countCurrentFragment == 0) {
//            fragmentTransaction(Nearby_Place_Fragment(), resources.getString(R.string.app_name))
            fragmentTransaction(Main_Menu_Fragment(), resources.getString(R.string.app_name))
//            fragmentTransaction(Login_Fragment(), resources.getString(R.string.app_name))
        }
        registerReceiver(InternetReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }


    companion object {
        //
        lateinit var instance: MainActivity
            private set

        //
        fun permission(): Boolean {
            var result: Boolean = false
            instance.IsLocationAllow(onPermissionAccepted =
            {
                result = true
                /// Toast.makeText(this,"ALLOW",Toast.LENGTH_SHORT).show()
            }, onPermissionDenied = {

                AlertDialog.Builder(instance)
                    .setMessage("These permissions are mandatory to get your location. You need to allow them.")
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface, i ->

                        permission()
                        // Application_Class.instance?.startLocationService()
                        //Application_Class.start(Application_Class.instance!!)
                    }.setNegativeButton("Cancel") { dialog, which -> }.create().show()
            })

            return result
        }
    }

    fun fragmentTransaction(fragment: Fragment, fragmentName: String) {
        val fragmentManager = supportFragmentManager

        CustomeText.text = fragmentName

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_from_left,
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        );
        fragmentTransaction.replace(R.id.FlFragmentArea, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        countCurrentFragment++
    }


    fun fragmentTransaction(fragment: Fragment, fragmentName: String,keyword : String) {
        val fragmentManager = supportFragmentManager

        CustomeText.text = fragmentName


        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_from_left,
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        );

        var bundle = Bundle()
        bundle.putString("KEYWORD",keyword)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.FlFragmentArea, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        countCurrentFragment++
    }

    override fun onBackPressed() {
        countCurrentFragment--

        if (countCurrentFragment == 1) {
            CustomeText.text = resources.getText(R.string.app_name)
        }
        if (countCurrentFragment == 0) {
            moveTaskToBack(true)
            finish()
        }else
            super.onBackPressed()
    }


    fun OpenInterstitialAd(uriKey: String) {
        val gmmIntentUri = Uri.parse(uriKey)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent)
        } else {
            Log.d("TAG", "goToNextLevel: ")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onPermissionsResult(grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(InternetReceiver)
        exit()
    }


    fun exit() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Do you really want to exit ?")
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            Exit_Activity.exitApplicationAnRemoveFromRecent(
                this
            )
            finish()
        }
        builder.create()
        builder.show()
    }
}
