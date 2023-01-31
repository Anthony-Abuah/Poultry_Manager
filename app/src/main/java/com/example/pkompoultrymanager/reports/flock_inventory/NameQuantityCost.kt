package com.example.pkompoultrymanager.reports.flock_inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class NameQuantityCost(
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "quantity")val quantity: Int,
    @ColumnInfo(name = "cost")val cost: Double
)
