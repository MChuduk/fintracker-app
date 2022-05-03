package com.example.fintracker_app.services.database

const val TABLE_CURRENCY = "currency";
const val CURRENCY_ID = "id"
const val CURRENCY_NAME = "name";
const val CURRENCY_EXCHANGE_RATE = "exchange_rate";

const val TABLE_WALLETS = "wallets";
const val WALLET_ID = "row_id";
const val WALLET_NAME = "name";
const val WALLET_CURRENCY = "currency_id";
const val WALLET_USER = "user_id";

const val TABLE_TRANSACTION_TYPES = "transaction_types";
const val TRANSACTION_TYPE_ID = "id";
const val TRANSACTION_TYPE_NAME = "name";

const val TABLE_TRANSACTION_CATEGORIES = "transaction_categories";
const val TRANSACTION_CATEGORY_ID = "row_id";
const val TRANSACTION_CATEGORY_NAME = "name";
const val TRANSACTION_CATEGORY_USER = "user_id";

const val TABLE_TRANSACTIONS = "transactions";
const val TRANSACTION_ID = "row_id";
const val TRANSACTION_TYPE = "type_id";
const val TRANSACTION_NOTE = "note";
const val TRANSACTION_AMOUNT = "amount";
const val TRANSACTION_DATE = "date";
const val TRANSACTION_WALLET = "wallet_id";
const val TRANSACTION_CATEGORY = "category_id";
const val TRANSACTION_REPEAT = "repeat";