package com.example.pkompoultrymanager.inventory.eggs.addition

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class EggInventoryAdditionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EggInventoryAdditionRepository


    val displayAllEggInventoryAdditions: LiveData<List<EggInventoryAddition>>
    val numberOfAllEggsAdded: LiveData<Int>
    val allTheEggAdditionsByDate: LiveData<List<DateQuantityInteger>>

    private val _sumOfEggsAddedByDate: MutableLiveData<Int?> = MutableLiveData(0)
    val sumOfEggsAddedByDate: LiveData<Int?> = _sumOfEggsAddedByDate

    private val _eggsAddedByEggType: MutableLiveData<Int?> = MutableLiveData(0)
    val eggsAddedByEggType: LiveData<Int?> = _eggsAddedByEggType

    private val _eggAdditionsByDate: MutableLiveData<List<DateQuantityInteger>> = MutableLiveData(mutableListOf())
    val eggAdditionsByDate: LiveData<List<DateQuantityInteger>> = _eggAdditionsByDate

    init {
        val eggInventoryAdditionDao = AppDatabase.getDatabase(application).eggInventoryAdditionDao()
        repository = EggInventoryAdditionRepository(eggInventoryAdditionDao)
        displayAllEggInventoryAdditions = repository.displayAllEggInventoryAddition
        allTheEggAdditionsByDate = repository.eggAdditionsByDate
        numberOfAllEggsAdded = repository.numberOfAllEggsAdded
    }

    fun displayAllEggInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayAllEggInventoryAdditionByDate(number, firstDate, lastDate)
        }
        return repository.displayAllEggInventoryAdditionByDate(number, firstDate, lastDate)
    }

    fun numberOfEggsAddedPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.numberOfEggsAddedPerDuration(firstDate, lastDate)
        }
        return repository.numberOfEggsAddedPerDuration(firstDate, lastDate)
    }

    fun getEggsAddedByEggType(eggType: String) {
        viewModelScope.launch {
            val eggsAdded:Int? = repository.sumOfAllEggAdditionByEggType(eggType)
            _eggsAddedByEggType.postValue(eggsAdded)
        }
    }/*
    fun getSumOfEggsAddedByDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val eggsCollected: Int = repository.sumOfEggsAddedByDate(firstDate, lastDate)
            _sumOfEggsAddedByDate.postValue(eggsCollected)
        }
    }*/
    fun getEggAdditionsByDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val eggDateQuantityInteger:List<DateQuantityInteger> = repository.eggAdditionsByDate(firstDate, lastDate)
            _eggAdditionsByDate.postValue(eggDateQuantityInteger)
        }
    }
    fun displayAllEggInventoryAdditionByFlock(flock: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayEggInventoryAdditionByFlock(flock, firstDate, lastDate)
        }
        return repository.displayEggInventoryAdditionByFlock(flock, firstDate, lastDate)
    }
    fun displayAllEggInventoryAdditionByEggType(eggType: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryAddition>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayEggInventoryAdditionByEggType(eggType, firstDate, lastDate)
        }
        return repository.displayEggInventoryAdditionByEggType(eggType, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun delete(eggInventoryAddition: EggInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(eggInventoryAddition)
        }
    }

    fun addEggInventoryAddition(eggInventoryAddition: EggInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEggInventory(eggInventoryAddition)
        }
    }

    fun updateEggInventoryAdditionInfo(eggInventoryAdditionId: Int, dateCollected: Date, flock: String, eggType: String, eggQuantity: Int, shortNotes:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEggInventoryAdditionInfo(eggInventoryAdditionId, dateCollected, flock, eggType, eggQuantity, shortNotes)
        }
    }

    fun deleteEggInventoryAddition(eggInventoryAdditionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(eggInventoryAdditionId)

        }
    }

}
