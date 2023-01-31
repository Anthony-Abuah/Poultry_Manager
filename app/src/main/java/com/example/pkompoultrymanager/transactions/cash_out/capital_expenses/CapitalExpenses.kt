package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class CapitalExpenses(
    @PrimaryKey(autoGenerate = true)
    val CapitalExpensesId: Int,
    val Date: Date,
    val ExpenseName: String,
    val PaymentMethod: String,
    val ExpenseAmount: Double,
    val TransactionID: String,
    val shortNotes: String
)
