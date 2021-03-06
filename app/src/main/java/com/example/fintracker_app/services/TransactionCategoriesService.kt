package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.model.TransactionCategoryModel
import com.example.fintracker_app.model.TransactionTypeModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.database.*

class TransactionCategoriesService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    private val snapshotService: SnapshotsService = SnapshotsService(context);

    fun getAll(): MutableList<TransactionCategoryModel> {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        val categoriesList = mutableListOf<TransactionCategoryModel>();
        try {
            cursor = db.query(TABLE_TRANSACTION_CATEGORIES, null, null, null, null, null, null);

            if(cursor.moveToFirst()) {
                do {
                    val id = cursor.getValueInteger(context, TRANSACTION_CATEGORY_ID);
                    val name = cursor.getValueString(context, TRANSACTION_CATEGORY_NAME);
                    val userId = cursor.getValueInteger(context, TRANSACTION_CATEGORY_USER);
                    categoriesList.add(TransactionCategoryModel(id, name, userId));
                } while (cursor.moveToNext());
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return categoriesList;
    }

    fun create(id: Int?, name: String, userId: Int): TransactionCategoryModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            if(id != null) values.put(TRANSACTION_CATEGORY_ID, id);
            values.put(TRANSACTION_CATEGORY_NAME, name);
            values.put(TRANSACTION_CATEGORY_USER, userId);
            val rowId = db.insert(TABLE_TRANSACTION_CATEGORIES, null, values).toInt();
            TransactionCategoryModel(rowId, name, userId);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    fun edit(id: Int, newName: String, newUser: Int): TransactionCategoryModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "$TRANSACTION_CATEGORY_ID = ?";
            val selectionArgs = arrayOf(id.toString());
            val values = ContentValues();
            values.put(TRANSACTION_CATEGORY_NAME, newName);
            values.put(TRANSACTION_CATEGORY_USER, newUser);
            db.update(TABLE_TRANSACTION_CATEGORIES, values, selection, selectionArgs);
            TransactionCategoryModel(id, newName, newUser);
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
            val selection = "$TRANSACTION_CATEGORY_ID = ?";
            val selectionArs = arrayOf(id.toString());
            db.delete(TABLE_TRANSACTION_CATEGORIES, selection, selectionArs);
            true;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            false;
        } finally {
            db.close();
        }
    }

    fun findByName(nameInput: String): TransactionCategoryModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var category: TransactionCategoryModel? = null;
        try {
            val selection = "$TRANSACTION_CATEGORY_NAME = ?";
            val selectionArgs = arrayOf(nameInput);
            cursor = db.query(TABLE_TRANSACTION_CATEGORIES, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, TRANSACTION_CATEGORY_ID);
                val name = cursor.getValueString(context, TRANSACTION_CATEGORY_NAME);
                val userId = cursor.getValueInteger(context, TRANSACTION_CATEGORY_USER);
                category = TransactionCategoryModel(id, name, userId);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return category;
    }

    fun findById(idInput: Int): TransactionCategoryModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var category: TransactionCategoryModel? = null;
        try {
            val selection = "$TRANSACTION_CATEGORY_ID = ?";
            val selectionArgs = arrayOf(idInput.toString());
            cursor = db.query(TABLE_TRANSACTION_CATEGORIES, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, TRANSACTION_CATEGORY_ID);
                val name = cursor.getValueString(context, TRANSACTION_CATEGORY_NAME);
                val userId = cursor.getValueInteger(context, TRANSACTION_CATEGORY_USER);
                category = TransactionCategoryModel(id, name, userId);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return category;
    }

    suspend fun attachAllToSnapshot(token: String) {
        try {
            val repo = Repository()
            val models = getAll();
            for(model in models) {
                repo.createTransactionCategory("Bearer $token", model);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        }
    }

    suspend fun applySnapshot(token: String, snapshotId: Int) {
        try {
            val snapshot = snapshotService.getOne(token, snapshotId);
            if(snapshot != null) {
                dbHelper.clearTableTransactionCategories();

                val repo = Repository();
                val response = repo.getAllTransactionCategories("Bearer $token", snapshot.id);
                val models = response.body();
                for(model in models!!) {
                    create(model.row_id, model.name, snapshot.user_id);
                }
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        }
    }
}