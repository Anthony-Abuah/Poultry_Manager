package com.example.pkompoultrymanager.reports.feed_inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class NameQuantityDouble(
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: Double
)
