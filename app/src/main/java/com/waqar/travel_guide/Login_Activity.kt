package com.waqar.travel_guide

import Fragments.Main_Menu_Fragment
import Utility.chkStatus
import Utility.showToast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class Login_Activity : AppCompatActivity() {


    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //login()

    }

    override fun onStart() {
        super.onStart()
        login()
    }

    fun login(){
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account == null) {
            sign_in_button.visibility = View.VISIBLE
            sign_in_button.setOnClickListener {

                if(chkStatus(this) != -1) {
                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, 101)
                }else{
                    "Please Check Internet Connection".showToast(this)
                }
            }
        }else{
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 101) { // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("GOOGLE",task.toString())
            handleSignInResult(task);
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            startActivity(Intent(this,MainActivity::class.java))
//            fragmentTransaction(Main_Menu_Fragment(), resources.getString(com.waqar.travel_guide.R.string.app_name))

        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE", "signInResult:failed code=" + e.statusCode)
        }
    }
}
