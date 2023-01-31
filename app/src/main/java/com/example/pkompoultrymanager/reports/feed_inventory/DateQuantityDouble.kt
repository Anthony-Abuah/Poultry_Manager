package com.example.pkompoultrymanager.reports.feed_inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class DateQuantityDouble(
    @ColumnInfo(name = "date")val date: Date,
    @ColumnInfo(name = "quantity")val quantity: Double
)
