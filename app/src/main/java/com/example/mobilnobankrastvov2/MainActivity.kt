package com.example.mobilnobankrastvov2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.mobilnobankrastvov2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            toggle= ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.firstItem->{

                    }
                    R.id.secondItem->{

                    }
                    R.id.logOutItem->{
                        firebaseAuth.signOut()
                        checkUser()
                    }
                }
                true
            }
        }
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //val userInformation = getDataFromUser(firebaseAuth.currentUser!!.uid)
        Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {result ->
                val userMan = result.toObject(UserInformation::class.java)
                binding.firstNameText.text = userMan?.firstName
            }

        /*binding.lastNameText.text = userInformation.lastName
        binding.fundsAvailableText.text = userInformation.funds.toString()*/

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getDataFromUser(uid: String):UserInformation{
        var user = UserInformation()
        Firebase.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { result ->
                    user = result.toObject(UserInformation::class.java)!!
            }
            .addOnFailureListener{e->
                Log.w("MainActivity", "Error")
            }
        return user
    }



}

