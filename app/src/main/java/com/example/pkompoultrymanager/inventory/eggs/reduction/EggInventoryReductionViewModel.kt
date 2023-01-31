package com.example.pkompoultrymanager.inventory.eggs.reduction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.egg_inventory.NameQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.DateQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate


class EggInventoryReductionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EggInventoryReductionRepository


    val displayAllEggInventoryReductions: LiveData<List<EggInventoryReduction>>
    val allTheEggReductionsByDate: LiveData<List<DateQuantityInteger>>
    val allTheEggReductionsByReductionReason: LiveData<List<NameQuantityInteger>>
    val allEggSales: LiveData<Double>
/*

    private val _eggSales: MutableLiveData<Double?> = MutableLiveData(0.0)
    val eggSales: LiveData<Double?> = _eggSales
*/

    private val _eggsReducedByEggType: MutableLiveData<Int?> = MutableLiveData(0)
    val eggsReducedByEggType: LiveData<Int?> = _eggsReducedByEggType

    private val _eggReductionsByDate: MutableLiveData<List<DateQuantityInteger>> = MutableLiveData(mutableListOf())
    val eggReductionsByDate: LiveData<List<DateQuantityInteger>> = _eggReductionsByDate

    private val _eggReductionsByReductionReason: MutableLiveData<List<NameQuantityInteger>> = MutableLiveData(mutableListOf())
    val eggReductionsByReductionReason: LiveData<List<NameQuantityInteger>> = _eggReductionsByReductionReason

    init {
        val eggInventoryReductionDao =
            AppDatabase.getDatabase(application).eggInventoryReductionDao()
        repository = EggInventoryReductionRepository(eggInventoryReductionDao)
        displayAllEggInventoryReductions = repository.displayAllEggInventoryReduction
        allTheEggReductionsByDate = repository.eggReductionsByDate
        allTheEggReductionsByReductionReason = repository.eggReductionsByReductionReason
        allEggSales = repository.allEggSales
    }

    fun displayEggInventoryReductionByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayEggInventoryReductionByDate(number, firstDate, lastDate)
        }
        return repository.displayEggInventoryReductionByDate(number, firstDate, lastDate)
    }

    fun numberOfEggsSoldPerDuration(firstDate: Date, lastDate: Date): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.numberOfEggsSoldPerDuration(firstDate, lastDate)
        }
        return repository.numberOfEggsSoldPerDuration(firstDate, lastDate)
    }

    fun getEggsReducedByEggType(eggType: String) {
        viewModelScope.launch {
            val eggsReduced:Int? = repository.sumOfEggReductionsByEggType(eggType)
            _eggsReducedByEggType.postValue(eggsReduced)
        }
    }
    fun getEggReductionsByDate(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val eggInventoryReduced:List<DateQuantityInteger> = repository.eggReductionsByDate(firstDate,lastDate)
            _eggReductionsByDate.postValue(eggInventoryReduced)
        }
    }

    fun eggSalesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch {
            repository.eggSalesPerDuration(firstDate,lastDate)
        }
        return repository.eggSalesPerDuration(firstDate, lastDate)
    }

    fun getEggReductionsByReductionReason(firstDate: Date, lastDate: Date) {
        viewModelScope.launch {
            val eggInventoryReduced:List<NameQuantityInteger> = repository.eggReductionsByReductionReason(firstDate,lastDate)
            _eggReductionsByReductionReason.postValue(eggInventoryReduced)
        }
    }

    fun displayEggInventoryReductionByReductionReason(reductionReason: String, firstDate: Date, lastDate: Date): LiveData<List<EggInventoryReduction>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayEggInventoryReductionByReductionReason(reductionReason, firstDate, lastDate)
        }
        return repository.displayEggInventoryReductionByReductionReason(reductionReason, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun delete(eggInventoryReduction: EggInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(eggInventoryReduction)
        }
    }

    fun addEggInventoryReduction(eggInventoryReduction: EggInventoryReduction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEggInventory(eggInventoryReduction)
        }
    }

    fun updateEggInventoryReduction(
        eggInventoryReductionId: Int,
        dateReduced: Date,
        reductionReason: String,
        eggType: String,
        no_OfEggs: Int,
        customer: String,
        eggCost: Double,
        amountPaid: Double,
        amountOwed: Double,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEggInventoryReduction(
                eggInventoryReductionId,
                dateReduced,
                reductionReason,
                eggType,
                no_OfEggs,
                customer,
                eggCost,
                amountPaid,
                amountOwed,
                shortNotes
            )
        }
    }
}
