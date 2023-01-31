package com.example.pkompoultrymanager.inventory.flock.reduction

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import java.sql.Date


@Dao
interface FlockInventoryReductionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFlockInventoryReduction(flockInventoryReduction: FlockInventoryReduction)

    @Query("SELECT * FROM FlockInventoryReduction ORDER BY DateReduced DESC ")
    fun displayAllFlockInventoryReduction(): LiveData<List<FlockInventoryReduction>>

    @Query("SELECT SUM(ReductionQuantity)  FROM FlockInventoryReduction WHERE FlockName = :flockName ORDER BY DateReduced DESC ")
    suspend fun sumFlock(flockName: String): Int

    @Query("SELECT * FROM FlockInventoryReduction WHERE  DateReduced BETWEEN :firstDate AND :lastDate ORDER BY DateReduced DESC LIMIT :number")
    fun displayFlockInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryReduction>>

    @Query("UPDATE FlockInventoryReduction SET FlockName=:flockName, DateReduced=:dateReduced, ReductionReason=:reductionReason, ReductionQuantity=:reductionQuantity, Customer=:customer, AmountReceived=:amountReceived, AmountOwed=:amountOwed, FlockCost=:flockCost, ShortNotes=:shortNotes WHERE FlockInventoryReductionId LIKE :flockInventoryReductionID")
    suspend fun updateFlockInventoryReduction( flockInventoryReductionID: Int, flockName: String, dateReduced: Date, reductionQuantity: Int, customer:String, flockCost:Double, amountReceived:Double, amountOwed:Double, reductionReason:String, shortNotes:String)

    @Delete
    suspend fun delete(flockInventoryReduction: FlockInventoryReduction)

    @Query("DELETE FROM FlockInventoryReduction")
    fun deleteAll()


    @Query("SELECT DateReduced AS date, SUM(ReductionQuantity) AS quantity FROM FlockInventoryReduction GROUP BY DateReduced")
    fun allFlockQuantityForDate(): LiveData<List<DateQuantityInteger>>

    @Query("SELECT DateReduced AS date, SUM(ReductionQuantity) AS quantity FROM FlockInventoryReduction WHERE DateReduced BETWEEN :firstDate AND :lastDate GROUP BY DateReduced")
    suspend fun flockQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger>

    @Query("SELECT ReductionReason AS name, SUM(ReductionQuantity) AS quantity FROM FlockInventoryReduction GROUP BY ReductionReason")
    fun allFlockQuantityForReductionReason(): LiveData<List<NameQuantityInteger>>

    @Query("SELECT ReductionReason AS name, SUM(ReductionQuantity) AS quantity FROM FlockInventoryReduction WHERE DateReduced BETWEEN :firstDate AND :lastDate GROUP BY ReductionReason")
    suspend fun flockQuantityForReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityInteger>

    @Query("SELECT Customer AS name, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS cost FROM FlockInventoryReduction WHERE ReductionReason LIKE '%old%' GROUP BY Customer")
    fun allFlockQuantityCostForCustomer(): LiveData<List<NameQuantityCost>>

    @Query("SELECT Customer AS name, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS cost  FROM FlockInventoryReduction WHERE ReductionReason LIKE '%old%' AND DateReduced BETWEEN :firstDate AND :lastDate GROUP BY Customer")
    suspend fun flockQuantityCostForCustomer(firstDate: Date, lastDate: Date): List<NameQuantityCost>

    @Query("SELECT FlockName AS name, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS cost FROM FlockInventoryReduction GROUP BY FlockName")
    fun allFlockQuantityCostForFlockName(): LiveData<List<NameQuantityCost>>

    @Query("SELECT FlockName AS name, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS cost FROM FlockInventoryReduction WHERE  DateReduced BETWEEN :firstDate AND :lastDate GROUP BY FlockName")
    suspend fun flockQuantityCostForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityCost>

    @Query("SELECT DateReduced AS date, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS amount FROM FlockInventoryReduction GROUP BY FlockName")
    fun allFlockSalesFlockDate(): LiveData<List<DateQuantityAmount>>

    @Query("SELECT DateReduced AS date, SUM(ReductionQuantity) AS quantity, SUM(FlockCost) AS amount FROM FlockInventoryReduction WHERE  DateReduced BETWEEN :firstDate AND :lastDate GROUP BY FlockName")
    suspend fun flockSalesFlockDate(firstDate: Date, lastDate: Date): List<DateQuantityAmount>

    @Query("SELECT SUM(ReductionQuantity) FROM FlockInventoryReduction WHERE FlockCost > 0.0 AND DateReduced BETWEEN :firstDate AND :lastDate ")
    fun numberOfSoldFlockPerDuration(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(ReductionQuantity) FROM FlockInventoryReduction WHERE ReductionReason LIKE '%eath%' AND DateReduced BETWEEN :firstDate AND :lastDate ")
    fun numberOfDeadFlockPerDuration(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(FlockCost) FROM FlockInventoryReduction WHERE FlockCost > 0.0 AND DateReduced BETWEEN :firstDate AND :lastDate ")
    fun flockSalesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(FlockCost) FROM FlockInventoryReduction")
    fun allFlockSales(): LiveData<Double>


}