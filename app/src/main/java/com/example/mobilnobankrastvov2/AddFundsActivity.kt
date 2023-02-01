package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mobilnobankrastvov2.databinding.ActivityAddFundsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFundsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFundsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val TAG = "AddFundsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFundsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var bundle:Bundle? = intent.extras
        firebaseAuth = FirebaseAuth.getInstance()
        binding.addButton.setOnClickListener {
            Log.d(TAG, intent.getStringExtra("currentFunds").toString())
            var funds: Int = intent.getStringExtra("currentFunds")!!.toInt() + binding.amountEditText.text.toString().toInt()
            Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .update("funds", funds)
                .addOnSuccessListener {
                    Log.d(TAG, "USPJELO ADD FUNDS")
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }
        binding.backImage2.setOnClickListener {
            finish()

        }
    }


}