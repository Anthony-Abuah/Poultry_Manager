package com.example.pkompoultrymanager.reports.income_expenses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class NameQuantityAmount(
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: String,
    @ColumnInfo(name = "amount")val amount: String
)
