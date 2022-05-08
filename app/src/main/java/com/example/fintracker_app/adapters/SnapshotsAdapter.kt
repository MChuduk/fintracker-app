package com.example.fintracker_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.SnapshotModel

class SnapshotsAdapter(private var items: List<SnapshotModel>) :
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