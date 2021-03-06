package com.dino.firebasestudysample.ui.addpost

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.dino.firebasestudysample.R
import com.dino.firebasestudysample.event.FirebaseEvent
import com.dino.firebasestudysample.event.FirebaseParam
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.*

class AddPostActivity : AppCompatActivity(R.layout.activity_add_post) {

    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(application) }

    private val db by lazy { FirebaseFirestore.getInstance().collection("posts") }

    private val images by lazy { intent.getParcelableArrayListExtra<Uri>(EXTRA_IMAGES) }

    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference.child("images/posts/")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListener()
        rv_content.adapter = AddPostAdapter().apply {
            resetAll(images)
        }
    }

    private fun setupListener() {
        btn_add_post.setOnClickListener {
            firebaseAnalytics.logEvent(
                FirebaseEvent.CLICK_POST, bundleOf(
                    FirebaseParam.POST_COUNT to images?.size
                )
            )
            uploadPost()
        }
    }

    private fun uploadPost() {
        pb_loading.isVisible = true
        val date = Date()
        val postDate = date.toString()
        uploadImages(postDate) {
            if (it.size != images?.size ?: 0) {
                return@uploadImages
            }
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val post = Post(
                title,
                content,
                date,
                it.map { it.toString() })
            db.document(postDate)
                .set(post)
                .addOnSuccessListener { finish() }
        }
    }

    private fun uploadImages(postDate: String, action: (List<Uri>) -> Unit) {
        val postRef = storageRef.child(postDate)
        val uriList = mutableListOf<Uri>()
        images?.forEachIndexed { index, uri ->
            val child = postRef.child("$index.jpg")
            child.putFile(uri)
                .addOnCompleteListener {
                    child
                        .downloadUrl
                        .addOnSuccessListener {
                            uriList.add(it)
                            action(uriList)
                        }
                }
        }
    }

    override fun onBackPressed() {
        if (pb_loading.isGone) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_IMAGES = "EXTRA_IMAGES"

        fun startActivity(context: Context?, images: List<Uri>) {
            context?.startActivity(Intent(context, AddPostActivity::class.java).apply {
                putExtras(bundleOf(EXTRA_IMAGES to images))
            })
        }
    }
}