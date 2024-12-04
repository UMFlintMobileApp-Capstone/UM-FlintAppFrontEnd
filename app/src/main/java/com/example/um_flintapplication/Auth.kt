package com.example.um_flintapplication

import android.app.Activity
import android.app.Activity.RESULT_OK
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

class Auth(private val ctx: Context) {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var launch: ActivityResultLauncher<Intent>

    private val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(ctx, R.string.googleClientId))
            .requestEmail()
            .requestProfile()
            .setHostedDomain("umich.edu")
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
                handleLogin(result.data)
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

    fun handleLogin(intent: Intent?): GoogleSignInAccount{
        Log.i("GOOGLEAUTH", "handleLogin")

        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

        val account = task.getResult(
            ApiException::class.java
        )

        Toast.makeText(ctx, ("Hello " + account?.givenName), Toast.LENGTH_LONG).show()

        return account
    }

    fun goHomeIfUnauthorized(){
        Log.i("GOOGLEAUTH", "goHomeIfUnauthorized")

        val launcher = (ctx as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode != RESULT_OK) {
                Log.d("GOOGLEAUTH", "goHomeIfUnauthorized: Unauthorized")
                Toast.makeText(ctx,"You are not authorized to view this page. Try to login.",
                    Toast.LENGTH_SHORT).show()

                val intent =
                    Intent(ctx, MainActivity::class.java)
                ctx.startActivity(intent)
            }else{
                Log.d("GOOGLEAUTH", "goHomeIfUnauthorized: Authorized")
            }
        }

        login(launcher)
    }

}