package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class LoanRepayment(
    @PrimaryKey(autoGenerate = true)
    val LoanRepaymentId: Int,
    val Date: Date,
    val Lender: String,
    val PaymentMethod: String,
    val AmountRepaid: Double,
    val TransactionID: String,
    val ShortNotes: String
)
