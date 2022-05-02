package com.example.fintracker_app.services

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Spinner
import android.widget.Toast
import android.widget.SpinnerAdapter

fun showMessage(context: Context, message: String?){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
}

fun Spinner.selectItemByValue(value: String) {
    val adapter: SpinnerAdapter = this.adapter;
    for (position in 0 until adapter.count) {
        if ((adapter.getItem(position) as String) == value) {
            this.setSelection(position)
            break;
        }
    }
}

fun Cursor.getValueInteger(context: Context, columnName : String) : Int {
    val columnIndex = this.getColumnIndex(columnName);
    return this.getInt(columnIndex);
}

fun Cursor.getValueFloat(context: Context, columnName : String) : Float {
    val columnIndex = this.getColumnIndex(columnName);
    return this.getFloat(columnIndex);
}

fun Cursor.getValueString(context: Context, columnName : String) : String {
    val columnIndex = this.getColumnIndex(columnName);
    return this.getString(columnIndex);
}

class UtilsService {
    companion object {
        @SuppressLint("NewApi")
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
            return false
        }
    }
}