//package com.example.um_flintapplication
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.gcm.Task
//import com.google.android.gms.tasks.Task
//import com.google.android.libraries.mapsplatform.transportation.driver.api.base.data.Task
//
//class SignInActivity<GoogleSignInAccount> : AppCompatActivity() {
//    private var mGoogleSignInClient: GoogleSignInClient? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        // Configure Google Sign-In
//        val gso: GoogleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        findViewById<View>(R.id.btn_google_sign_in).setOnClickListener { view: View? -> signIn() }
//    }
//
//    private fun signIn() {
//        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val task: com.google.android.gms.tasks.Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully
//            Log.d("SignInActivity", "Signed in as: " + account.getDisplayName())
//        } catch (e: ApiException) {
//            Log.w("SignInActivity", "Sign-in failed: " + e.getStatusCode())
//        }
//    }
//
//    companion object {
//        private const val RC_SIGN_IN = 100
//    }
//}
