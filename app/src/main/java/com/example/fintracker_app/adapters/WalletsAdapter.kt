package com.example.fintracker_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.WalletModel

class WalletsAdapter(private var items: List<WalletModel>) :
    RecyclerView.Adapter<WalletsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewWalletName: TextView? = null;
        var textViewWalletAmount: TextView? = null;

        init {
            textViewWalletName = itemView.findViewById(R.id.textViewWalletName);
            textViewWalletAmount = itemView.findViewById(R.id.textViewWalletAmount);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wallet_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewWalletName?.text = items[position].name;
    }

    override fun getItemCount() = items.size
}