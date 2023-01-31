package com.example.pkompoultrymanager.inventory.eggs.reduction

import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import java.sql.Date

class EggInventoryReductionRepository(private val eggInventoryReductionDao: EggInventoryReductionDao) {

    val displayAllEggInventoryReduction: LiveData<List<EggInventoryReduction>> =
    eggInventoryReductionDao.displayAllEggInventoryReduction()

    val allEggSales: LiveData<Double> = eggInventoryReductionDao.allEggSales()

    fun displayEggInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>> =
        eggInventoryReductionDao.displayEggInventoryReductionByDate(number, firstDate, lastDate)


    fun eggSalesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        eggInventoryReductionDao.eggSalesPerDuration(firstDate, lastDate)

    suspend fun eggReductionsByDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger> =
        eggInventoryReductionDao.eggReductionsByDate(firstDate, lastDate)

    suspend fun eggReductionsByReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityInteger> =
        eggInventoryReductionDao.eggReductionsByReductionReason(firstDate, lastDate)

    fun numberOfEggsSoldPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> =
        eggInventoryReductionDao.numberOfEggsSoldPerDuration(firstDate, lastDate)

    val eggReductionsByDate: LiveData<List<DateQuantityInteger>> =
        eggInventoryReductionDao.eggReductionsByDate()

    val eggReductionsByReductionReason: LiveData<List<NameQuantityInteger>> =
        eggInventoryReductionDao.eggReductionsByReductionReason()

    val allEggsSoldByDate: LiveData<List<DateQuantityAmount>> =
        eggInventoryReductionDao.allEggsSoldByDate()


    fun displayEggInventoryReductionByReductionReason(reductionReason: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>> =
        eggInventoryReductionDao.displayEggInventoryReductionByReductionReason(reductionReason, firstDate, lastDate)

    suspend fun addEggInventory(eggInventoryReduction: EggInventoryReduction){
        eggInventoryReductionDao.addEggInventoryReduction(eggInventoryReduction)
    }
    suspend fun sumOfEggReductionsByEggType(eggType: String): Int? =
        eggInventoryReductionDao.sumOfEggReductionsByEggType(eggType)

    suspend fun eggsSoldByDate(firstDate: Date, lastDate: Date): List<DateQuantityAmount> =
        eggInventoryReductionDao.eggsSoldByDate(firstDate, lastDate)

    suspend fun updateEggInventoryReduction(eggInventoryReductionId: Int, dateReduced: Date, reductionReason: String, eggType: String, no_OfEggs: Int, customer:String, eggCost:Double, amountPaid:Double, amountOwed:Double, shortNotes:String){
        eggInventoryReductionDao.updateEggInventoryReduction(eggInventoryReductionId, dateReduced, reductionReason, eggType, no_OfEggs, customer, eggCost, amountPaid, amountOwed, shortNotes)
    }

     fun deleteAll(){
        eggInventoryReductionDao.deleteAll()
    }
    suspend fun delete(eggInventoryReduction: EggInventoryReduction){
        eggInventoryReductionDao.delete(eggInventoryReduction)
    }

}