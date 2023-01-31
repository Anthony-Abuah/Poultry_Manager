package com.example.pkompoultrymanager.inventory.feed.reduction

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import java.sql.Date
import java.time.LocalDate
import java.util.*

class FeedInventoryReductionRepository(private val feedInventoryReductionDao: FeedInventoryReductionDao) {


    val displayAllFeedInventoryReduction: LiveData<List<FeedInventoryReduction>> =
        feedInventoryReductionDao.displayAllFeedInventoryReduction()

    val allFeedingQuantityUsedForFeeding: LiveData<Double> =
        feedInventoryReductionDao.allFeedingQuantityUsedForFeeding()

    fun displayFeedInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryReduction>> =
        feedInventoryReductionDao.displayFeedInventoryReductionByDate(number, firstDate, lastDate)


    suspend fun sumOfFeedReducedByFeedName(feedName: String): Double?=
        feedInventoryReductionDao.sumOfFeedReducedByFeedName(feedName)

    suspend fun feedQuantityForDate(firstDate: Date, lastDate: Date): List<DateQuantityDouble> =
        feedInventoryReductionDao.feedQuantityForDate(firstDate, lastDate)

    fun feedQuantityUsedForFeedingPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        feedInventoryReductionDao.feedQuantityUsedForFeedingPerDuration(firstDate, lastDate)

    val allFeedQuantityForDate: LiveData<List<DateQuantityDouble>> =
        feedInventoryReductionDao.allFeedQuantityForDate()

    suspend fun feedQuantityForFeedName(firstDate: Date, lastDate: Date): List<NameQuantityDouble> =
        feedInventoryReductionDao.feedQuantityForFeedName(firstDate, lastDate)

    val allFeedQuantityForFeedName: LiveData<List<NameQuantityDouble>> =
        feedInventoryReductionDao.allFeedQuantityForFeedName()

    suspend fun feedQuantityForFlockName(firstDate: Date, lastDate: Date): List<NameQuantityDouble> =
        feedInventoryReductionDao.feedQuantityForFlockName(firstDate, lastDate)

    val allFeedQuantityForFlockName: LiveData<List<NameQuantityDouble>> =
        feedInventoryReductionDao.allFeedQuantityForFlockName()

    suspend fun feedQuantityForReductionReason(firstDate: Date, lastDate: Date): List<NameQuantityDouble> =
        feedInventoryReductionDao.feedQuantityForReductionReason(firstDate, lastDate)

    val allFeedQuantityForReductionReason: LiveData<List<NameQuantityDouble>> =
        feedInventoryReductionDao.allFeedQuantityForReductionReason()


    suspend fun addFeedInventory(feedInventoryReduction: FeedInventoryReduction){
        feedInventoryReductionDao.addFeedInventoryReduction(feedInventoryReduction)
    }

    suspend fun updateFeedInventoryReduction(feedInventoryReductionID: Int, feedName: String, reductionDate: Date, reductionQuantity: Double, flockName:String, reductionReason: String, shortNotes:String){
        feedInventoryReductionDao.updateFeedInventoryReduction(feedInventoryReductionID, feedName, reductionDate, reductionQuantity, flockName, reductionReason, shortNotes)
    }

     fun deleteAll(){
        feedInventoryReductionDao.deleteAll()
    }
    suspend fun delete(feedInventoryReduction: FeedInventoryReduction){
        feedInventoryReductionDao.delete(feedInventoryReduction)
    }

}