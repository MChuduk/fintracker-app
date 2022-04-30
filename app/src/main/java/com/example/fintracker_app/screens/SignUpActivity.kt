package com.example.fintracker_app.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.R
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.ErrorModel
import com.example.fintracker_app.services.CurrencyService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var email : EditText;
    private lateinit var password: EditText;
    private lateinit var passwordConfirm: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        findViews();
    }

    fun onContinueButtonClick(view: View) { signUp(); }

    fun onSignInButtonClick(view: View) {
        val intent = Intent(this, SignInActivity::class.java);
        startActivity(intent);
    }

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
                    val err = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        ErrorModel::class.java
                    )
                    showMessage(err.message);
                } else {
                    val intent = Intent(applicationContext, SignInActivity::class.java);
                    startActivity(intent);

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