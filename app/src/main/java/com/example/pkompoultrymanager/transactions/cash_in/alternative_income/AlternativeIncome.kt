package com.example.pkompoultrymanager.transactions.cash_in.alternative_income

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class AlternativeIncome(
    @PrimaryKey(autoGenerate = true)
    val AlternativeIncomeId: Int,
    val Date: Date,
    val ItemPurchased: String,
    val Customer: String,
    val ItemPurchased_Quantity: Int,
    val ItemPurchased_Cost: Double,
    val AmountReceived: Double,
    val AmountOwed: Double,
    val ReceiptNumber: String,
    val shortNotes: String
)
