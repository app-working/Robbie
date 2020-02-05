package com.example.robbie.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthRepository {
    val auth = FirebaseAuth.getInstance()
    fun getCurrentUser() : FirebaseUser? {
        Log.d("Robbie Auth ", "Name is " + auth.currentUser?.displayName)
        Log.d("Robbie Auth ", "Email is " + auth.currentUser?.email)
        Log.d("Robbie Auth ", "Uid is " + auth.currentUser?.uid)
        Log.d("Robbie Auth ", "ProviderData is " + (auth.currentUser?.providerData?.get(1)?.providerId))
        return auth.currentUser
    }
}