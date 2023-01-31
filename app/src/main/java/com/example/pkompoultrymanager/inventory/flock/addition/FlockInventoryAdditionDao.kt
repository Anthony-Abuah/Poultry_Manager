package com.example.pkompoultrymanager.inventory.flock.addition

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import java.sql.Date


@Dao
interface FlockInventoryAdditionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFlockInventoryAddition(flockInventoryAddition: FlockInventoryAddition)

    @Query("SELECT * FROM FlockInventoryAddition ORDER BY DateAdded DESC")
    fun displayAllFlockInventoryAddition(): LiveData<List<FlockInventoryAddition>>

    @Query("SELECT * FROM FlockInventoryAddition WHERE FlockName =:flockName")
    fun displayFlockInventoryAdditionForFlockName(flockName: String): LiveData<List<FlockInventoryAddition>>

    @Query("SELECT * FROM FlockInventoryAddition WHERE DateAdded BETWEEN :firstDate AND :lastDate ORDER BY DateAdded DESC LIMIT :number")
    fun displayFlockInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryAddition>>

    @Query("SELECT FlockName FROM FlockInventoryAddition ORDER BY DateAdded DESC")
    fun allFlockNames(): LiveData<List<String>>

    @Query("SELECT DateAdded as date, SUM(FlockQuantity) as quantity FROM FlockInventoryAddition GROUP BY DateAdded")
    fun allFlockQuantityForDate(): LiveData<List<DateQuantityInteger>>

    @Query("SELECT DateAdded as date, SUM(FlockQuantity) as quantity FROM FlockInventoryAddition WHERE DateAdded BETWEEN :firstDate AND :lastDate GROUP BY DateAdded")
    suspend fun flockQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityInteger>

    @Query("SELECT FlockName as name, FlockQuantity as quantity FROM FlockInventoryAddition")
    fun allFlockQuantityForFlockName(): LiveData<List<NameQuantityInteger>>

    @Query("SELECT FlockName as name, FlockQuantity as quantity FROM FlockInventoryAddition WHERE DateAdded BETWEEN :firstDate AND :lastDate")
    suspend fun flockQuantityForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityInteger>

    @Query("SELECT FlockName as name, FlockQuantity as quantity, FlockCost as cost FROM FlockInventoryAddition")
    fun allFlockQuantityCostForFlockName(): LiveData<List<NameQuantityCost>>

    @Query("SELECT FlockName as name, FlockQuantity as quantity, FlockCost as cost FROM FlockInventoryAddition WHERE DateAdded BETWEEN :firstDate AND :lastDate")
    suspend fun flockQuantityCostForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityCost>

    @Query("SELECT FlockPurpose as name, SUM(FlockQuantity) as quantity, SUM(FlockCost) as cost  FROM FlockInventoryAddition GROUP BY FlockPurpose")
    fun allFlockQuantityCostForFlockPurpose(): LiveData<List<NameQuantityCost>>

    @Query("SELECT FlockPurpose as name, SUM(FlockQuantity) as quantity, SUM(FlockCost) as cost FROM FlockInventoryAddition WHERE DateAdded BETWEEN :firstDate AND :lastDate GROUP BY FlockPurpose")
    suspend fun flockQuantityCostForFlockPurpose(firstDate: Date, lastDate: Date): List<NameQuantityCost>

    @Query("SELECT FlockPurpose FROM FlockInventoryAddition WHERE FlockName =:flockName")
    suspend fun flockPurposeByFlockName(flockName: String): String

    @Query("SELECT FlockQuantity FROM FlockInventoryAddition WHERE FlockName =:flockName ")
    suspend fun flockAddedByFlockName(flockName: String): Int

    @Query("UPDATE FlockInventoryAddition SET FlockName=:flockName, FlockCost=:flockCost, FlockQuantity=:flockQuantity, FlockSource=:flockSource, DateAdded=:dateAdded, FlockAge =:flockAge, FlockPurpose =:flockPurpose, FlockBreed =:flockBreed, ShortNotes=:shortNotes WHERE FlockInventoryAdditionId LIKE :flockInventoryAdditionID")
    suspend fun updateFlockInventoryAddition( flockInventoryAdditionID: Int, flockName: String, dateAdded: Date, flockQuantity: Double, flockSource:String, flockCost: Double, flockAge: Int, flockPurpose: String, flockBreed: String, shortNotes:String)

    @Delete
    suspend fun delete(flockInventoryAddition: FlockInventoryAddition)

    @Query("DELETE FROM FlockInventoryAddition")
    fun deleteAll()





}