package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.mobilnobankrastvov2.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEditText.error = "Invalid email format"
        }else if(TextUtils.isEmpty(password)){
            binding.passwordEditText.error = "Please enter password"
        }
        else if(password.length < 6){
            binding.passwordEditText.error = "Password is too short"
        }else{
            firebaseSignup()
        }
    }

    private fun firebaseSignup() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener{e ->
                Toast.makeText(this, "Signup failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}