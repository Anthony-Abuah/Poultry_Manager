package com.example.pkompoultrymanager.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val ReminderId: Int,
    val ReminderTitle: String,
    val ReminderDate: String,
    val ReminderTime: String,
    val ReminderFrequency: String,
    val ReminderOff: Boolean,
    val ReminderNotes: String
)
