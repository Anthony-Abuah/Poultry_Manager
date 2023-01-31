package com.example.pkompoultrymanager.inventory.eggs.addition

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import java.sql.Date


@Dao
interface EggInventoryAdditionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addEggInventory(eggInventoryAddition: EggInventoryAddition)

    @Query("SELECT * FROM EggInventoryAddition ORDER BY DateCollected DESC")
    fun displayEggAllInventoryAddition(): LiveData<List<EggInventoryAddition>>

    @Query("SELECT SUM(EggQuantity) FROM EggInventoryAddition WHERE EggType =:eggType")
    suspend fun sumOfAllEggAdditionByEggType(eggType: String): Int

    @Query("SELECT DateCollected AS date, SUM(EggQuantity) AS quantity FROM EggInventoryAddition WHERE DateCollected BETWEEN :firstDate AND :lastDate GROUP BY DateCollected ")
    suspend fun eggAdditionsByDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger>

    @Query("SELECT DateCollected AS date, SUM(EggQuantity) AS quantity FROM EggInventoryAddition GROUP BY DateCollected")
    fun eggAdditionsByDate(): LiveData<List<DateQuantityInteger>>

    @Query("SELECT * FROM EggInventoryAddition WHERE DateCollected BETWEEN :firstDate AND :lastDate  ORDER BY DateCollected DESC LIMIT :number ")
    fun displayEggInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>>

    @Query("UPDATE EggInventoryAddition SET DateCollected=:dateCollected, Flock=:flock, EggType=:eggType, EggQuantity=:eggQuantity, shortNotes=:shortNotes WHERE EggInventoryAdditionId LIKE :eggInventoryAdditionId")
    suspend fun updateEggInventoryAdditionInfo(eggInventoryAdditionId: Int, dateCollected: Date, flock: String, eggType: String, eggQuantity: Int, shortNotes:String)

    @Delete
    suspend fun delete(eggInventoryAddition: EggInventoryAddition)

    @Query("DELETE FROM EggInventoryAddition WHERE EggInventoryAdditionId LIKE :eggInventoryAdditionId")
    suspend fun deleteEggInventory(eggInventoryAdditionId: Int)

    @Query("DELETE FROM EggInventoryAddition")
    fun deleteAll()


    @Query("SELECT * FROM EggInventoryAddition WHERE Flock LIKE :flock AND  DateCollected BETWEEN :firstDate AND :lastDate  ORDER BY DateCollected ASC ")
    fun displayEggInventoryAdditionByFlock(flock: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>>

    @Query("SELECT * FROM EggInventoryAddition WHERE EggType LIKE :eggType AND  DateCollected BETWEEN :firstDate AND :lastDate  ORDER BY DateCollected ASC ")
    fun displayEggInventoryAdditionByEggType(eggType: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>>

    @Query("SELECT SUM(EggQuantity) FROM EggInventoryAddition")
    fun numberOfAllEggsAdded(): LiveData<Int>


    @Query("SELECT SUM(EggQuantity) FROM EggInventoryAddition WHERE DateCollected BETWEEN :firstDate AND :lastDate")
    fun numberOfEggsAddedPerDuration(firstDate: Date, lastDate: Date): LiveData<Int>


}