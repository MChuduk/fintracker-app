package com.example.fintracker_app.screens.transactions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.components.DatePicker
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.wallets.WalletsListActivity
import com.example.fintracker_app.services.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class TransactionsUpsertActivity : AppCompatActivity() {

    private lateinit var spinnerType: Spinner;
    private lateinit var spinnerWallet: Spinner;
    private lateinit var spinnerTransactionCategory: Spinner;
    private lateinit var editTextTransactionDate: EditText;
    private lateinit var editTextTransactionNote: EditText;
    private lateinit var editTextTransactionAmount: EditText;
    private lateinit var checkBoxTransactionRepeat: CheckBox;
    private lateinit var buttonDatePicker: Button;
    private lateinit var buttonConfirm: Button;

    private lateinit var datePicker: DatePicker;
    private lateinit var selectedTransaction: TransactionModel;
    private lateinit var transactionTypesService: TransactionsTypesService;
    private lateinit var walletsService: WalletsService;
    private lateinit var transactionCategoriesService: TransactionCategoriesService;
    private lateinit var transactionsService: TransactionsService;
    private lateinit var upsertMode: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_upsert);
        findViews();
        initServices();
        setUpsertMode();
        setTransactionTypeSpinnerValues();
        setWalletSpinnerValues();
        setTransactionCategorySpinnerValues();
        setSelectedTransaction();
    }

    fun onConfirmButtonClick (view: View) { confirm(); }

    private fun confirm() {
        try {
            if(spinnerType.selectedItem == null) throw Exception("Необходимо выбрать тип транзакции")
            if(spinnerWallet.selectedItem == null) throw Exception("Необходимо выбрать платежный кашелек")
            if(spinnerTransactionCategory.selectedItem == null) throw Exception("Необходимо выбрать категорию транзакции")

            val typeId = transactionTypesService.findByName(spinnerType.selectedItem.toString())?.id;
            val walletId = walletsService.findByName(spinnerWallet.selectedItem.toString())?.row_id
            val categoryId = transactionCategoriesService.findByName(spinnerTransactionCategory.selectedItem.toString())?.row_id

            val date = editTextTransactionDate.text.toString();
            val note = editTextTransactionNote.text.toString();
            val amount = editTextTransactionAmount.text.toString().toFloatOrNull()
                ?: throw Exception("Ошибка преобразования суммы транзакции");

            if (typeId == 2) {
                val walletAmount = walletsService.getAmount(walletId!!);
                val walletName = walletsService.findById(walletId)?.name;
                if(walletAmount - amount < 0)
                    throw Exception("На кашельке $walletName не достаточно средств для проведения данной транзакции");
            }

            val repeat = checkBoxTransactionRepeat.isChecked.toInt();

            var transaction: TransactionModel?;
            if(upsertMode == "Insert") {
                transaction = transactionsService.create(null, typeId!!, note, amount, date, walletId!!, categoryId!!, repeat);
            } else {
                transaction = transactionsService.edit(selectedTransaction.row_id, typeId!!, note, amount, date, walletId!!, categoryId!!, repeat);
            }
            if(transaction != null) {
                val intent = Intent(applicationContext, TransactionsListActivity::class.java);
                startActivity(intent);
            }

        } catch (error: Exception) {
            showMessage(applicationContext, error.message);
        }
    }

    private fun setUpsertMode() {
        if(intent.hasExtra("UpsertMode")) {
            upsertMode = intent.getStringExtra("UpsertMode")!!;
            if(upsertMode == "Insert") {
                buttonConfirm.text = "Создать";
            } else {
                buttonConfirm.text = "Изменить";
            }
        }
    }

    private fun setTransactionTypeSpinnerValues() {
        val values = mutableListOf<String>();
        val itemsList = transactionTypesService.getAll();
        for(item in itemsList) {
            values.add(item.name);
        }
        spinnerType.adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, values);
    }

    private fun setWalletSpinnerValues() {
        val values = mutableListOf<String>();
        val itemsList = walletsService.getAll();
        for(item in itemsList) {
            values.add(item.name);
        }
        spinnerWallet.adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, values);
    }

    private fun setTransactionCategorySpinnerValues() {
        val values = mutableListOf<String>();
        val itemsList = transactionCategoriesService.getAll();
        for(item in itemsList) {
            values.add(item.name);
        }
        spinnerTransactionCategory.adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, values);
    }

    private fun setSelectedTransaction() {
        if(intent.hasExtra("SelectedItem")) {
            selectedTransaction = intent.getSerializableExtra("SelectedItem") as TransactionModel;

            editTextTransactionNote.setText(selectedTransaction.note);
            editTextTransactionAmount.setText(selectedTransaction.amount.toString());
            editTextTransactionDate.setText(selectedTransaction.date);

            val typeName = transactionTypesService.findById(selectedTransaction.type_id)?.name;
            spinnerType.selectItemByValue(typeName!!);
            val walletName = walletsService.findById(selectedTransaction.wallet_id)?.name;
            spinnerWallet.selectItemByValue(walletName!!);
            val categoryName = transactionCategoriesService.findById(selectedTransaction.category_id)?.name;
            spinnerTransactionCategory.selectItemByValue(categoryName!!);

            checkBoxTransactionRepeat.isChecked = (selectedTransaction.repeat == 1);
        }
    }

    private fun initServices() {
        datePicker = DatePicker(this, editTextTransactionDate, buttonDatePicker);
        transactionTypesService = TransactionsTypesService(applicationContext);
        walletsService = WalletsService(applicationContext);
        transactionCategoriesService = TransactionCategoriesService(applicationContext);
        transactionsService = TransactionsService(applicationContext);
    }

    private fun findViews() {
        spinnerType = findViewById(R.id.spinnerTransactionType);
        spinnerWallet = findViewById(R.id.spinnerWallet);
        spinnerTransactionCategory = findViewById(R.id.spinnerTransactionCategory);
        editTextTransactionDate = findViewById(R.id.editTextTransactionDate);
        editTextTransactionNote = findViewById(R.id.editTextTransactionNote);
        editTextTransactionAmount = findViewById(R.id.editTextTransactionAmount);
        checkBoxTransactionRepeat = findViewById(R.id.checkBoxTransactioRepeat);
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonConfirm = findViewById(R.id.buttonConfirm);
    }
}