package com.example.fintracker_app.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.fintracker_app.R
import com.example.fintracker_app.model.TransactionCollection
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.services.*
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo

class StatsActivity : AppCompatActivity() {

    private lateinit var totalTransactionAmount: TextView;
    private lateinit var maxTransactionAmount: TextView;
    private lateinit var minTransactionAmount: TextView;
    private lateinit var spinnerType: Spinner;
    private lateinit var pie : AnimatedPieView;

    private var itemList: MutableList<TransactionModel> = mutableListOf();

    private lateinit var currencyService: CurrencyService;
    private lateinit var walletsService: WalletsService;
    private lateinit var transactionTypesService: TransactionsTypesService;
    private lateinit var transactionCategoriesService: TransactionCategoriesService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        initServices();
        findViews();
        getItemListFromTransactionCollection();
        setTransactionTypeSpinnerValues();
        drawPie(itemList);
    }

    private fun getItemListFromTransactionCollection() {
        if(intent.hasExtra("TransactionCollection")) {
            val collection = intent.getSerializableExtra("TransactionCollection") as TransactionCollection;
            itemList = collection.items;
        }
    }

    private fun drawPie(items: MutableList<TransactionModel>) {
        var sortedItems = items;
        val typeId = transactionTypesService.findByName(spinnerType.selectedItem.toString())?.id;
        sortedItems = sortByTransactionType(typeId!!, sortedItems)

        val totalAmount = getTotalTransactionAmount(sortedItems);
        totalTransactionAmount.text = "Общая сумма транзакий: ${String.format("%.1f", totalAmount)} USD";
        val maxAmount = getMaxTransactionAmount(sortedItems);
        maxTransactionAmount.text = "Максимальная сумма транзакции: ${String.format("%.1f", maxAmount)} USD"
        val minAmount = getMinTransactionAmount(sortedItems);
        minTransactionAmount.text = "Минимальная сумма транзакции: ${String.format("%.1f", minAmount)} USD"

        val config = AnimatedPieViewConfig();
        val angle : Float = -90F;
        config.startAngle(angle)
            .duration(1000)
            .drawText(true)
            .strokeMode(false)
            .textSize(30f);

        addPieData(config, sortedItems);
        pie.applyConfig(config);
        pie.start();
    }

    private fun addPieData(config: AnimatedPieViewConfig, items: MutableList<TransactionModel>) {
        val totalAmount = getTotalTransactionAmount(items);
        val categories = transactionCategoriesService.getAll();
        for(category in categories) {
            val transactions = sortTransactionsByCategory(category.row_id, items);
            val amount = getTotalTransactionAmount(transactions);
            val percentage = (amount / totalAmount * 100).toDouble()
            val label = "${category.name} (${String.format("%.1f", percentage)}%)";
            config.addData(SimplePieInfo(percentage, getRandomColor(), label));
        }
    }

    private fun sortTransactionsByCategory(categoryId: Int, items: MutableList<TransactionModel>): MutableList<TransactionModel> {
        val sortedItems: MutableList<TransactionModel> = mutableListOf();
        for(item in items) {
            if(item.category_id == categoryId)
                sortedItems.add(item);
        }
        return sortedItems;
    }

    private fun getTotalTransactionAmount(items: MutableList<TransactionModel>): Float {
        var amount = 0.0f;
        for(item in items) {
            val wallet = walletsService.findById(item.wallet_id);
            val currency = currencyService.findById(wallet!!.currency_id);
            val exchangeRate = currency!!.exchange_rate;
            amount += item.amount / exchangeRate;
        }
        return amount;
    }

    private fun getMinTransactionAmount(items: MutableList<TransactionModel>): Float {
        if(items.count() == 0) return 0f;
        var minAmount = Float.MAX_VALUE;
        for(item in items) {
            val wallet = walletsService.findById(item.wallet_id);
            val currency = currencyService.findById(wallet!!.currency_id);
            val exchangeRate = currency!!.exchange_rate;
            val amount = item.amount / exchangeRate;
            if(amount <= minAmount) {
                minAmount = amount;
            }
        }
        return minAmount;
    }

    private fun getMaxTransactionAmount(items: MutableList<TransactionModel>): Float {
        if(items.count() == 0) return 0f;
        var minAmount = Float.MIN_VALUE;
        for(item in items) {
            val wallet = walletsService.findById(item.wallet_id);
            val currency = currencyService.findById(wallet!!.currency_id);
            val exchangeRate = currency!!.exchange_rate;
            val amount = item.amount / exchangeRate;
            if(amount >= minAmount) {
                minAmount = amount;
            }
        }
        return minAmount;
    }

    private fun setTransactionTypeSpinnerValues() {
        val values = mutableListOf<String>();
        val itemsList = transactionTypesService.getAll();
        for(item in itemsList) {
            values.add(item.name);
        }
        spinnerType.adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, values);

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                drawPie(itemList);
            }
        }
    }

    private fun sortByTransactionType(typeId: Int, items: MutableList<TransactionModel>): MutableList<TransactionModel> {
        val sortedItems: MutableList<TransactionModel> = mutableListOf();
        for(item in items) {
            if(item.type_id == typeId) {
                sortedItems.add(item);
            }
        }
        return sortedItems;
    }

    private fun initServices() {
        currencyService = CurrencyService(applicationContext);
        walletsService = WalletsService(applicationContext);
        transactionTypesService = TransactionsTypesService(applicationContext);
        transactionCategoriesService = TransactionCategoriesService(applicationContext);
    }

    private fun findViews() {
        totalTransactionAmount = findViewById(R.id.textViewTotalTransactionAmount);
        maxTransactionAmount = findViewById(R.id.textViewMaxTransactionAmount);
        minTransactionAmount = findViewById(R.id.textViewMinTransactionAmount);
        spinnerType = findViewById(R.id.spinnerTransactionType);
        pie = findViewById(R.id.animatedPieView);
    }
}