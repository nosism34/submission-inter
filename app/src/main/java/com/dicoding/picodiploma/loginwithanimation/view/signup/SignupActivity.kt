package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.data.Api.ApiConfig.getApiService
import com.dicoding.picodiploma.loginwithanimation.data.Api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.costumview.EditText
import com.dicoding.picodiploma.loginwithanimation.view.costumview.button
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private lateinit var Button: button
    private lateinit var passeditText: EditText
    private lateinit var Username: TextInputEditText
    private lateinit var Email: TextInputEditText
    private lateinit var mContext: Context
    private lateinit var mApiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this
        mApiService = getApiService("")



        Button = binding.signupButton
        passeditText = binding.passwordEditText
        Email = binding.emailEditText
        Username = binding.nameEditText


        setMyButtonEnable()
        setupView()
        setupAction()
        showAnimation()

        passeditText.addTextChangedListener(object : TextWatcher {
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

        Username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

    }

    private fun showAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, 30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nama =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailText =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1F).setDuration(100)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(100)
        val passwordText =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1F).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1F).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                signup,
                title,
                name, nama,
                emailText, email,
                passwordText, password
            )
            start()
        }

    }

    private fun requestRegister() {
        showLoading(true)

        lifecycleScope.launch {
            val name: String = binding.nameEditText.text.toString()
            val email: String = binding.emailEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()
            try {
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                intent.putExtra("email", email)
                val apiService = getApiService("")
                apiService.register(name, email, password)
                showToast("User Account Created")
                showLoading(false)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignupActivity as Activity)
                        .toBundle()
                )

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
    private fun setMyButtonEnable(){
        val password = passeditText.text
        val email = Email.text
        val username = Username.text
        Button.isEnabled = password != null && password.toString().isNotEmpty()
                && email != null && email.toString().isNotEmpty()
                && password.toString().length >= 8
                && username != null && username.toString().isNotEmpty()

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
        binding.signupButton.setOnClickListener {
           requestRegister()
        }
    }
}