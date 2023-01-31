package com.example.pkompoultrymanager.inventory.feed.addition

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.inventory.eggs.addition.EggInventoryAddition
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReduction
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import java.sql.Date
import java.time.LocalDate
import java.util.*

class FeedInventoryAdditionRepository(private val feedInventoryAdditionDao: FeedInventoryAdditionDao) {

    val displayAllFeedInventoryAddition: LiveData<List<FeedInventoryAddition>> =
        feedInventoryAdditionDao.displayAllFeedInventoryAddition()

    val allFeedCost: LiveData<Double> =
        feedInventoryAdditionDao.allFeedCost()

    val quantityOfAllFeedAdded: LiveData<Double> =
        feedInventoryAdditionDao.quantityOfAllFeedAdded()

    suspend fun feedInventoryAddition(firstDate: Date,lastDate: Date): List<FeedInventoryAddition> =
        feedInventoryAdditionDao.feedInventoryAddition(firstDate, lastDate)

    fun quantityOfFeedAddedPerDuration(firstDate: Date,lastDate: Date): LiveData<Double> =
        feedInventoryAdditionDao.quantityOfFeedAddedPerDuration(firstDate, lastDate)

    fun feedCostPerDuration(firstDate: Date,lastDate: Date): LiveData<Double> =
        feedInventoryAdditionDao.feedCostPerDuration(firstDate, lastDate)

    fun displayFeedInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>> =
        feedInventoryAdditionDao.displayFeedInventoryAdditionByDate(number, firstDate, lastDate)

    suspend fun addFeedInventory(feedInventoryAddition: FeedInventoryAddition){
        feedInventoryAdditionDao.addFeedInventory(feedInventoryAddition)
    }

    suspend fun sumOfFeedQuantityByFeedName(feedName: String) : Double? =
        feedInventoryAdditionDao.sumOfFeedQuantityByFeedName(feedName)

    suspend fun feedQuantityForDate(firstDate: Date, lastDate: Date) : List<DateQuantityDouble> =
        feedInventoryAdditionDao.feedQuantityForDate(firstDate, lastDate)

    val allFeedQuantityForDate : LiveData<List<DateQuantityDouble>> =
        feedInventoryAdditionDao.allFeedQuantityForDate()

    suspend fun feedQuantityForFeedName(firstDate: Date, lastDate: Date) : List<NameQuantityDouble> =
        feedInventoryAdditionDao.feedQuantityForFeedName(firstDate, lastDate)

    val allFeedQuantityForFeedName : LiveData<List<NameQuantityDouble>> =
        feedInventoryAdditionDao.allFeedQuantityForFeedName()

    suspend fun feedCostForFeedName(firstDate: Date, lastDate: Date) : List<NameQuantityDouble> =
        feedInventoryAdditionDao.feedCostForFeedName(firstDate, lastDate)

    val allFeedCostForFeedName : LiveData<List<NameQuantityDouble>> =
        feedInventoryAdditionDao.allFeedCostForFeedName()


    suspend fun updateFeedInventoryAddition(feedInventoryAdditionID: Int, feedName: String, dateAcquired: Date, feedQuantity: Double, feedSource:String, feedCost: Double, shortNotes:String){
        feedInventoryAdditionDao.updateFeedInventoryAddition(feedInventoryAdditionID, feedName, dateAcquired, feedQuantity, feedSource, feedCost, shortNotes)
    }

     fun deleteAll(){
        feedInventoryAdditionDao.deleteAll()
    }
    suspend fun delete(feedInventoryAddition: FeedInventoryAddition){
        feedInventoryAdditionDao.delete(feedInventoryAddition)
    }

}