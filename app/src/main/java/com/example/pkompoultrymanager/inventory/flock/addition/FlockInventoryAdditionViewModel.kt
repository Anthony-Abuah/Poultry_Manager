package com.example.pkompoultrymanager.inventory.flock.addition

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.inventory.flock.reduction.FlockInventoryReduction
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class FlockInventoryAdditionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FlockInventoryAdditionRepository


    val displayAllFlockInventoryAdditions: LiveData<List<FlockInventoryAddition>>
    val allFlockNames: LiveData<List<String>>
    val allFlockQuantityForDate: LiveData<List<DateQuantityInteger>>
    val allFlockQuantityCostForFlockPurpose: LiveData<List<NameQuantityCost>>
    val allFlockQuantityForFlockName: LiveData<List<NameQuantityInteger>>
    val allFlockQuantityCostForFlockName: LiveData<List<NameQuantityCost>>

    private val _flockAddedByFlockName: MutableLiveData<Int?> = MutableLiveData(0)
    val flockAddedByFlockName: LiveData<Int?> = _flockAddedByFlockName

    private val _flockPurposeByFlockName: MutableLiveData<String?> = MutableLiveData("")
    val flockPurposeByFlockName: LiveData<String?> = _flockPurposeByFlockName

    private val _flockQuantityCostForFlockPurpose: MutableLiveData<List<NameQuantityCost>> = MutableLiveData(mutableListOf())
    val flockQuantityCostForFlockPurpose: LiveData<List<NameQuantityCost>> = _flockQuantityCostForFlockPurpose

    private val _flockQuantityForFlockName: MutableLiveData<List<NameQuantityInteger>> = MutableLiveData(mutableListOf())
    val flockQuantityForFlockName: LiveData<List<NameQuantityInteger>> = _flockQuantityForFlockName

    private val _flockQuantityCostForFlockName: MutableLiveData<List<NameQuantityCost>> = MutableLiveData(mutableListOf())
    val flockQuantityCostForFlockName: LiveData<List<NameQuantityCost>> = _flockQuantityCostForFlockName

    private val _flockQuantityForDate: MutableLiveData<List<DateQuantityInteger>> = MutableLiveData(mutableListOf())
    val flockQuantityForDate: LiveData<List<DateQuantityInteger>> = _flockQuantityForDate


    init {
        val flockInventoryAdditionDao =
            AppDatabase.getDatabase(application).flockInventoryAdditionDao()
        repository = FlockInventoryAdditionRepository(flockInventoryAdditionDao)
        displayAllFlockInventoryAdditions = repository.displayFlockInventoryAddition
        allFlockNames = repository.allFlockNames
        allFlockQuantityForDate = repository.allFlockQuantityForDate
        allFlockQuantityCostForFlockPurpose = repository.allFlockQuantityCostForFlockPurpose
        allFlockQuantityForFlockName = repository.allFlockQuantityForFlockName
        allFlockQuantityCostForFlockName = repository.allFlockQuantityCostForFlockName
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun displayFlockInventoryAdditionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryAddition>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayFlockInventoryAdditionByDate(number, firstDate, lastDate)
        }
        return repository.displayFlockInventoryAdditionByDate(number, firstDate, lastDate)
    }


    fun addFlockInventoryAddition(flockInventoryAddition: FlockInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFlockInventory(flockInventoryAddition)
        }
    }
    fun getFlockAddedByFlockName(selectedFlockName: String) {
        viewModelScope.launch {
            val flockAdded:Int? = repository.flockAddedByFlockName(selectedFlockName)
            _flockAddedByFlockName.postValue(flockAdded)
        }
    }
    fun getFlockPurposeByFlockName(selectedFlockName: String) {
        viewModelScope.launch {
            val flockPurpose:String? = repository.flockPurposeByFlockName(selectedFlockName)
            _flockPurposeByFlockName.postValue(flockPurpose)
        }
    }
    fun getFlockQuantityCostForFlockPurpose(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantity:List<NameQuantityCost> = repository.flockQuantityCostForFlockPurpose(firstDate, lastDate)
            _flockQuantityCostForFlockPurpose.postValue(flockQuantity)
        }
    }
    fun getFlockQuantityForFlockName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantity:List<NameQuantityInteger> = repository.flockQuantityForFlockName(firstDate, lastDate)
            _flockQuantityForFlockName.postValue(flockQuantity)
        }
    }
    fun getFlockQuantityCostForFlockName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantityCost:List<NameQuantityCost> = repository.flockQuantityCostForFlockName(firstDate, lastDate)
            _flockQuantityCostForFlockName.postValue(flockQuantityCost)
        }
    }
    fun getFlockQuantityForDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantity:List<DateQuantityInteger> = repository.flockQuantityForDate(firstDate, lastDate)
            _flockQuantityForDate.postValue(flockQuantity)
        }
    }

    fun updateFlockInventoryAddition(
        flockInventoryAdditionID: Int,
        flockName: String,
        dateAdded: Date,
        flockQuantity: Double,
        flockSource: String,
        flockCost: Double,
        flockAge: Int,
        flockPurpose: String,
        flockBreed: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFlockInventoryAddition(
                flockInventoryAdditionID,
                flockName,
                dateAdded,
                flockQuantity,
                flockSource,
                flockCost,
                flockAge,
                flockPurpose,
                flockBreed,
                shortNotes
            )
        }
    }

    fun delete(flockInventoryAdditionId: FlockInventoryAddition) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(flockInventoryAdditionId)
        }
}

}
