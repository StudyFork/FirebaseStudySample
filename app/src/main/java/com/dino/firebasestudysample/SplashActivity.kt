package com.dino.firebasestudysample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkFirebaseAuth()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_FIREBASE_SIGN_IN -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startMain()
                    }
                    Activity.RESULT_CANCELED -> {
                        finish()
                    }
                }
            }
        }
    }

    private fun checkFirebaseAuth() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startFirebaseSignIn()
        } else {
            startMain()
        }
    }

    private fun startFirebaseSignIn() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.FirebaseUIAuth)
                .setLogo(R.mipmap.ic_launcher_round)
                .setAvailableProviders(providers)
                .build(),
            REQ_CODE_FIREBASE_SIGN_IN
        )
    }

    private fun startMain() {
        MainActivity.startActivity(this)
        finish()
    }

    companion object {
        private const val REQ_CODE_FIREBASE_SIGN_IN = 61597
    }


}