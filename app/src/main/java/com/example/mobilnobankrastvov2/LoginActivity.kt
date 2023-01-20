package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.mobilnobankrastvov2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        binding.registerText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEditText.setError("Invalid email format")
        }else if(TextUtils.isEmpty(password)){
            binding.passwordEditText.error = "Please enter password"
        }else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener{e ->
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}