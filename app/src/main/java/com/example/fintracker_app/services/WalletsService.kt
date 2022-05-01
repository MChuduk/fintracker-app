package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.base.CrudService
import com.example.fintracker_app.services.database.*

class WalletsService(val context: Context): CrudService<WalletModel> {

    private val dbHelper = DatabaseHelper(context);

    fun create(name: String, currencyId: Int, userId: Int): WalletModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            values.put(WALLET_NAME, name);
            values.put(WALLET_CURRENCY, currencyId);
            values.put(WALLET_USER, userId);
            val rowId = db.insertOrThrow(TABLE_WALLETS, null, values).toInt();
            WalletModel(rowId, name, currencyId, userId);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    override fun getAll(): MutableList<WalletModel> {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        val walletsList = mutableListOf<WalletModel>();
        try {
            cursor = db.query(TABLE_WALLETS, null, null, null, null, null, null);

            if(cursor.moveToFirst()) {
                do {
                    val id = cursor.getValueInteger(context, WALLET_ID);
                    val name = cursor.getValueString(context, WALLET_NAME);
                    val currencyId = cursor.getValueInteger(context, WALLET_CURRENCY);
                    val userId = cursor.getValueInteger(context, WALLET_USER);
                    walletsList.add(WalletModel(id, name, currencyId, userId));
                } while (cursor.moveToNext());
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return walletsList;
    }

    fun edit(id: Int, newName: String, newCurrencyId: Int, newUserId: Int): WalletModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "id = ?";
            val selectionArgs = arrayOf(id.toString());
            val values = ContentValues();
            values.put(WALLET_NAME, newName);
            values.put(WALLET_CURRENCY, newCurrencyId);
            values.put(WALLET_USER, newUserId);
            db.update(TABLE_WALLETS, values, selection, selectionArgs);
            WalletModel(id, newName, newCurrencyId, newUserId);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    override fun deleteById(id: Int): Boolean {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "id = ?";
            val selectionArs = arrayOf(id.toString());
            db.delete(TABLE_WALLETS, selection, selectionArs);
            true;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            false;
        } finally {
            db.close();
        }
    }
}