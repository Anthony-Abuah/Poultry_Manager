package com.example.pkompoultrymanager.inventory.flock.addition

import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import java.sql.Date
import java.time.LocalDate
import java.util.*

class FlockInventoryAdditionRepository(private val flockInventoryAdditionDao: FlockInventoryAdditionDao) {


    val displayFlockInventoryAddition: LiveData<List<FlockInventoryAddition>> =
        flockInventoryAdditionDao.displayAllFlockInventoryAddition()

    val allFlockNames: LiveData<List<String>> =
        flockInventoryAdditionDao.allFlockNames()

    fun displayFlockInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryAddition>> =
        flockInventoryAdditionDao.displayFlockInventoryAdditionByDate(number, firstDate, lastDate)


    fun displayFlockInventoryAdditionForFlockName(flockName: String): LiveData<List<FlockInventoryAddition>> =
       flockInventoryAdditionDao.displayFlockInventoryAdditionForFlockName(flockName)


    suspend fun flockAddedByFlockName(flockName: String): Int? =
       flockInventoryAdditionDao.flockAddedByFlockName(flockName)

    suspend fun flockPurposeByFlockName(flockName: String): String? =
       flockInventoryAdditionDao.flockPurposeByFlockName(flockName)

    val allFlockQuantityForDate: LiveData<List<DateQuantityInteger>> =
       flockInventoryAdditionDao.allFlockQuantityForDate()

    suspend fun flockQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger> =
       flockInventoryAdditionDao.flockQuantityForDate(firstDate, lastDate)

    val allFlockQuantityForFlockName: LiveData<List<NameQuantityInteger>> =
       flockInventoryAdditionDao.allFlockQuantityForFlockName()

    suspend fun flockQuantityForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityInteger> =
       flockInventoryAdditionDao.flockQuantityForFlockName(firstDate, lastDate)

    val allFlockQuantityCostForFlockPurpose: LiveData<List<NameQuantityCost>> =
       flockInventoryAdditionDao.allFlockQuantityCostForFlockPurpose()

    suspend fun flockQuantityCostForFlockPurpose(firstDate: Date, lastDate: Date): List<NameQuantityCost> =
       flockInventoryAdditionDao.flockQuantityCostForFlockPurpose(firstDate, lastDate)

    val allFlockQuantityCostForFlockName: LiveData<List<NameQuantityCost>> =
       flockInventoryAdditionDao.allFlockQuantityCostForFlockName()

    suspend fun flockQuantityCostForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityCost> =
       flockInventoryAdditionDao.flockQuantityCostForFlockName(firstDate, lastDate)


    suspend fun addFlockInventory(flockInventoryAddition: FlockInventoryAddition){
        flockInventoryAdditionDao.addFlockInventoryAddition(flockInventoryAddition)
    }

    suspend fun updateFlockInventoryAddition(flockInventoryAdditionID: Int, flockName: String, dateAdded: Date, flockQuantity: Double, flockSource:String, flockCost: Double, flockAge: Int, flockPurpose: String, flockBreed: String, shortNotes:String){
        flockInventoryAdditionDao.updateFlockInventoryAddition(flockInventoryAdditionID, flockName, dateAdded , flockQuantity, flockSource, flockCost, flockAge, flockPurpose, flockBreed, shortNotes)
    }

     fun deleteAll(){
        flockInventoryAdditionDao.deleteAll()
    }
    suspend fun delete(flockInventoryAddition: FlockInventoryAddition){
        flockInventoryAdditionDao.delete(flockInventoryAddition)
    }

}