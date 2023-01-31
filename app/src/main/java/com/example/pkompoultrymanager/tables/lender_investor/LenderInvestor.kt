package com.example.pkompoultrymanager.tables.lender_investor

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class LenderInvestor(
    @PrimaryKey(autoGenerate = true)
    val LenderInvestorId: Int,
    val LenderInvestorName: String,
    val LenderInvestorContact: String,
    val LenderInvestorLocation: String,
    val shortNotes: String
)
