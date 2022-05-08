package com.example.fintracker_app.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.SnapshotModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.WalletsService

class SnapshotsAdapter(context: Context, private var items: List<SnapshotModel>) :
    RecyclerView.Adapter<SnapshotsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewSnapshotDate: TextView? = null;

        init {
            textViewSnapshotDate = itemView.findViewById(R.id.textViewSnapshotDate);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.snapshot_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val snapshot = items[position];
        holder.textViewSnapshotDate?.text = snapshot.created_at;
    }

    override fun getItemCount() = items.size
}