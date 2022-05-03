package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fintracker_app.model.*
import com.example.fintracker_app.services.database.*

class TransactionsTypesService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    init {
        create("Доход");
        create("Расход");
    }

    fun getAll(): MutableList<TransactionTypeModel> {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        val typesList = mutableListOf<TransactionTypeModel>();
        try {
            cursor = db.query(TABLE_TRANSACTION_TYPES, null, null, null, null, null, null);

            if(cursor.moveToFirst()) {
                do {
                    val id = cursor.getValueInteger(context, TRANSACTION_TYPE_ID);
                    val name = cursor.getValueString(context, TRANSACTION_TYPE_NAME);
                    typesList.add(TransactionTypeModel(id, name));
                } while (cursor.moveToNext());
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return typesList;
    }

    fun create(name: String): TransactionTypeModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            values.put(TRANSACTION_TYPE_NAME, name);
            val rowId = db.insertWithOnConflict(TABLE_TRANSACTION_TYPES, null, values,
                SQLiteDatabase.CONFLICT_IGNORE).toInt();
            TransactionTypeModel(rowId, name);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    fun findByName(nameInput: String): TransactionTypeModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var type: TransactionTypeModel? = null;
        try {
            val selection = "$TRANSACTION_TYPE_NAME = ?";
            val selectionArgs = arrayOf(nameInput);
            cursor = db.query(TABLE_TRANSACTION_TYPES, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, TRANSACTION_TYPE_ID);
                val name = cursor.getValueString(context, TRANSACTION_TYPE_NAME);
                type = TransactionTypeModel(id, name)
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return type;
    }

    fun findById(idInput: Int): TransactionTypeModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var type: TransactionTypeModel? = null;
        try {
            val selection = "$TRANSACTION_TYPE_ID = ?";
            val selectionArgs = arrayOf(idInput.toString());
            cursor = db.query(TABLE_TRANSACTION_TYPES, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, TRANSACTION_TYPE_ID);
                val name = cursor.getValueString(context, TRANSACTION_TYPE_NAME);
                type = TransactionTypeModel(id, name)
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return type;
    }
}