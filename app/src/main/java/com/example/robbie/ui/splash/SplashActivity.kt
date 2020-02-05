package com.example.robbie.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.robbie.R
import com.example.robbie.domain.usecase.AuthUseCase
import com.example.robbie.ui.login.LoginActivity
import com.example.robbie.ui.top.TopActivity

class SplashActivity : AppCompatActivity() {


    private val handler = Handler()
    private val runnableTop = Runnable {

        // MainActivityへ遷移させる
        val intent = Intent(this, TopActivity::class.java)
        startActivity(intent)
        finish()
    }
    private val runnableLogin = Runnable {

        // MainActivityへ遷移させる
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // ログインチェック
        if (!AuthUseCase().isLogin()) {
            // スプラッシュ表示1000ms(1秒)後にrunnableを呼んでMeinActivityへ遷移させる
            handler.postDelayed(runnableLogin, 1000)
        } else {
            handler.postDelayed(runnableTop, 1000)
        }
    }
}
