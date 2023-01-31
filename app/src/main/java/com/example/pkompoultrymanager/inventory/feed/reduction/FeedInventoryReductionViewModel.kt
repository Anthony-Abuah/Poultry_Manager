package com.example.pkompoultrymanager.inventory.feed.reduction

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.inventory.feed.addition.FeedInventoryAddition
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReduction
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class FeedInventoryReductionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FeedInventoryReductionRepository


    val displayAllFeedInventoryReductions: LiveData<List<FeedInventoryReduction>>
    val allFeedQuantityForDate: LiveData<List<DateQuantityDouble>>
    val allFeedQuantityForFeedName: LiveData<List<NameQuantityDouble>>
    val allFeedQuantityForFlockName: LiveData<List<NameQuantityDouble>>
    val allFeedQuantityForReductionReason: LiveData<List<NameQuantityDouble>>
    val allFeedingQuantityUsedForFeeding: LiveData<Double>

    private val _sumOfFeedReduction: MutableLiveData<Double?> = MutableLiveData(0.0)
    val sumOfFeedReduction: LiveData<Double?> = _sumOfFeedReduction

    private val _feedQuantityForDate: MutableLiveData<List<DateQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForDate: LiveData<List<DateQuantityDouble>> = _feedQuantityForDate

    private val _feedQuantityForFeedName: MutableLiveData<List<NameQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForFeedName: LiveData<List<NameQuantityDouble>> = _feedQuantityForFeedName

    private val _feedQuantityForFlockName: MutableLiveData<List<NameQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForFlockName: LiveData<List<NameQuantityDouble>> = _feedQuantityForFlockName

    private val _feedQuantityForReductionReason: MutableLiveData<List<NameQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForReductionReason: LiveData<List<NameQuantityDouble>> = _feedQuantityForReductionReason

    init {
        val feedInventoryReductionDao =
            AppDatabase.getDatabase(application).feedInventoryReductionDao()
        repository = FeedInventoryReductionRepository(feedInventoryReductionDao)
        displayAllFeedInventoryReductions = repository.displayAllFeedInventoryReduction
        allFeedQuantityForDate = repository.allFeedQuantityForDate
        allFeedQuantityForFeedName = repository.allFeedQuantityForFeedName
        allFeedQuantityForFlockName = repository.allFeedQuantityForFlockName
        allFeedQuantityForReductionReason = repository.allFeedQuantityForReductionReason
        allFeedingQuantityUsedForFeeding = repository.allFeedingQuantityUsedForFeeding
    }

    fun displayFeedInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryReduction>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayFeedInventoryReductionByDate(number, firstDate, lastDate)
        }
        return repository.displayFeedInventoryReductionByDate(number, firstDate, lastDate)
    }
    fun feedQuantityUsedForFeedingPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.feedQuantityUsedForFeedingPerDuration(firstDate, lastDate)
        }
        return repository.feedQuantityUsedForFeedingPerDuration(firstDate, lastDate)
    }
    fun getTotalFeedReductionQuantityByFeedName(feedName: String) {
        viewModelScope.launch {
            val totalFeedReduced:Double? = repository.sumOfFeedReducedByFeedName(feedName)
            _sumOfFeedReduction.postValue(totalFeedReduced)
        }
    }/*
    fun getSumOfFeedQuantityUsedForFeeding(firstDate: Date,lastDate: Date) {
        viewModelScope.launch {
            val totalFeedReduced:Double = repository.sumOfFeedQuantityUsedForFeeding(firstDate, lastDate)
            _sumOfFeedQuantityUsedForFeeding.postValue(totalFeedReduced)
        }
    }*/
    fun getFeedQuantityForDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedReduced:List<DateQuantityDouble> = repository.feedQuantityForDate(firstDate, lastDate)
            _feedQuantityForDate.postValue(feedReduced)
        }
    }
    fun getFeedQuantityForFeedName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedReduced:List<NameQuantityDouble> = repository.feedQuantityForFeedName(firstDate, lastDate)
            _feedQuantityForFeedName.postValue(feedReduced)
        }
    }
    fun getFeedQuantityForFlockName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedReduced:List<NameQuantityDouble> = repository.feedQuantityForFlockName(firstDate, lastDate)
            _feedQuantityForFlockName.postValue(feedReduced)
        }
    }
    fun getFeedQuantityForReductionReason(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedReduced:List<NameQuantityDouble> = repository.feedQuantityForReductionReason(firstDate, lastDate)
            _feedQuantityForReductionReason.postValue(feedReduced)
        }
    }

    fun delete(feedInventoryReduction: FeedInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(feedInventoryReduction)
        }
    }
    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addFeedInventoryReduction(feedInventoryReduction: FeedInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFeedInventory(feedInventoryReduction)
        }
    }

    fun updateFeedInventoryReduction(
        feedInventoryReductionID: Int, feedName: String, reductionDate: Date, reductionQuantity: Double, flockName:String, reductionReason: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFeedInventoryReduction(
                feedInventoryReductionID,
                feedName,
                reductionDate,
                reductionQuantity,
                flockName,
                reductionReason,
                shortNotes
            )
        }
    }
/*
    fun deleteFeedInventoryReduction(feedInventoryReductionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(feedInventoryReductionId)

        }
}*/

}
