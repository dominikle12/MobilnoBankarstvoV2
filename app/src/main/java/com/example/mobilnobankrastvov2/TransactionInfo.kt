package com.example.mobilnobankrastvov2

import java.text.SimpleDateFormat

data class TransactionInfo(
    val amout: Int? = 0,
    val recipientIban: String? = "",
    val senderIban: String? = "",
    val timeSent: String? = ""
)
