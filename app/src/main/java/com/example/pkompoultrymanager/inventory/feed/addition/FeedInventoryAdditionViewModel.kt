package com.example.pkompoultrymanager.inventory.feed.addition

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.inventory.feed.reduction.FeedInventoryReduction
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class FeedInventoryAdditionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FeedInventoryAdditionRepository


    val displayAllFeedInventoryAdditions: LiveData<List<FeedInventoryAddition>>
    val allFeedQuantityForFeedName: LiveData<List<NameQuantityDouble>>
    val allFeedCostForFeedName: LiveData<List<NameQuantityDouble>>
    val allFeedQuantityForDate: LiveData<List<DateQuantityDouble>>
    val allFeedCost: LiveData<Double>
    val quantityOfAllFeedAdded: LiveData<Double>

    private val _feedCost: MutableLiveData<Double?> = MutableLiveData(0.0)
    val feedCost: LiveData<Double?> = _feedCost

    private val _sumOfFeedInventory: MutableLiveData<Double?> = MutableLiveData(0.0)
    val sumOfFeedInventory: LiveData<Double?> = _sumOfFeedInventory

    private val _feedQuantityForDate: MutableLiveData<List<DateQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForDate: LiveData<List<DateQuantityDouble>> = _feedQuantityForDate

    private val _feedQuantityForFeedName: MutableLiveData<List<NameQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedQuantityForFeedName: LiveData<List<NameQuantityDouble>> = _feedQuantityForFeedName

    private val _feedCostForFeedName: MutableLiveData<List<NameQuantityDouble>> = MutableLiveData(mutableListOf())
    val feedCostForFeedName: LiveData<List<NameQuantityDouble>> = _feedCostForFeedName

    private val _feedInventoryAddition: MutableLiveData<List<FeedInventoryAddition>> = MutableLiveData(mutableListOf())
    val feedInventoryAddition: LiveData<List<FeedInventoryAddition>> = _feedInventoryAddition

    init {
        val feedInventoryAdditionDao = AppDatabase.getDatabase(application).feedInventoryAdditionDao()
        repository = FeedInventoryAdditionRepository(feedInventoryAdditionDao)
        displayAllFeedInventoryAdditions = repository.displayAllFeedInventoryAddition
        allFeedQuantityForFeedName = repository.allFeedQuantityForFeedName
        allFeedCostForFeedName = repository.allFeedCostForFeedName
        allFeedQuantityForDate = repository.allFeedQuantityForDate
        allFeedCost = repository.allFeedCost
        quantityOfAllFeedAdded = repository.quantityOfAllFeedAdded
    }

    fun displayFeedInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FeedInventoryAddition>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayFeedInventoryAdditionByDate(number, firstDate, lastDate)
        }
        return repository.displayFeedInventoryAdditionByDate(number, firstDate, lastDate)
    }

    fun quantityOfFeedAddedPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.quantityOfFeedAddedPerDuration(firstDate, lastDate)
        }
        return repository.quantityOfFeedAddedPerDuration(firstDate, lastDate)
    }

    fun feedCostPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.feedCostPerDuration(firstDate, lastDate)
        }
        return repository.feedCostPerDuration(firstDate, lastDate)
    }

    fun getTotalFeedQuantityByFeedName(feedName: String) {
        viewModelScope.launch {
            val totalFeedAdded:Double? = repository.sumOfFeedQuantityByFeedName(feedName)
            _sumOfFeedInventory.postValue(totalFeedAdded)
        }
    }

    fun getFeedQuantityForDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedAdded:List<DateQuantityDouble> = repository.feedQuantityForDate(firstDate, lastDate)
            _feedQuantityForDate.postValue(feedAdded)
        }
    }
    fun getFeedQuantityForFeedName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedAdded:List<NameQuantityDouble> = repository.feedQuantityForFeedName(firstDate, lastDate)
            _feedQuantityForFeedName.postValue(feedAdded)
        }
    }
    fun getFeedCostForFeedName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedAdded:List<NameQuantityDouble> = repository.feedCostForFeedName(firstDate, lastDate)
            _feedCostForFeedName.postValue(feedAdded)
        }
    }/*
    fun getSumOfQuantityOfFeedAdded(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val feedAdded:Double = repository.sumOfQuantityOfFeedAdded(firstDate, lastDate)
            _sumOfQuantityOfFeedAdded.postValue(feedAdded)
        }
    }*/

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun delete(feedInventoryAddition: FeedInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(feedInventoryAddition)
        }
    }

    fun addFeedInventoryAddition(feedInventoryAddition: FeedInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFeedInventory(feedInventoryAddition)
        }
    }

    fun updateFeedInventoryAddition(
        feedInventoryAdditionID: Int,
        feedName: String,
        dateAcquired: Date,
        feedQuantity: Double,
        feedSource: String,
        feedCost: Double,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFeedInventoryAddition(
                feedInventoryAdditionID,
                feedName,
                dateAcquired,
                feedQuantity,
                feedSource,
                feedCost,
                shortNotes
            )
        }
    }
/*
    fun deleteFeedInventoryAddition(feedInventoryAdditionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(feedInventoryAdditionId)

        }
}*/

}
