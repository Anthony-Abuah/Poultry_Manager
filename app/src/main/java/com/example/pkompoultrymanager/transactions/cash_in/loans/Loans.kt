package com.example.pkompoultrymanager.transactions.cash_in.loans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class Loans(
    @PrimaryKey(autoGenerate = true)
    val LoanId: Int,
    val Date: Date,
    val Lender: String,
    val LoanAmountReceived: Double,
    val TransactionID: String,
    val shortNotes: String
)
