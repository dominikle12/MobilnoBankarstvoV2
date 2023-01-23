package com.example.mobilnobankrastvov2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilnobankrastvov2.databinding.ActivityAddFundsBinding

class AddFundsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFundsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFundsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}