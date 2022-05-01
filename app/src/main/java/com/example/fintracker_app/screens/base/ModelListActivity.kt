package com.example.fintracker_app.screens.base

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.model.BaseDataModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.MainActivity
import com.example.fintracker_app.services.base.CrudService

open class ModelListActivity<T> : AppCompatActivity() where T : BaseDataModel {

    lateinit var itemList: MutableList<T>;

    lateinit var recyclerView: RecyclerView;

    lateinit var service: CrudService<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);
        findView();

        val linearLayoutManager = LinearLayoutManager(applicationContext);
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun onResume() {
        super.onResume()
        itemList = service.getAll();
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

    open fun onItemDelete() {
        val items = getSelectedItems();
        for(item in items) {
            service.deleteById(item.id);
        }
        itemList = service.getAll();
    }

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
        recyclerView = findViewById(R.id.recyclerView);
    }
}