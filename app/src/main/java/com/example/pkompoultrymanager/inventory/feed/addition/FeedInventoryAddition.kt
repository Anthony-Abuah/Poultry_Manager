package com.example.pkompoultrymanager.inventory.feed.addition

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FeedInventoryAddition(
    @PrimaryKey(autoGenerate = true)
    val FeedInventoryAdditionId: Int,
    val DateAcquired: Date,
    val FeedName: String,
    val FeedQuantity: Double,
    val FeedCost: Double,
    val FeedSource: String,
    val ShortNotes: String
)
