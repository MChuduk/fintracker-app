package com.example.fintracker_app.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.R
import com.example.fintracker_app.data.repository.Repository
import kotlinx.coroutines.launch
import com.google.gson.Gson

import org.json.JSONObject




class SignUpActivity : AppCompatActivity() {

    private lateinit var email : EditText;
    private lateinit var password: EditText;
    private lateinit var passwordConfirm: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        findViews();
    }

    fun onSignUpButtonClick(view: View) { signUp(); }

    private fun signUp() {
        lifecycleScope.launch {
            try {
                val repo = Repository();

                val emailString = email.text.toString();
                val passwordString = password.text.toString();
                val passwordConfirmString = passwordConfirm.text.toString();

                if (passwordString != passwordConfirmString) {
                    throw Exception("Пароли не совпадают");
                }

                val response = repo.signUpUser(emailString, passwordString);
                val data = response.body();
                if (data === null) {
                    showMessage(response.errorBody()?.string());
                } else {
                    showMessage("Зарегистрирован id:" + data.id);
                }
            } catch(error: Exception) {
                showMessage(error.message);
            }
        }
    }

    private fun findViews() {
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        passwordConfirm = findViewById(R.id.editTextConfirmPassword);
    }

    private fun showMessage(message: String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }
}