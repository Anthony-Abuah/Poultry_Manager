package com.example.pkompoultrymanager.inventory.feed

import android.os.Bundle
import java.sql.Date

interface FeedCommunicator {
    fun passFeedInventoryAddition(
         FeedInventoryAdditionId: String,
         DateAcquired: String,
         FeedName: String,
         FeedQuantity: String,
         FeedCost: String,
         FeedSource: String,
         ShortNotes: String
    )
    fun passFeedInventoryReduction(
         FeedInventoryReductionId: String,
         ReductionDate: String,
         FeedName: String,
         FlockName: String,
         ReductionQuantity: String,
         ReductionReason: String,
         ShortNotes: String
    )
}