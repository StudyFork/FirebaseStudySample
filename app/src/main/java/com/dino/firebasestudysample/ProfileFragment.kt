package com.dino.firebasestudysample

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firebaseUser by lazy { FirebaseAuth.getInstance().currentUser ?: error("잘못 된 접근") }

    private val storageRef =
        FirebaseStorage.getInstance().reference.child("images/profile/${firebaseUser.email}.jpg")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfile()
        setupListener()
    }

    private fun setupListener() {
        iv_profile.setOnClickListener {
            TedImagePicker.with(context!!)
                .start { uri -> uploadProfileImage(uri) }
        }
    }

    private fun setupProfile() {
        storageRef.downloadUrl
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
        storageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
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