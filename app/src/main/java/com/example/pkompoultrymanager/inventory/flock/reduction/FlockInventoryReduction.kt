package com.example.pkompoultrymanager.inventory.flock.reduction

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FlockInventoryReduction(
    @PrimaryKey(autoGenerate = true)
    val FlockInventoryReductionId: Int,
    val DateReduced: Date,
    val FlockName: String,
    val Customer: String,
    val ReductionReason: String,
    val ReductionQuantity: Int,
    val FlockCost: Double,
    val AmountReceived: Double,
    val AmountOwed: Double,
    val ShortNotes: String
)
