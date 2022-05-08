package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.ErrorModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.database.*
import com.google.gson.Gson

class WalletsService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    private val transactionsService: TransactionsService = TransactionsService(context);
    private val snapshotService: SnapshotsService = SnapshotsService(context);

    fun getAll(): MutableList<WalletModel> {
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

    fun create(id: Int?, name: String, currencyId: Int, userId: Int): WalletModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            if(id != null) values.put(WALLET_ID, id)
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

    fun edit(id: Int, newName: String, newCurrencyId: Int, newUserId: Int): WalletModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "$WALLET_ID = ?";
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

    fun delete(id: Int): Boolean {
        val db = dbHelper.writableDatabase;
        return try {
            val selection = "$WALLET_ID = ?";
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

    fun findByName(nameInput: String): WalletModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var wallet: WalletModel? = null;
        try {
            val selection = "$WALLET_NAME = ?";
            val selectionArgs = arrayOf(nameInput);
            cursor = db.query(TABLE_WALLETS, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, WALLET_ID);
                val name = cursor.getValueString(context, WALLET_NAME);
                val currencyId = cursor.getValueInteger(context, WALLET_CURRENCY);
                val userId = cursor.getValueInteger(context, WALLET_USER);
                wallet = WalletModel(id, name, currencyId, userId);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return wallet;
    }

    fun findById(idInput: Int): WalletModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var wallet: WalletModel? = null;
        try {
            val selection = "$WALLET_ID = ?";
            val selectionArgs = arrayOf(idInput.toString());
            cursor = db.query(TABLE_WALLETS, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, WALLET_ID);
                val name = cursor.getValueString(context, WALLET_NAME);
                val currencyId = cursor.getValueInteger(context, WALLET_CURRENCY);
                val userId = cursor.getValueInteger(context, WALLET_USER);
                wallet = WalletModel(id, name, currencyId, userId);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return wallet;
    }

    fun getAmount(walletId: Int): Float {
        val incomeAmount = transactionsService.getIncomeAmount(walletId);
        val spendingAmount = transactionsService.getSpendingAmount(walletId);
        return incomeAmount - spendingAmount;
    }

    suspend fun attachAllToSnapshot(token: String) {
        try {
            val repo = Repository()
            val models = getAll();
            for(model in models) {
                repo.createWallet("Bearer $token", model);
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        }
    }

    suspend fun applySnapshot(token: String, snapshotId: Int) {
        try {
            val snapshot = snapshotService.getOne(token, snapshotId);
            if(snapshot != null) {
                dbHelper.clearTableWallets();

                val repo = Repository();
                val models = repo.getAllWallets("Bearer $token", snapshot.id);
                for(model in models) {
                    println("TEST")
                    create(model.row_id, model.name, model.currency_id, snapshot.user_id);
                }
            }
        } catch (error: java.lang.Exception) {
            showMessage(context, error.message.toString());
        }
    }
}