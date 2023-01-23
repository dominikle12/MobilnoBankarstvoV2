package com.example.mobilnobankrastvov2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactionList: ArrayList<TransactionInfo>):
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val amount: TextView = itemView.findViewById(R.id.amountText)
        val senderIban: TextView = itemView.findViewById(R.id.senderIbanText)
        val recipientIban: TextView = itemView.findViewById(R.id.recipientIbanText)
        val timeSent: TextView = itemView.findViewById(R.id.timeSentText)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_in_recyclerview, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.amount.text = transactionList[position].amout.toString()
        holder.senderIban.text = transactionList[position].senderIban.toString()
        holder.recipientIban.text = transactionList[position].recipientIban.toString()
        holder.timeSent.text = transactionList[position].timeSent.toString()
    }

}