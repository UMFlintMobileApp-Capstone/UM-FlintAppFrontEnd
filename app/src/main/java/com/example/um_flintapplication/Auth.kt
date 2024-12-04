package com.example.um_flintapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
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
    private lateinit var launch: ActivityResultLauncher<Intent>
    private var success: Boolean = false

    private val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(ctx, R.string.googleClientId))
            .requestEmail()
            .requestProfile()
            .build()

    fun silentLogin(callback: (GoogleSignInAccount?) -> Unit){
        Log.i("GOOGLEAUTH", "silentLogin")

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

    fun createLauncher(){
        Log.i("GOOGLEAUTH", "createLauncher")

        launch = (ctx as ComponentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleLogin(GoogleSignIn.getSignedInAccountFromIntent(result.data))
            }
        }
    }

    fun login(launcher: ActivityResultLauncher<Intent>? = null){
        var l: ActivityResultLauncher<Intent>? = launcher
        Log.i("GOOGLEAUTH", "login")

        if (ctx !is AppCompatActivity) {
            throw Exception("Please use the activity context")
        }

        if(l==null){
            if(::launch.isInitialized){
                l = launch
            }else{
                throw Exception("Please either pass a launcher to login() or create a generic with " +
                        "createLauncher()")
            }
        }

        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        l.launch(signInIntent)
    }

    fun logout(callback: (Boolean) -> Unit){
        Log.i("GOOGLEAUTH", "logout")

        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso)

        mGoogleSignInClient.signOut().addOnCompleteListener { success ->
            callback(success.isComplete)
        }
    }

    private fun handleLogin(completedTask: Task<GoogleSignInAccount>){
        Log.i("GOOGLEAUTH", "handleLogin")

        var account = completedTask.getResult(
            ApiException::class.java
        )

        Toast.makeText(ctx, ("Hello " + account?.givenName), Toast.LENGTH_LONG).show()
    }
}