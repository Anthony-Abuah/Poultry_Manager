package com.example.pkompoultrymanager.inventory.eggs.addition


import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import java.sql.Date

class EggInventoryAdditionRepository(private val eggInventoryAdditionDao: EggInventoryAdditionDao) {


    val displayAllEggInventoryAddition: LiveData<List<EggInventoryAddition>> =
        eggInventoryAdditionDao.displayEggAllInventoryAddition()

    val numberOfAllEggsAdded: LiveData<Int> =
        eggInventoryAdditionDao.numberOfAllEggsAdded()


    fun displayAllEggInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> =
        eggInventoryAdditionDao.displayEggInventoryAdditionByDate(number, firstDate, lastDate)

    fun displayEggInventoryAdditionByFlock(flock: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> =
        eggInventoryAdditionDao.displayEggInventoryAdditionByFlock(flock, firstDate, lastDate)

    fun displayEggInventoryAdditionByEggType(eggType: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> =
        eggInventoryAdditionDao.displayEggInventoryAdditionByEggType(eggType, firstDate, lastDate)

    fun numberOfEggsAddedPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> =
        eggInventoryAdditionDao.numberOfEggsAddedPerDuration(firstDate, lastDate)



    suspend fun eggAdditionsByDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger> =
        eggInventoryAdditionDao.eggAdditionsByDate(firstDate, lastDate)

    val eggAdditionsByDate: LiveData<List<DateQuantityInteger>> =
        eggInventoryAdditionDao.eggAdditionsByDate()

    suspend fun sumOfAllEggAdditionByEggType(eggType: String): Int? =
        eggInventoryAdditionDao.sumOfAllEggAdditionByEggType(eggType)


    suspend fun addEggInventory(eggInventoryAddition: EggInventoryAddition){
        eggInventoryAdditionDao.addEggInventory(eggInventoryAddition)
    }

    suspend fun updateEggInventoryAdditionInfo(eggInventoryAdditionId: Int, dateCollected: Date, flock: String, eggType: String, eggQuantity: Int, shortNotes:String){
        eggInventoryAdditionDao.updateEggInventoryAdditionInfo(eggInventoryAdditionId, dateCollected, flock, eggType, eggQuantity, shortNotes)
    }

     fun deleteAll(){
        eggInventoryAdditionDao.deleteAll()
    }
    suspend fun delete(eggInventoryAddition: EggInventoryAddition){
        eggInventoryAdditionDao.delete(eggInventoryAddition)
    }
    suspend fun deleteEggInventory(eggInventoryAdditionId: Int){
        eggInventoryAdditionDao.deleteEggInventory(eggInventoryAdditionId)
    }

}