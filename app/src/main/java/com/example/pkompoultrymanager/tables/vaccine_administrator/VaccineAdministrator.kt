package com.example.pkompoultrymanager.tables.customer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class VaccineAdministrator(
    @PrimaryKey(autoGenerate = true)
    val AdministratorId: Int,
    val AdministratorName: String,
    val AdministratorContact: String,
    val AdministratorLocation: String,
    val shortNotes: String
)
