package com.example.pkompoultrymanager.tables.feed

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class Feed(
    @PrimaryKey(autoGenerate = true)
    val FeedId: Int,
    val FeedName: String,
    val FeedType: String,
    val FeedBrand: String,
    val FeedSource: String,
    val ShortNotes: String
)
