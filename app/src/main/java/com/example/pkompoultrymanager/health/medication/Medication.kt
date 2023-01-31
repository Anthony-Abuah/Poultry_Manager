package com.example.pkompoultrymanager.health.medication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Medication")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val medicationId: Int,
    val medicationName: String,
    val medicationDate: Date,
    val medicationAdministrator: String,
    val medicationCost: Double,
    val medicatedFlock: String,
    val numberOfMedicatedBirds: Int,
    val medicatedDisease: String,
    val shortNotes: String
)
