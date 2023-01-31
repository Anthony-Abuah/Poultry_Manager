package com.example.pkompoultrymanager.inventory.flock.reduction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.inventory.flock.addition.FlockInventoryAdditionRepository
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.flock_inventory.NameQuantityCost
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class FlockInventoryReductionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FlockInventoryReductionRepository

    val displayAllFlockInventoryReductions: LiveData<List<FlockInventoryReduction>>
    val allFlockQuantityForDate: LiveData<List<DateQuantityInteger>>
    val allFlockQuantityCostForFlockName: LiveData<List<NameQuantityCost>>
    val allFlockQuantityCostForCustomer: LiveData<List<NameQuantityCost>>
    val allFlockQuantityForReductionReason: LiveData<List<NameQuantityInteger>>
    val allFlockSalesFlockDate: LiveData<List<DateQuantityAmount>>
    val allFlockSales: LiveData<Double>


   private val _reductionSum: MutableLiveData<Int?> = MutableLiveData(0)
    val reductionSum: LiveData<Int?> = _reductionSum

   private val _flockQuantityForDate: MutableLiveData<List<DateQuantityInteger>> = MutableLiveData(mutableListOf())
    val flockQuantityForDate: LiveData<List<DateQuantityInteger>> = _flockQuantityForDate

   private val _flockQuantityForReductionReason: MutableLiveData<List<NameQuantityInteger>> = MutableLiveData(mutableListOf())
    val flockQuantityForReductionReason: LiveData<List<NameQuantityInteger>> = _flockQuantityForReductionReason

   private val _flockQuantityCostForCustomer: MutableLiveData<List<NameQuantityCost>> = MutableLiveData(mutableListOf())
    val flockQuantityCostForCustomer: LiveData<List<NameQuantityCost>> = _flockQuantityCostForCustomer

   private val _flockQuantityCostForFlockName: MutableLiveData<List<NameQuantityCost>> = MutableLiveData(mutableListOf())
    val flockQuantityCostForFlockName: LiveData<List<NameQuantityCost>> = _flockQuantityCostForFlockName

   private val _flockSalesFlockDate: MutableLiveData<List<DateQuantityAmount>> = MutableLiveData(mutableListOf())
    val flockSalesFlockDate: LiveData<List<DateQuantityAmount>> = _flockSalesFlockDate

    init {
        val flockInventoryReductionDao =
            AppDatabase.getDatabase(application).flockInventoryReductionDao()
        repository = FlockInventoryReductionRepository(flockInventoryReductionDao)
        displayAllFlockInventoryReductions = repository.displayAllFlockInventoryReduction
        allFlockQuantityCostForFlockName = repository.allFlockQuantityCostForFlockName
        allFlockQuantityCostForCustomer = repository.allFlockQuantityCostForCustomer
        allFlockQuantityForReductionReason = repository.allFlockQuantityForReductionReason
        allFlockQuantityForDate = repository.allFlockQuantityForDate
        allFlockSalesFlockDate = repository.allFlockSalesFlockDate
        allFlockSales = repository.allFlockSales
    }

    fun displayFlockInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<FlockInventoryReduction>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayFlockInventoryReductionByDate(number, firstDate, lastDate)
        }
        return repository.displayFlockInventoryReductionByDate(number, firstDate, lastDate)
    }
    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun delete(flockInventoryReduction: FlockInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(flockInventoryReduction)
        }
    }

    fun addFlockInventoryReduction(flockInventoryReduction: FlockInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFlockInventory(flockInventoryReduction)
        }
    }
    fun numberOfDeadFlockPerDuration(firstDate: Date, lastDate: Date) : LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.numberOfDeadFlockPerDuration(firstDate, lastDate)
        }
        return repository.numberOfDeadFlockPerDuration(firstDate, lastDate)
    }
    fun numberOfSoldFlockPerDuration(firstDate: Date, lastDate: Date) : LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.numberOfSoldFlockPerDuration(firstDate, lastDate)
        }
        return repository.numberOfSoldFlockPerDuration(firstDate, lastDate)
    }
    fun flockSalesPerDuration(firstDate: Date, lastDate: Date) : LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.flockSalesPerDuration(firstDate, lastDate)
        }
        return repository.flockSalesPerDuration(firstDate, lastDate)
    }

    fun getSum(selectedFlockName: String) {
        viewModelScope.launch {
            val totalSum:Int? = repository.getSum(selectedFlockName)
            _reductionSum.postValue(totalSum)
        }
    }
    fun getFlockQuantityForReductionReason(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantity:List<NameQuantityInteger> = repository.flockQuantityForReductionReason(firstDate, lastDate)
            _flockQuantityForReductionReason.postValue(flockQuantity)
        }
    }
    fun getFlockQuantityForDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantity:List<DateQuantityInteger> = repository.flockQuantityForDate(firstDate, lastDate)
            _flockQuantityForDate.postValue(flockQuantity)
        }
    }
    fun getFlockQuantityCostForCustomer(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantityCost:List<NameQuantityCost> = repository.flockQuantityCostForCustomer(firstDate, lastDate)
            _flockQuantityCostForCustomer.postValue(flockQuantityCost)
        }
    }
    fun getFlockQuantityCostForFlockName(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val flockQuantityCost:List<NameQuantityCost> = repository.flockQuantityCostForFlockName(firstDate, lastDate)
            _flockQuantityCostForFlockName.postValue(flockQuantityCost)
        }
    }

    fun updateFlockInventoryReduction(
        flockInventoryReductionID: Int, flockName: String, dateReduced: Date, reductionQuantity: Int, customer:String, flockCost:Double, amountReceived:Double, amountOwed:Double, reductionReason:String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFlockInventoryReduction(
                flockInventoryReductionID,
                flockName,
                dateReduced,
                reductionQuantity,
                customer,
                flockCost,
                amountReceived,
                amountOwed,
                reductionReason,
                shortNotes
            )
        }
    }
/*
    fun deleteFlockInventoryReduction(flockInventoryReductionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(flockInventoryReductionId)

        }
}*/

}
