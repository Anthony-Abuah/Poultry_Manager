package com.example.pkompoultrymanager.inventory.eggs.addition

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class EggInventoryAddition(
    @PrimaryKey(autoGenerate = true)
    val EggInventoryAdditionId: Int,
    val DateCollected: Date,
    val Flock: String,
    val EggType: String,
    val EggQuantity: Int,
    val ShortNotes: String
)
