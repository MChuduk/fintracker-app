package com.example.fintracker_app.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.TransactionCategoriesService
import com.example.fintracker_app.services.WalletsService

class TransactionsAdapter(context: Context, private var items: List<TransactionModel>) :
    RecyclerView.Adapter<TransactionsAdapter.MyViewHolder>() {

    private val walletsService = WalletsService(context);
    private val currencyService = CurrencyService(context);
    private val categoriesService = TransactionCategoriesService(context);

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTransactionNote: TextView? = null;
        var textViewTransactionAmount: TextView? = null;
        var textViewWalletName: TextView? = null;
        var imageViewRepeat: ImageView? = null;

        init {
            textViewTransactionNote = itemView.findViewById(R.id.textViewTransactionNote);
            textViewTransactionAmount = itemView.findViewById(R.id.textViewTransactionAmount);
            textViewWalletName = itemView.findViewById(R.id.textViewWalletName);
            imageViewRepeat = itemView.findViewById(R.id.imageViewRepeat);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wallet = walletsService.findById(items[position].wallet_id);
        val currency = currencyService.findById(wallet?.currency_id!!);
        val category = categoriesService.findById(items[position].category_id);

        holder.textViewTransactionNote?.text = items[position].note;

        val amount = items[position].amount;
        val type = items[position].type_id;
        val amountTextColor = if (type == 1) Color.parseColor("#3bbf5e") else Color.parseColor("#d13058")
        holder.textViewTransactionAmount?.setTextColor(amountTextColor);
        holder.textViewTransactionAmount?.text = "${if (type == 1) '+' else '-'}$amount ${currency?.name}";

        holder.textViewWalletName?.text = "${items[position].date} | ${category?.name} | ${wallet.name}";

        holder.imageViewRepeat?.visibility = if (items[position].repeat == 1) View.VISIBLE else View.INVISIBLE;
    }

    override fun getItemCount() = items.size
}