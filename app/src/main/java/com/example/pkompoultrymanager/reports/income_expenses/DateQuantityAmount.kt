package com.example.pkompoultrymanager.reports.income_expenses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class DateQuantityAmount(
    @ColumnInfo(name = "date")val date: Date,
    @ColumnInfo(name = "quantity")val quantity: Int,
    @ColumnInfo(name = "amount")val amount: Double
)
