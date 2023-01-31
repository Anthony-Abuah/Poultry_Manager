package com.example.pkompoultrymanager.tables.customer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val CustomerId: Int,
    val CustomerName: String,
    val CustomerContact: String,
    val CustomerLocation: String,
    val shortNotes: String
)
