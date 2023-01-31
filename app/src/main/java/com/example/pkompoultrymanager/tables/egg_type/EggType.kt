package com.example.pkompoultrymanager.tables.egg_type

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class EggType(
    @PrimaryKey(autoGenerate = true)
    val EggTypeId: Int,
    val EggTypeName: String,
    val EggTypeDescription: String
)
