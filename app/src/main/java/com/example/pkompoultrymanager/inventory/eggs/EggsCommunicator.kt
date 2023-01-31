package com.example.pkompoultrymanager.inventory.eggs

import android.os.Bundle
import java.sql.Date

interface EggsCommunicator {
    fun passEggInventoryAddition(
        EggInventoryAdditionId: Int,
        DateCollected: String,
        Flock: String,
        EggType: String,
        NumberOfEggs: String,
        ShortNotes: String
    )
    fun passEggInventoryReduction(
         EggInventoryReductionId: String,
         DateReduced: String,
         ReductionReason: String,
         EggType: String,
         NumberOfEggs: String,
         EggCost: String,
         AmountPaid: String,
         Customer: String,
         ShortNotes: String
    )
}