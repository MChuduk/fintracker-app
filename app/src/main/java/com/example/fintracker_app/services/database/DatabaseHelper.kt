package com.example.fintracker_app.services.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fintracker_app.dbName
import com.example.fintracker_app.dbVersion

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, dbName, null, dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        createTableCurrency(db);
        createTableWallets(db);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTableCurrency(db);
        dropTableWallets(db);
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

    private fun createTableWallets(db: SQLiteDatabase){
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_WALLETS (" +
                "$WALLET_ID INTEGER PRIMARY KEY, " +
                "$WALLET_NAME TEXT UNIQUE NOT NULL, " +
                "$WALLET_CURRENCY INTEGER NOT NULL, " +
                "$WALLET_USER INTEGER NOT NULL, " +
                "FOREIGN KEY ($WALLET_CURRENCY) REFERENCES $TABLE_CURRENCY ($CURRENCY_ID) ON DELETE CASCADE" +
                ")");
    }

    private fun dropTableWallets(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WALLETS");
    }
}