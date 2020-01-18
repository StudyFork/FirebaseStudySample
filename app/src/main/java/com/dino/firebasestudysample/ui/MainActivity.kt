package com.dino.firebasestudysample.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dino.firebasestudysample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    companion object {

        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, MainActivity::class.java))
        }

    }

}
