package com.example.mobilnobankrastvov2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TransactionFragment : Fragment() {

    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<TransactionInfo>
    private lateinit var firebaseAuth: FirebaseAuth
    private var iban = ""
    private var db = Firebase.firestore
    private val TAG = "TransactionFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView2)

        transactionRecyclerView.layoutManager = LinearLayoutManager(this@TransactionFragment.context)

        Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.toObject(UserInformation::class.java)!!
                    iban = user.iban!!

                }else{
                    Log.w(TAG, "Error")
                }
            }

        view.findViewById<Button>(R.id.sentButton2).setOnClickListener {
            showTransactions(iban, true)
            Log.d(TAG, "STISNUTO")
        }

        view.findViewById<Button>(R.id.recievedButton2).setOnClickListener {
            showTransactions(iban, false)
        }

        return view
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


}