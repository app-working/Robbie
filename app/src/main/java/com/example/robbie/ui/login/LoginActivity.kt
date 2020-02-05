package com.example.robbie.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.example.robbie.R
import com.example.robbie.domain.usecase.AuthUseCase
import com.example.robbie.ui.top.TopActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.loginContents, LoginFragment(), null).commit()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}
