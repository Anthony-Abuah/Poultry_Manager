package com.example.pkompoultrymanager.inventory.flock.addition

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FlockInventoryAddition(
    @PrimaryKey(autoGenerate = true)
    val FlockInventoryAdditionId: Int,
    val DateAdded: Date,
    val FlockName: String,
    val FlockBreed: String,
    val FlockSource: String,
    val FlockPurpose: String,
    val FlockQuantity: Int,
    val FlockAge: Int,
    val FlockCost: Double,
    val ShortNotes: String
)
