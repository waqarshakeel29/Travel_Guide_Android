package com.waqar.travel_guide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class Exit_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask()
        } else {
            finish()
        }
    }

    companion object {
        fun exitApplicationAnRemoveFromRecent(mContext: Context) {
            try {
                val intent = Intent(mContext, Exit_Activity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                mContext.startActivity(intent)
                //Drawer_Activity.FragmentsList = ArrayList<Fragment_Model>()
                //Drawer_Activity.FragmentPosition = -1
                //                Drawer_Activity.fm = null
                //                Drawer_Activity.fragmentTransaction = null
                //            FirebaseInstanceId.getInstance().deleteInstanceId();

            } catch (e: Exception) {
                Log.d("TAG", "exitApplicationAndRemoveFromRecent Exp:\n" + e.message)
            }

        }
    }
}