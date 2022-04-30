package com.example.fintracker_app.services.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fintracker_app.dbName
import com.example.fintracker_app.dbVersion

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, dbName, null, dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        createTableCurrency(db);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTableCurrency(db);
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    private fun createTableCurrency(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_CURRENCY (" +
                "$CURRENCY_ID INTEGER PRIMARY KEY, " +
                "$CURRENCY_NAME TEXT UNIQUE NOT NULL, " +
                "$CURRENCY_EXCHANGE_RATE REAL NOT NULL);");
    }

    private fun dropTableCurrency(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CURRENCY");
    }
}