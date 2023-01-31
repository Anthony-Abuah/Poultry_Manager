package com.example.pkompoultrymanager.inventory.flock

import android.os.Bundle
import java.sql.Date

interface FlockCommunicator {
    fun passFlockInventoryAddition(
         FlockInventoryAdditionId: String,
         DateAdded: String,
         FlockName: String,
         FlockBreed: String,
         FlockSource: String,
         FlockPurpose: String,
         FlockQuantity: String,
         FlockAge: String,
         FlockCost: String,
         ShortNotes: String
    )
    fun passFlockInventoryReduction(
          FlockInventoryReductionId: String,
          DateReduced: String,
          FlockName: String,
          Customer: String,
          ReductionReason: String,
          ReductionQuantity: String,
          FlockCost: String,
          AmountReceived: String,
          ShortNotes: String
    )
}