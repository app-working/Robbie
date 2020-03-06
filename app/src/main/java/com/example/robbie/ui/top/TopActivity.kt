package com.example.robbie.ui.top

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.robbie.R
import com.example.robbie.data.repository.FirebaseAuthRepository
import com.example.robbie.domain.usecase.AuthUseCase
import com.example.robbie.ui.login.LoginActivity
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.activity_top.*
import com.example.robbie.util.isActiveNetwork


class TopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        button_signout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("ログアウトしますか？")
                .setPositiveButton("はい") {_, _ ->
                    AuthUseCase().signOutWithGoogle(this)
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    this.startActivity(intent)
                }
                .setNegativeButton("キャンセル") {_, _ -> }
                .show()
        }

        val firebaseUser = FirebaseAuthRepository().getCurrentUser()
        displayName.text = firebaseUser?.displayName + "さん"

        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(navigation, navController)
    }

    override fun onResume() {
        super.onResume()
        // ネットワーク接続チェック
        if (!isActiveNetwork(this)) {
            AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Robbieにアクセスできません\nインターネット接続を確認してください")
                .setPositiveButton("確認") {_, _ ->
                    val intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                }
                .show()
        }
        // 認証チェック
        if (!AuthUseCase().isLogin()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}

// [zxingでQRコードを読み取る時に画面が横回転しないようにする](https://qiita.com/tktktks10/items/3b327b2900d38e672996)
class RobbieCaptureActivity : CaptureActivity()
