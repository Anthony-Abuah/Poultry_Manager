package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class AdvancePurchase(
    @PrimaryKey(autoGenerate = true)
    val AdvancePurchaseId: Int,
    val Date: Date,
    val Customer: String,
    val ItemPurchased: String,
    val ItemPurchased_Cost: Double,
    val AmountReceived: Double,
    val ReceiptNumber: String,

    val ItemDeliveryDate:Date?,
    val HasBeenDelivered: Boolean,
    val MoneyReturned:Boolean,
    val AmountReturned:Double,

    val shortNotes: String
)
