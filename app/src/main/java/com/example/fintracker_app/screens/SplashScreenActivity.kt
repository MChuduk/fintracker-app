package com.example.fintracker_app.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);

        val isLogin = preferences.contains("UserId")

        if(!isLogin) {
            val intent = Intent(applicationContext, SignUpActivity::class.java);
            startActivity(intent);
        }else{
            val intent = Intent(applicationContext, MainActivity::class.java);
            startActivity(intent);
        }
    }
}