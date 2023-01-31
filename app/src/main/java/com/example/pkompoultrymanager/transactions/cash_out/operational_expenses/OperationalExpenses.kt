package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class OperationalExpenses(
    @PrimaryKey(autoGenerate = true)
    val OperationalExpensesId: Int,
    val Date: Date,
    val ExpenseName: String,
    val PaymentMethod: String,
    val ExpenseAmount: Double,
    val TransactionID: String,
    val shortNotes: String
)
