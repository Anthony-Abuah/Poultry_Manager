package com.example.pkompoultrymanager.reports.egg_inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class DateQuantityInteger(
    @ColumnInfo(name = "date")val date: Date,
    @ColumnInfo(name = "quantity")val quantity: Int,
)
