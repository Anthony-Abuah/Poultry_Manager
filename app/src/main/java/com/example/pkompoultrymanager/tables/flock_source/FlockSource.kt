package com.example.pkompoultrymanager.tables.flock_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class FlockSource(
    @PrimaryKey(autoGenerate = true)
    val FlockSourceId: Int,
    val FlockSourceName: String,
    val FlockSourceContact: String,
    val FlockSourceLocation: String,
    val shortNotes: String
)
