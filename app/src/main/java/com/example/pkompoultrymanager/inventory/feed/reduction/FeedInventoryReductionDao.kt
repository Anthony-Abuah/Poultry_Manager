package com.example.pkompoultrymanager.inventory.feed.reduction

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import java.security.cert.CertPathValidatorException
import java.sql.Date


@Dao
interface FeedInventoryReductionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedInventoryReduction(feedInventoryReduction: FeedInventoryReduction)

    @Query("SELECT * FROM FeedInventoryReduction ORDER BY ReductionDate DESC ")
    fun displayAllFeedInventoryReduction(): LiveData<List<FeedInventoryReduction>>

    @Query("SELECT SUM(ReductionQuantity) FROM FeedInventoryReduction WHERE FeedName =:feedName")
    suspend fun sumOfFeedReducedByFeedName(feedName: String): Double

    @Query("SELECT * FROM FeedInventoryReduction WHERE ReductionDate BETWEEN :firstDate AND :lastDate ORDER BY ReductionDate DESC LIMIT :number")
    fun displayFeedInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryReduction>>

    @Query("UPDATE FeedInventoryReduction SET FeedName=:feedName, ReductionDate=:reductionDate, ReductionQuantity=:reductionQuantity, FlockName=:flockName, ReductionReason=:reductionReason, ShortNotes=:shortNotes WHERE FeedInventoryReductionId LIKE :feedInventoryReductionID")
    suspend fun updateFeedInventoryReduction( feedInventoryReductionID: Int, feedName: String, reductionDate: Date, reductionQuantity: Double, flockName:String, reductionReason: String, shortNotes:String)

    @Query("SELECT ReductionDate AS date, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction WHERE ReductionDate BETWEEN :firstDate AND :lastDate GROUP BY ReductionDate")
    suspend fun feedQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityDouble>

    @Query("SELECT ReductionDate AS date, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction GROUP BY ReductionDate")
    fun allFeedQuantityForDate(): LiveData<List<DateQuantityDouble>>

    @Query("SELECT FeedName AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction WHERE ReductionDate BETWEEN :firstDate AND :lastDate GROUP BY FeedName")
    suspend fun feedQuantityForFeedName(firstDate: Date, lastDate: Date): List<NameQuantityDouble>

    @Query("SELECT FeedName AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction GROUP BY FeedName")
    fun allFeedQuantityForFeedName(): LiveData<List<NameQuantityDouble>>

    @Query("SELECT FlockName AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction WHERE ReductionDate BETWEEN :firstDate AND :lastDate GROUP BY FlockName")
    suspend fun feedQuantityForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityDouble>

    @Query("SELECT FlockName AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction GROUP BY FlockName")
    fun allFeedQuantityForFlockName(): LiveData<List<NameQuantityDouble>>

    @Query("SELECT ReductionReason AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction WHERE ReductionDate BETWEEN :firstDate AND :lastDate GROUP BY ReductionReason")
    suspend fun feedQuantityForReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityDouble>

    @Query("SELECT SUM(ReductionQuantity) FROM FeedInventoryReduction WHERE ReductionReason LIKE '%eed%' AND ReductionDate BETWEEN :firstDate AND :lastDate")
    fun feedQuantityUsedForFeedingPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(ReductionQuantity) FROM FeedInventoryReduction WHERE ReductionReason LIKE '%eed%'")
    fun allFeedingQuantityUsedForFeeding(): LiveData<Double>

    @Query("SELECT ReductionReason AS name, SUM(ReductionQuantity) AS quantity FROM FeedInventoryReduction GROUP BY ReductionReason")
    fun allFeedQuantityForReductionReason(): LiveData<List<NameQuantityDouble>>

    @Delete
    suspend fun delete(feedInventoryReduction: FeedInventoryReduction)

    @Query("DELETE FROM FeedInventoryReduction")
    fun deleteAll()

}