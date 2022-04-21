package com.example.fintracker_app.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.fintracker_app.R

class SignInActivity : AppCompatActivity() {

    private var _editTextEmail: EditText? = null;
    private var _editTextPassword: EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    private fun findViews() {
        _editTextEmail = findViewById(R.id.editTextEmail);
        _editTextPassword = findViewById(R.id.editTextPassword);
    }
}