package com.example.runningapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler  = Handler().postDelayed(object:Runnable{
            override fun run() {
                val Intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(Intent)
                finish()
            }

        },3500)
    }
}