package com.dino.firebasestudysample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class AddPostActivity : AppCompatActivity(R.layout.activity_add_post) {


    companion object {

        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, AddPostActivity::class.java))

        }
    }
}