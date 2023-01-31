package com.example.pkompoultrymanager.inventory.flock.reduction

import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import java.sql.Date

class FlockInventoryReductionRepository(private val flockInventoryReductionDao: FlockInventoryReductionDao) {

    val displayAllFlockInventoryReduction: LiveData<List<FlockInventoryReduction>> =
        flockInventoryReductionDao.displayAllFlockInventoryReduction()

    val allFlockSales: LiveData<Double> =
        flockInventoryReductionDao.allFlockSales()

    fun displayFlockInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryReduction>> =
        flockInventoryReductionDao.displayFlockInventoryReductionByDate(number, firstDate, lastDate)

    suspend fun addFlockInventory(flockInventoryReduction: FlockInventoryReduction){
        flockInventoryReductionDao.addFlockInventoryReduction(flockInventoryReduction)
    }

    val allFlockQuantityForDate: LiveData<List<DateQuantityInteger>> =
        flockInventoryReductionDao.allFlockQuantityForDate()

    suspend fun flockQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger> =
        flockInventoryReductionDao.flockQuantityForDate(firstDate, lastDate)

    fun numberOfDeadFlockPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> =
        flockInventoryReductionDao.numberOfDeadFlockPerDuration(firstDate, lastDate)

    fun numberOfSoldFlockPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> =
        flockInventoryReductionDao.numberOfSoldFlockPerDuration(firstDate, lastDate)

    fun flockSalesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        flockInventoryReductionDao.flockSalesPerDuration(firstDate, lastDate)

    val allFlockQuantityForReductionReason: LiveData<List<NameQuantityInteger>> =
        flockInventoryReductionDao.allFlockQuantityForReductionReason()

    val allFlockSalesFlockDate: LiveData<List<DateQuantityAmount>> =
        flockInventoryReductionDao.allFlockSalesFlockDate()

    suspend fun flockSalesFlockDate(firstDate: Date, lastDate: Date): List<DateQuantityAmount> =
        flockInventoryReductionDao.flockSalesFlockDate(firstDate, lastDate)

    suspend fun flockQuantityForReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityInteger> =
        flockInventoryReductionDao.flockQuantityForReductionReason(firstDate, lastDate)

    val allFlockQuantityCostForCustomer: LiveData<List<NameQuantityCost>> =
        flockInventoryReductionDao.allFlockQuantityCostForCustomer()

    suspend fun flockQuantityCostForCustomer(firstDate: Date, lastDate: Date): List<NameQuantityCost> =
        flockInventoryReductionDao.flockQuantityCostForCustomer(firstDate, lastDate)

    val allFlockQuantityCostForFlockName: LiveData<List<NameQuantityCost>> =
        flockInventoryReductionDao.allFlockQuantityCostForFlockName()

    suspend fun flockQuantityCostForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityCost> =
        flockInventoryReductionDao.flockQuantityCostForFlockName(firstDate, lastDate)


    suspend fun updateFlockInventoryReduction(flockInventoryReductionID: Int, flockName: String, dateReduced: Date, reductionQuantity: Int, customer:String, flockCost:Double, amountReceived:Double, amountOwed:Double, reductionReason:String, shortNotes:String){
        flockInventoryReductionDao.updateFlockInventoryReduction(flockInventoryReductionID, flockName, dateReduced, reductionQuantity, customer, flockCost, amountReceived, amountOwed, reductionReason, shortNotes)
    }

     fun deleteAll(){
        flockInventoryReductionDao.deleteAll()
    }
    suspend fun delete(flockInventoryReduction: FlockInventoryReduction){
        flockInventoryReductionDao.delete(flockInventoryReduction)
    }

    suspend fun getSum(flockName: String): Int? {
        return flockInventoryReductionDao.sumFlock(flockName)
    }

}