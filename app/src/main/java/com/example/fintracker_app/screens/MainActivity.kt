package com.example.fintracker_app.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName

class MainActivity : AppCompatActivity() {

    private lateinit var textViewUserProfile: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setUserInfo();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.ExitItem -> exit();
        }
        return super.onOptionsItemSelected(item);
    }

    private fun exit() {
        val preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        val editor = preferences.edit();
        editor.remove("UserId")
        editor.remove("UserEmail");
        editor.remove("UserToken")
        editor.apply();

        val intent = Intent(applicationContext, SignInActivity::class.java);
        startActivity(intent);
    }

    private fun setUserInfo() {
        val preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        val userEmail = preferences.getString("UserEmail", "Undefined");
        textViewUserProfile.text = "Пользователь: $userEmail";
    }

    private fun findViews() {
        textViewUserProfile = findViewById(R.id.textViewUserProfile);
    }
}