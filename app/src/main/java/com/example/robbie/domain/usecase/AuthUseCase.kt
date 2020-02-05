package com.example.robbie.domain.usecase

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.robbie.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthUseCase {

    // 認証結果リスナー
    interface SignInListener {
        // 認証成功時
        fun onSignIn()
        // 認証失敗時
        fun notSighIn()
    }

    private val auth = FirebaseAuth.getInstance()

    fun isLogin() : Boolean {
        auth.currentUser?.let {
            Log.d("Robbie Auth ", "FirebaseAuth is Authenticated")
            return true
        } ?: run {
            Log.d("Robbie Auth ", "FirebaseAuth is Unauthenticated")
            return false
        }
    }

    fun getUser(): FirebaseUser {
        return auth.currentUser!!
    }

    fun signInWithGoogle(account: GoogleSignInAccount, listener: SignInListener) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("Robbie Auth ", "FirebaseAuth is Successful")
                listener.onSignIn()
            } else {
                Log.d("Robbie Auth ", "FirebaseAuth is Failed")
                listener.notSighIn()
            }
        }
    }

    fun signOutWithGoogle(activity: AppCompatActivity) {
        // Robbieサインアウト
        auth.signOut()
        // Googleサインアウト
        getGoogleSignInClient(activity).signOut()
    }

    fun getGoogleSignInClient(activity: AppCompatActivity) : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }
}