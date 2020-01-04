package com.dino.firebasestudysample.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dino.firebasestudysample.R
import com.dino.firebasestudysample.addpost.AddPostActivity
import com.dino.firebasestudysample.addpost.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firebaseUser by lazy { FirebaseAuth.getInstance().currentUser ?: error("잘못 된 접근") }

    private val profileImageStorageRef =
        FirebaseStorage.getInstance().reference.child("images/profile/${firebaseUser.email}.jpg")

    private val postStorageRef =
        FirebaseFirestore.getInstance().collection("posts")

    private val profilePostAdapter = ProfilePostAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_content.adapter = profilePostAdapter
        setupProfile()
        setupListener()
        loadPosts()
    }

    private fun loadPosts() {
        postStorageRef.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot?.isEmpty == false) {
                    val list = querySnapshot.documents
                        .map { it.toObject(Post::class.java)!! }
                    profilePostAdapter.resetAll(list)
                }
            }
    }

    private fun setupListener() {
        iv_profile.setOnClickListener {
            TedImagePicker.with(context!!)
                .start { uri -> uploadProfileImage(uri) }
        }
        btn_add_post.setOnClickListener {
            TedImagePicker.with(context!!)
                .max(20, "최대 20장까지 업로드")
                .startMultiImage {
                    if (it.isNotEmpty()) {
                        AddPostActivity.startActivity(context, it)
                    }
                }
        }
    }

    private fun setupProfile() {
        profileImageStorageRef.downloadUrl
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    showProfileImage(result)
                } else {
                    firebaseUser.photoUrl?.let(this::showProfileImage)
                }
            }
        firebaseUser.displayName?.let(this::showProfileName)
    }

    private fun uploadProfileImage(uri: Uri) {
        showLoading()
        profileImageStorageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                profileImageStorageRef.downloadUrl
            }
            .addOnCompleteListener { storageTask ->
                hideLoading()
                if (storageTask.isSuccessful) {
                    val result = storageTask.result ?: return@addOnCompleteListener
                    updateFirebaseUserProfile(result, uri)
                }
            }
    }

    private fun updateFirebaseUserProfile(result: Uri, uri: Uri) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(result)
            .build()
        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { profileTask ->
                if (profileTask.isSuccessful) {
                    showProfileImage(uri)
                } else {
                    // TODO: 2019-12-21 실패하면 이미지 삭제
                }
            }
    }

    private fun hideLoading() {
        pb_loading.isVisible = false
    }

    private fun showLoading() {
        pb_loading.isVisible = true
    }

    private fun showProfileImage(uri: Uri) {
        Glide.with(iv_profile)
            .load(uri)
            .circleCrop()
            .into(iv_profile)
    }

    private fun showProfileName(name: String) {
        tv_name.text = name
    }


}