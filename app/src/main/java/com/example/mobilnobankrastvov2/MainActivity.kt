package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.mobilnobankrastvov2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        /*Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.toObject(UserInformation::class.java)!!
                    Log.d("MainActivity", "Uspjesno. prezime: ${user.lastName}")
                    binding.firstNameText.text = user.firstName
                    binding.lastNameText.text = user.lastName
                    binding.fundsAvailableText.text = user.funds.toString() + "€"
                    binding.ibanText.text = user.iban
                }else{
                    Log.w("MainActivity", "Error")
                }
            }*/

        Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .addSnapshotListener{snapshot, e ->
                if(e != null){
                    Log.w(TAG, "Listen failed", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val user = snapshot.toObject(UserInformation::class.java)!!
                    Log.d("MainActivity", "Uspjesno. prezime: ${user.lastName}")
                    binding.firstNameText.text = user.firstName
                    binding.lastNameText.text = user.lastName
                    binding.fundsAvailableText.text = user.funds.toString() + "€"
                    binding.ibanText.text = user.iban
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

        binding.logOutItem.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        binding.payItem.setOnClickListener{
            intent = Intent(this, PayActivity::class.java)
            intent.putExtra("senderIban", binding.ibanText.text.toString())
            finish()
            startActivity(intent)

        }




    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            val email = firebaseUser.email
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    private fun getDataFromUser(uid: String): UserInformation{
        var user = UserInformation()
        Firebase.firestore.collection("users").document(uid).get()
            .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        user = task.result.toObject(UserInformation::class.java)!!
                        Log.d("MainActivity", "Uspjesno. prezime: ${user.lastName}")
                        binding.firstNameText.text = user.firstName
                        binding.lastNameText.text = user.lastName
                        binding.fundsAvailableText.text = user.funds.toString()
                    }else{
                        Log.w("MainActivity", "Error")
                    }
            }
        return user
    }






}

