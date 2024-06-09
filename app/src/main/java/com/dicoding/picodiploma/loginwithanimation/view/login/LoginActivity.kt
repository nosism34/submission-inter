package com.dicoding.picodiploma.loginwithanimation.view.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.data.Api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.costumview.EditText
import com.dicoding.picodiploma.loginwithanimation.view.costumview.button
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    private lateinit var Button : button
    private lateinit var editText: EditText
    private lateinit var Email : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Button = binding.loginButton
        editText = binding.passwordEditText
        Email = binding.emailEditText
        val inputEmail: String? = intent.getStringExtra("email")
        Email.setText(inputEmail)

        setMyButtonEnable()
        setupView()
        setupAction()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        Email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

    }
    private fun setMyButtonEnable() {

        val password = editText.text
        val email = Email.text

        Button.isEnabled = password != null && password.toString().isNotEmpty()
                && email != null && email.toString().isNotEmpty()
                && password.toString().length >= 8

    }
    private fun requestLogin() {
        showLoading(true)

        lifecycleScope.launch {
            val email: String = binding.emailEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()
            try {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                val apiService = ApiConfig.getApiService("")
                val successResponse = apiService.login(email, password)

                showToast("Login Success")
                showLoading(false)

                try {
                    val responseBody = successResponse
                    if (!responseBody.error && responseBody.message == "success") {
                        val token = responseBody.loginResult.token
                        val username = responseBody.loginResult.userId
                        Log.d("Token", token)
                        viewModel.saveSession(UserModel(email, token ,username))

                    } else {
                        Log.e("Login", "Login failed")
                    }

                } catch (e: Exception) {
                    Log.e("JSON", "Error parsing JSON: ${e.message}")
                }
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle())

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                showToast(errorResponse.message)
                showLoading(false)
            }

        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setupAction() {

        binding.loginButton.setOnClickListener {
            requestLogin()
        }
    }

}