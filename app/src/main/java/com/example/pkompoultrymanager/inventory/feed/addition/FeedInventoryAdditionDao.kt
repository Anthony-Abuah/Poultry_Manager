package com.example.pkompoultrymanager.inventory.feed.addition

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import java.sql.Date


@Dao
interface FeedInventoryAdditionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedInventory(feedInventoryAddition: FeedInventoryAddition)

    @Query("SELECT * FROM FeedInventoryAddition ORDER BY DateAcquired DESC ")
    fun displayAllFeedInventoryAddition(): LiveData<List<FeedInventoryAddition>>

    @Query("SELECT * FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate ORDER BY DateAcquired ASC ")
    suspend fun feedInventoryAddition(firstDate: Date, lastDate: Date): List<FeedInventoryAddition>

    @Query("SELECT SUM(FeedQuantity) FROM FeedInventoryAddition WHERE FeedName=:feedName")
    suspend fun sumOfFeedQuantityByFeedName(feedName: String): Double

    @Query("SELECT DateAcquired AS date, SUM(FeedQuantity) AS quantity FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate GROUP BY DateAcquired")
    suspend fun feedQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityDouble>

    @Query("SELECT DateAcquired AS date, SUM(FeedQuantity) AS quantity FROM FeedInventoryAddition GROUP BY DateAcquired")
    fun allFeedQuantityForDate(): LiveData<List<DateQuantityDouble>>

    @Query("SELECT FeedName AS name, SUM(FeedQuantity) AS quantity FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate GROUP BY FeedName")
    suspend fun feedQuantityForFeedName(firstDate: Date, lastDate: Date): List<NameQuantityDouble>

    @Query("SELECT FeedName AS name, SUM(FeedQuantity) AS quantity FROM FeedInventoryAddition GROUP BY FeedName")
    fun allFeedQuantityForFeedName(): LiveData<List<NameQuantityDouble>>

    @Query("SELECT FeedName AS name, SUM(FeedCost) AS quantity FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate GROUP BY FeedName")
    suspend fun feedCostForFeedName(firstDate: Date, lastDate: Date): List<NameQuantityDouble>

    @Query("SELECT FeedName AS name, SUM(FeedCost) AS quantity FROM FeedInventoryAddition GROUP BY FeedName")
    fun allFeedCostForFeedName(): LiveData<List<NameQuantityDouble>>

    @Query("SELECT SUM(FeedQuantity) FROM FeedInventoryAddition WHERE FeedName=:feedName")
    suspend fun feedCostForFeedName(feedName: String): Double

    @Query("SELECT * FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate ORDER BY DateAcquired DESC LIMIT :number")
    fun displayFeedInventoryAdditionByDate(number:Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>>

    @Query("SELECT * FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate ORDER BY feedInventoryAdditionId DESC LIMIT :number")
    fun displayFeedInventoryAddition1(number:Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>>

    @Query("UPDATE FeedInventoryAddition SET FeedName=:feedName, FeedCost=:feedCost, FeedQuantity=:feedQuantity, FeedSource=:feedSource, DateAcquired=:dateAcquired, ShortNotes=:shortNotes WHERE FeedInventoryAdditionId LIKE :feedInventoryAdditionID")
    suspend fun updateFeedInventoryAddition( feedInventoryAdditionID: Int, feedName: String, dateAcquired: Date, feedQuantity: Double, feedSource:String, feedCost: Double, shortNotes:String)

    @Delete
    suspend fun delete(feedInventoryAddition: FeedInventoryAddition)

    @Query("DELETE FROM FeedInventoryAddition")
    fun deleteAll()


    @Query("SELECT * FROM FeedInventoryAddition WHERE FeedSource LIKE :feedSource AND DateAcquired BETWEEN :firstDate AND :lastDate")
    fun displayFeedInventoryAdditionForFeedSource(feedSource: String, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>>

    @Query("SELECT * FROM FeedInventoryAddition WHERE FeedName LIKE :feedName AND DateAcquired BETWEEN :firstDate AND :lastDate")
    fun displayFeedInventoryAdditionForFeedName(feedName: String, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>>

    @Query("SELECT SUM(FeedQuantity) FROM FeedInventoryAddition WHERE FeedName LIKE :feedName AND DateAcquired BETWEEN :firstDate AND :lastDate")
    fun sumOfQuantityOfFeedTypeUsed(feedName: String, firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(FeedQuantity) FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate")
    fun quantityOfFeedAddedPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(FeedQuantity) FROM FeedInventoryAddition")
    fun quantityOfAllFeedAdded(): LiveData<Double>

    @Query("SELECT SUM(FeedCost) FROM FeedInventoryAddition WHERE DateAcquired BETWEEN :firstDate AND :lastDate")
    fun feedCostPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(FeedCost) FROM FeedInventoryAddition")
    fun allFeedCost(): LiveData<Double>

}