package com.example.um_flintapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Auth(private val ctx: Context) {
    private var credentialManager = CredentialManager.create(ctx)
    lateinit var sharedPreferences: SharedPreferences

    private val request: GetCredentialRequest = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("178916369142-chblq5qgi571g6jmjmcgirit4tgpit25.apps.googleusercontent.com")
        .setAutoSelectEnabled(true)
        .build().let {
            GetCredentialRequest.Builder()
                .addCredentialOption(it)
                .build()
        }

    fun login(callback: (cred: String?) -> Unit){
        if (ctx !is AppCompatActivity) {
            throw Exception("Please use the activity context")
        }
        sharedPreferences = ctx.getSharedPreferences("loginPref", Context.MODE_PRIVATE)

        if(sharedPreferences.contains("GoogleIdTokenCredential")){
            callback(sharedPreferences.getString("GoogleIdTokenCredential", ""))
            Toast.makeText(ctx,"Already logged in, passed the JWT token!", Toast.LENGTH_LONG).show()
        }else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.i(TAG, "credentialManager.getCredential")

                    val result = credentialManager.getCredential(
                        context = ctx,
                        request = request
                    )

                    handleLogin(callback, result)

                    Toast.makeText(ctx,"Just logged in, passed the JWT token!", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleLogin(callback: (cred: String?) -> Unit,
                            result: GetCredentialResponse
    ){
        Log.i(TAG, "handleGoogleSignIn")
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try{
                Log.i(TAG, "GoogleIdTokenCredential createFrom")
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("GoogleIdTokenCredential", googleIdTokenCredential.idToken)
                editor.apply()

                callback(googleIdTokenCredential.idToken)
                Log.i(TAG, "handleSignIn: $googleIdTokenCredential")
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "Received an invalid google id token response", e)
            }
        }
    }
}