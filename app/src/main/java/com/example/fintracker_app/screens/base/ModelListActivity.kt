package com.example.fintracker_app.screens.base

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.screens.MainActivity

open class ModelListActivity<T> : AppCompatActivity() {

    var itemList: MutableList<T> = mutableListOf();

    lateinit var progressBar: ProgressBar;
    lateinit var recyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);
        findView();

        val linearLayoutManager = LinearLayoutManager(applicationContext);
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.CreateItem -> onItemCreate();
            R.id.DeleteItem -> onItemDelete();
            R.id.EditItem -> onItemEdit();
            R.id.BackItem -> onItemBack();
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.EditItem)!!.isEnabled = (getSelectedItems().count() == 1);
        menu.findItem(R.id.DeleteItem)!!.isEnabled = (getSelectedItems().count() >= 1);
        return true;
    }

    open fun onItemCreate() { }

    open fun onItemDelete() { }

    open fun onItemEdit() { }

    open fun onItemBack() {
        val intent = Intent(applicationContext, MainActivity::class.java);
        startActivity(intent);
    }

    fun getSelectedItems(): MutableList<T> {
        val items: MutableList<T> = mutableListOf();
        for (index in itemList.indices) {
            val view = recyclerView.layoutManager?.findViewByPosition(index);
            val isSelected = view?.findViewById<CheckBox>(R.id.checkBoxWalletSelected)?.isChecked;
            if(isSelected == true) {
                items.add(itemList[index]);
            }
        }
        return items;
    }

    private fun findView() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
    }
}