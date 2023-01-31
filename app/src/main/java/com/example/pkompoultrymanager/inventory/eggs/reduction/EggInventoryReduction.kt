package com.example.pkompoultrymanager.inventory.eggs.reduction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class EggInventoryReduction(
    @PrimaryKey(autoGenerate = true)
    val EggInventoryReductionId: Int,
    val DateReduced: Date,
    val ReductionReason: String,
    val EggType: String,
    val NumberOfEggs: Int,
    val EggCost: Double,
    val AmountPaid: Double,
    val AmountOwed: Double,
    val Customer: String,
    val ShortNotes: String
)
