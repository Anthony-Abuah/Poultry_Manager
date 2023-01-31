package com.example.pkompoultrymanager.transactions.cash_in.investment

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Investment")
data class Investment(
    @PrimaryKey(autoGenerate = true)
    val InvestmentId: Int,
    val Date: Date,
    val Investor: String,
    val AmountInvested: Double,
    val TransactionID: String,
    val shortNotes: String
)
