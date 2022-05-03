package com.example.fintracker_app.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.WalletsService

class WalletsAdapter(context: Context, private var items: List<WalletModel>) :
    RecyclerView.Adapter<WalletsAdapter.MyViewHolder>() {

    private val walletsService: WalletsService = WalletsService(context);
    private val currencyService: CurrencyService = CurrencyService(context);

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewWalletName: TextView? = null;
        var textViewWalletAmount: TextView? = null;

        init {
            textViewWalletName = itemView.findViewById(R.id.textWalletName);
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

        val currency = currencyService.findById(items[position].currency_id);
        val amount = walletsService.getAmount(items[position].row_id)

        holder.textViewWalletAmount?.text = "$amount ${currency?.name}";

        val amountTextColor = if (amount >= 0.0f) Color.parseColor("#3bbf5e") else Color.parseColor("#d13058")
        holder.textViewWalletAmount?.setTextColor(amountTextColor);
    }

    override fun getItemCount() = items.size
}