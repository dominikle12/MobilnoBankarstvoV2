package com.example.mobilnobankrastvov2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilnobankrastvov2.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import com.example.mobilnobankrastvov2.MainActivity.TransactionViewHolder as TransactionViewHolder1

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<TransactionInfo>
    private var db = Firebase.firestore
    var iban = ""
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()







        transactionRecyclerView = findViewById(R.id.transacitonRecyclerView1)
        //transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

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
                    iban = user.iban!!
                    Log.d(TAG, "USER IBAN: $iban")

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

        binding.sentButton.setOnClickListener {
            showTransactions(iban, true)
        }

        binding.recievedButton.setOnClickListener {
            showTransactions(iban, false)
        }



    }

    private fun showTransactions(iban: String, sender:Boolean){

        db = FirebaseFirestore.getInstance()
        val transactionList = ArrayList<TransactionInfo>()
        if(sender){
            db.collection("transactions").whereEqualTo("senderIban", iban)
                .get()
                .addOnSuccessListener {result->
                    Log.d(TAG, "USpjelo recycleview")
                    for(data in result.documents){
                        val transaction: TransactionInfo? = data.toObject(TransactionInfo::class.java)
                        if(transaction!=null){
                            transactionList.add(transaction)
                        }
                    }
                    Log.d(TAG, transactionList.size.toString())

                    transactionRecyclerView.adapter = TransactionAdapter(transactionList)
                }
        }else{
            db.collection("transactions").whereEqualTo("recipientIban", iban)
                .get()
                .addOnSuccessListener {result->
                    Log.d(TAG, "USpjelo recycleview")
                    for(data in result.documents){
                        val transaction: TransactionInfo? = data.toObject(TransactionInfo::class.java)
                        if(transaction!=null){
                            transactionList.add(transaction)
                        }
                    }
                    Log.d(TAG, transactionList.size.toString())

                    transactionRecyclerView.adapter = TransactionAdapter(transactionList)
                }
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

    private class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }




}



