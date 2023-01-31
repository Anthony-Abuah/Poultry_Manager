package com.example.pkompoultrymanager.tables.user_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val SupplierId: Int,
    val SupplierName: String,
    val SupplierContact: String,
    val SupplierLocation: String,
    val shortNotes: String
)
