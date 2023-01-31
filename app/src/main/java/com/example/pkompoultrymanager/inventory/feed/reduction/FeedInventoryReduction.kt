package com.example.pkompoultrymanager.inventory.feed.reduction

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FeedInventoryReduction(
    @PrimaryKey(autoGenerate = true)
    val FeedInventoryReductionId: Int,
    val ReductionDate: Date,
    val FeedName: String,
    val FlockName: String,
    val ReductionQuantity: Double,
    val ReductionReason: String,
    val ShortNotes: String
)
