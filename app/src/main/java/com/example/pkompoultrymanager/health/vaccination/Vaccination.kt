package com.example.pkompoultrymanager.health.vaccination

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Vaccination")
data class Vaccination(
    @PrimaryKey(autoGenerate = true)
    val vaccinationId: Int,
    val vaccinationName: String,
    val vaccinationDate: Date,
    val vaccinationAdministrator: String,
    val vaccinationCost: Double,
    val vaccinatedFlock: String,
    val numberOfVaccinatedBirds: Int,
    val vaccinatedDisease: String,
    val shortNotes: String
)
