package com.example.fintracker_app.screens

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fintracker_app.R
import com.example.fintracker_app.model.TransactionCollection
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.services.TransactionCategoriesService
import com.example.fintracker_app.services.getRandomColor
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class StatsActivity : AppCompatActivity() {

    private lateinit var pie : AnimatedPieView;

    private var itemList: MutableList<TransactionModel> = mutableListOf();

    private lateinit var transactionCategoriesService: TransactionCategoriesService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        initServices();
        findViews();
        getItemListFromTransactionCollection();
        drawPie();
    }

    private fun getItemListFromTransactionCollection() {
        if(intent.hasExtra("TransactionCollection")) {
            val collection = intent.getSerializableExtra("TransactionCollection") as TransactionCollection;
            itemList = collection.items;
        }
    }

    private fun drawPie() {
        val config = AnimatedPieViewConfig();
        val angle : Float = -90F;
        config.startAngle(angle)
            .duration(1000)
            .drawText(true)
            .strokeMode(false)
            .textSize(30f);

        addPieData(config);
        pie.applyConfig(config);
        pie.start();
    }

    private fun addPieData(config: AnimatedPieViewConfig) {
        val totalAmount = getTransactionAmount(itemList);
        val categories = transactionCategoriesService.getAll();
        for(category in categories) {
            val transactions = sortTransactionsByCategory(category.row_id);
            val amount = getTransactionAmount(transactions);
            val percentage = (amount / totalAmount * 100).toDouble()
            val label = "${category.name} (${String.format("%.1f", percentage)}%)";
            config.addData(SimplePieInfo(percentage, getRandomColor(), label));
        }
    }

    private fun sortTransactionsByCategory(categoryId: Int): MutableList<TransactionModel> {
        val sortedItems: MutableList<TransactionModel> = mutableListOf();
        for(item in itemList) {
            if(item.category_id == categoryId)
                sortedItems.add(item);
        }
        return sortedItems;
    }

    private fun getTransactionAmount(transactions: MutableList<TransactionModel>): Float {
        var amount = 0.0f;
        for(item in transactions) {
            amount += item.amount;
        }
        return amount;
    }

    private fun initServices() {
        transactionCategoriesService = TransactionCategoriesService(applicationContext);
    }

    private fun findViews() {
        pie = findViewById(R.id.animatedPieView);
    }
}