package com.example.pkompoultrymanager.inventory.eggs.reduction

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import java.sql.Date
/*

val date: LocalDate = LocalDate.now()
val month: LocalDate = LocalDate.now().minusMonths(1)
*/

@Dao
interface EggInventoryReductionDao {

    val month: String
        get() = "LocalDate"

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addEggInventoryReduction(eggInventoryReduction: EggInventoryReduction)

    @Query("SELECT * FROM EggInventoryReduction ORDER BY DateReduced DESC")
    fun displayAllEggInventoryReduction(): LiveData<List<EggInventoryReduction>>

    @Query("SELECT DateReduced AS date, SUM(NumberOfEggs) AS quantity FROM EggInventoryReduction GROUP BY DateReduced ")
    fun eggReductionsByDate(): LiveData<List<DateQuantityInteger>>

    @Query("SELECT ReductionReason AS name, SUM(NumberOfEggs) AS quantity FROM EggInventoryReduction GROUP BY ReductionReason ")
    fun eggReductionsByReductionReason(): LiveData<List<NameQuantityInteger>>

    @Query("SELECT DateReduced AS date, SUM(NumberOfEggs) AS quantity FROM EggInventoryReduction WHERE DateReduced BETWEEN :firstDate AND :lastDate GROUP BY DateReduced ")
    suspend fun eggReductionsByDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger>

    @Query("SELECT SUM(NumberOfEggs) FROM EggInventoryReduction WHERE EggCost > 0.0 AND DateReduced BETWEEN :firstDate AND :lastDate ")
    fun numberOfEggsSoldPerDuration(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT DateReduced AS date, SUM(NumberOfEggs) AS quantity, SUM(EggCost) as amount FROM EggInventoryReduction WHERE EggCost > 0.0 AND DateReduced BETWEEN :firstDate AND :lastDate GROUP BY DateReduced ORDER BY DateReduced ASC")
    suspend fun eggsSoldByDate(firstDate: Date, lastDate: Date): List<DateQuantityAmount>

    @Query("SELECT DateReduced AS date, SUM(NumberOfEggs) AS quantity, SUM(EggCost) as amount FROM EggInventoryReduction WHERE EggCost > 0.0 GROUP BY DateReduced ORDER BY DateReduced ASC")
    fun allEggsSoldByDate(): LiveData<List<DateQuantityAmount>>

    @Query("SELECT ReductionReason AS name, SUM(NumberOfEggs) AS quantity FROM EggInventoryReduction WHERE DateReduced BETWEEN :firstDate AND :lastDate GROUP BY ReductionReason ")
    suspend fun eggReductionsByReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityInteger>

    @Query("SELECT SUM(NumberOfEggs) FROM EggInventoryReduction WHERE EggType =:eggType")
    suspend fun sumOfEggReductionsByEggType(eggType: String): Int

    @Query("SELECT SUM(EggCost) FROM EggInventoryReduction WHERE EggCost > 0.0")
    fun allEggSales(): LiveData<Double>

    @Query("SELECT SUM(EggCost) FROM EggInventoryReduction WHERE EggCost > 0.0 AND DateReduced BETWEEN :firstDate AND :lastDate")
    fun eggSalesPerDuration(firstDate: Date,  lastDate: Date): LiveData<Double>

    @Query("SELECT * FROM EggInventoryReduction WHERE DateReduced BETWEEN :firstDate AND :lastDate  ORDER BY DateReduced DESC LIMIT :number")
    fun displayEggInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>>

    @Query("UPDATE EggInventoryReduction SET DateReduced=:dateReduced, ReductionReason=:reductionReason, EggType=:eggType, NumberOfEggs=:no_OfEggs, Customer=:customer, EggCost=:eggCost, AmountPaid=:amountPaid, AmountOwed=:amountOwed, shortNotes=:shortNotes WHERE EggInventoryReductionId LIKE :eggInventoryReductionId")
    suspend fun updateEggInventoryReduction(eggInventoryReductionId: Int, dateReduced: Date, reductionReason: String, eggType: String, no_OfEggs: Int, customer:String, eggCost:Double, amountPaid:Double, amountOwed:Double, shortNotes:String)

    @Delete
    suspend fun delete(eggInventoryReduction: EggInventoryReduction)

    @Query("DELETE FROM EggInventoryReduction")
    fun deleteAll()

    @Query("SELECT * FROM EggInventoryReduction WHERE ReductionReason LIKE :reductionReason AND DateReduced BETWEEN :firstDate AND :lastDate  ORDER BY DateReduced ASC ")
    fun displayEggInventoryReductionByReductionReason(reductionReason: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>>


}