package com.example.pkompoultrymanager.tables.feed_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FeedSource(
    @PrimaryKey(autoGenerate = true)
    val FeedSourceId: Int,
    val FeedSourceName: String,
    val FeedSourceContact: String,
    val FeedSourceLocation: String,
    val shortNotes: String
)
