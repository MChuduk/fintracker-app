package com.example.fintracker_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.TransactionCategoryModel

class TransactionCategoryAdapter(private var items: List<TransactionCategoryModel>) :
    RecyclerView.Adapter<TransactionCategoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewCategoryName: TextView? = null;

        init {
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_category_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewCategoryName?.text = items[position].name;
    }

    override fun getItemCount() = items.size
}