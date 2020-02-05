package com.example.robbie.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.robbie.R
import com.example.robbie.domain.usecase.AuthUseCase
import com.example.robbie.ui.top.TopActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), AuthUseCase.SignInListener {

    val RC_GOOGLE_SIGN_IN_CODE = 908

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Googleサインイン
        val googleSignInClient = AuthUseCase().getGoogleSignInClient(activity as LoginActivity)
        btn_sign_in.setOnClickListener {
            val googleSignInIntent = googleSignInClient.signInIntent
            startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE && data != null) {
            try {
                // Google認証情報の取得
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.result
                account?.let {
                    // Google認証情報を元にRobbie(Firebase)認証する
                    AuthUseCase().signInWithGoogle(it, this)
                }
            } catch (e: ApiException) {
                Log.d("Robbie ApiException", e.toString())
            }
        }
    }

    override fun onSignIn() {
        val intent = Intent(this.activity, TopActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        this.activity?.startActivity(intent)
    }

    override fun notSighIn() {
        Toast.makeText(this.activity, "認証に失敗しました", Toast.LENGTH_SHORT).show()
    }
}
