package com.example.um_flintapplication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getString
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class Auth(private val ctx: Context) {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(ctx, R.string.googleClientId))
            .requestEmail()
            .requestProfile()
            .build()

    fun silentLogin(callback: (GoogleSignInAccount?) -> Unit){
        if (ctx !is AppCompatActivity) {
            throw Exception("Please use the activity context")
        }

        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso)

        val task = mGoogleSignInClient.silentSignIn()

        if(task.isSuccessful){
            callback(task.getResult())
        }else{
            callback(null)
        }
    }

    fun login(){
        if (ctx !is AppCompatActivity) {
            throw Exception("Please use the activity context")
        }

        val launcher = (ctx as ComponentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleLogin(GoogleSignIn.getSignedInAccountFromIntent(result.data))
            }
        }

        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    fun logout(callback: (Boolean) -> Unit){
        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso)

        val task = mGoogleSignInClient.signOut()

        if(task.isSuccessful){
            callback(true)
        }else{
            callback(false)
        }
    }

    private fun handleLogin(completedTask: Task<GoogleSignInAccount>){
        Log.i(TAG, "handleGoogleSignIn")

        val account = completedTask.getResult(
            ApiException::class.java
        )

        Toast.makeText(ctx, ("Hello " + account?.givenName), Toast.LENGTH_LONG).show()
    }
}