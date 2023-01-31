package com.example.pkompoultrymanager.tables.my_farm

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class MyFarm(
    @PrimaryKey(autoGenerate = true)
    val FarmId: Int,
    val FarmName: String,
    val FarmContact: String,
    val FarmLocation: String,
    val FarmOwner: String,
    val Currency: String,
    val MeasuringUnit: String,
    val NumberOfEggsPerCrate: Int,
    val NumberOfEmployees: Int,
    val ShortNotes: String
)
