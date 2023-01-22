package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TimeUtils
import android.widget.EditText
import com.example.mobilnobankrastvov2.databinding.ActivityMainBinding
import com.example.mobilnobankrastvov2.databinding.ActivityPayBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.zip.Inflater

class PayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        firebaseAuth = FirebaseAuth.getInstance()

        var recIban = findViewById<EditText>(R.id.recipientIbanEditText)
        var amount = findViewById<EditText>(R.id.amountEditText)


        binding.backImage.setOnClickListener {
            finish()
        }
        binding.payButton.setOnClickListener {
            validateData()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }


    private fun saveTransaction(amount: Int){
        var bundle: Bundle? = intent.extras

        Firebase.firestore.collection("transactions").document()
            .set(hashMapOf(
                "amount" to amount,
                "recipientIban" to binding.recipientIbanEditText.text.toString(),
                "senderIban" to intent.getStringExtra("senderIban"),
                "timeSent" to SimpleDateFormat("MMM dd,yyyy HH:mm").format(System.currentTimeMillis())
            ))
            .addOnSuccessListener {
                Log.d("MainActivity", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener{
                Log.d(TAG, "Error in savetrans fun")
            }

    }
    private fun validateData(){

        if(TextUtils.isEmpty(binding.amountEditText.text.toString())){
            binding.amountEditText.setError("Can't be empty!")
        }else if(TextUtils.isEmpty(binding.recipientIbanEditText.text.toString())){
            binding.recipientIbanEditText.setError("Can't be empty")
        }else{
            Log.d(TAG, "Proslo validatedata")
            validateAmount(binding.amountEditText.text.toString().toInt())
        }
    }

    private fun validateAmount(amount: Int){
        Log.d(TAG, amount.toString())
        Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    var currentAmount = task.result.toObject(UserInformation::class.java)?.funds

                    Log.d(TAG, "Pare: $currentAmount")
                    if(amount <= currentAmount!!){
                        deductFunds(currentAmount-amount)
                        checkRecipientsFunds(binding.recipientIbanEditText.text.toString(), amount)
                        saveTransaction(amount)
                    }
                }else{
                    Log.d(TAG, "Error")
                }
            }
    }


    private fun deductFunds(amount: Int){
        Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .update("funds", amount)
            .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { Log.w("MainActivity", "Error writing document") }
    }
    private fun checkRecipientsFunds(iban: String, amount: Int){
        Firebase.firestore.collection("users").whereEqualTo("iban", iban)
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    Log.d(TAG, task.result.documents[0].id)
                    var currentAmount = task.result.documents[0].toObject(UserInformation::class.java)?.funds
                    Firebase.firestore.collection("users").document(task.result.documents[0].id)
                        .update("funds", currentAmount!! + amount)
                        .addOnSuccessListener { Log.d("MainActivity", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { Log.w("MainActivity", "Error writing document") }
                }else{
                    Log.d(TAG, "Error")
                }
            }


    }

}