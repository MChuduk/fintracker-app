package com.example.fintracker_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.TransactionModel

class TransactionsAdapter(private var items: List<TransactionModel>) :
    RecyclerView.Adapter<TransactionsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTransactionNote: TextView? = null;

        init {
            textViewTransactionNote = itemView.findViewById(R.id.textViewTransactionNote);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewTransactionNote?.text = items[position].note;
    }

    override fun getItemCount() = items.size
}