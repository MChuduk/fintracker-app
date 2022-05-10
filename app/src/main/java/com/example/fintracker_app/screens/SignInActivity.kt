package com.example.fintracker_app.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.ErrorModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.SnapshotsService
import com.example.fintracker_app.services.WalletsService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var email : EditText;
    private lateinit var password: EditText;

    private lateinit var preferences: SharedPreferences;
    private lateinit var snapshotsService: SnapshotsService;
    private lateinit var walletsService: WalletsService;
    private lateinit var currencyService: CurrencyService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        findViews();
        initServices();
    }

    fun onContinueButtonClick(view: View) {
        lifecycleScope.launch {
            try {
                val repo = Repository();

                val emailString = email.text.toString();
                val passwordString = password.text.toString();

                val response = repo.signInUser(emailString, passwordString);
                val data = response.body();
                if (data === null) {
                    val err = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        ErrorModel::class.java
                    )
                    showMessage(err.message);
                } else {
                    val editor = preferences.edit();
                    editor.putInt("UserId", data.id);
                    editor.putString("UserEmail", emailString);
                    editor.putString("UserToken", data.token);
                    editor.apply();

                    applyLatestSnapshot();
                    currencyService.updateExchangeRates();

                    val intent = Intent(applicationContext, MainActivity::class.java);
                    startActivity(intent);

                    showMessage("Вход:" + data.type);
                }
            } catch(error: Exception) {
                showMessage(error.message);
            }
        }
    }

    fun onSignUpButtonClick(view: View) {
        val intent = Intent(this, SignUpActivity::class.java);
        startActivity(intent);
    }

    private fun applyLatestSnapshot() {
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            val snapshot = snapshotsService.getLatest(token!!);
            currencyService.updateExchangeRates();
            if(snapshot != null) {
                walletsService.applySnapshot(token, snapshot.id);
            }
        }
    }

    private fun findViews() {
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
    }

    private fun initServices() {
        snapshotsService = SnapshotsService(applicationContext);
        walletsService = WalletsService(applicationContext);
        currencyService = CurrencyService(applicationContext);
    }

    private fun showMessage(message: String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }
}