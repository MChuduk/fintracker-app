package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.database.*

class TransactionsService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    fun getAll(): MutableList<TransactionModel> {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        val transactionsList = mutableListOf<TransactionModel>();
        try {
            cursor = db.query(TABLE_TRANSACTIONS, null, null, null, null, null, null);

            if(cursor.moveToFirst()) {
                do {
                    println("test2")
                    val id = cursor.getValueInteger(context, TRANSACTION_ID);
                    val typeId = cursor.getValueInteger(context, TRANSACTION_TYPE);
                    val note = cursor.getValueString(context, TRANSACTION_NOTE);
                    val amount = cursor.getValueFloat(context, TRANSACTION_AMOUNT);
                    val date = cursor.getValueString(context, TRANSACTION_DATE);
                    val walletId = cursor.getValueInteger(context, TRANSACTION_WALLET);
                    val categoryId = cursor.getValueInteger(context, TRANSACTION_CATEGORY);
                    val repeat = cursor.getValueInteger(context, TRANSACTION_REPEAT);
                    transactionsList.add(TransactionModel(id, typeId, note, amount, date, walletId, categoryId, repeat));
                } while (cursor.moveToNext());
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return transactionsList;
    }

    fun create(id: Int?, typeId: Int, note: String, amount: Float,
               date: String, walletId: Int, categoryId: Int, repeat: Int): TransactionModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            if(id != null) values.put(TRANSACTION_ID, id)
            values.put(TRANSACTION_TYPE, typeId);
            values.put(TRANSACTION_NOTE, note);
            values.put(TRANSACTION_AMOUNT, amount);
            values.put(TRANSACTION_DATE, date);
            values.put(TRANSACTION_WALLET, walletId);
            values.put(TRANSACTION_CATEGORY, categoryId);
            values.put(TRANSACTION_REPEAT, repeat);
            val rowId = db.insertOrThrow(TABLE_TRANSACTIONS, null, values).toInt();
            TransactionModel(rowId, typeId, note, amount, date, walletId, categoryId, repeat);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    fun edit(id: Int, newTypeId: Int, newNote: String, newAmount: Float,
             newDate: String, newWalletId: Int, newCategoryId: Int, newRepeat: Int): TransactionModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "$TRANSACTION_ID = ?";
            val selectionArgs = arrayOf(id.toString());
            val values = ContentValues();
            values.put(TRANSACTION_TYPE, newTypeId);
            values.put(TRANSACTION_NOTE, newNote);
            values.put(TRANSACTION_AMOUNT, newAmount);
            values.put(TRANSACTION_DATE, newDate);
            values.put(TRANSACTION_WALLET, newWalletId);
            values.put(TRANSACTION_CATEGORY, newCategoryId);
            values.put(TRANSACTION_REPEAT, newRepeat);
            db.update(TABLE_TRANSACTIONS, values, selection, selectionArgs);
            TransactionModel(id, newTypeId, newNote, newAmount, newDate, newWalletId, newCategoryId, newRepeat);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    fun delete(id: Int): Boolean {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "$TRANSACTION_ID = ?";
            val selectionArs = arrayOf(id.toString());
            db.delete(TABLE_TRANSACTIONS, selection, selectionArs);
            true;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            false;
        } finally {
            db.close();
        }
    }

    fun getIncomeAmount(walletId: Int): Float {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var amount = 0.0f;
        try {
            cursor = db.rawQuery("SELECT SUM(amount) FROM $TABLE_TRANSACTIONS WHERE $TRANSACTION_WALLET = ? " +
                    "AND $TRANSACTION_TYPE = 1", arrayOf(walletId.toString()));
            if(cursor.moveToFirst()) {
                amount = cursor.getFloat(0);
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return amount;
    }

    fun getSpendingAmount(walletId: Int): Float {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var amount = 0.0f;
        try {
            cursor = db.rawQuery("SELECT SUM(amount) FROM $TABLE_TRANSACTIONS WHERE $TRANSACTION_WALLET = ? " +
                    "AND $TRANSACTION_TYPE = 2", arrayOf(walletId.toString()));
            if(cursor.moveToFirst()) {
                amount = cursor.getFloat(0);
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return amount;
    }
}