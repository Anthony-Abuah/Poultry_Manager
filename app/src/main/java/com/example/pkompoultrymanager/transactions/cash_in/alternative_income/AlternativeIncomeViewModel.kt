package com.example.pkompoultrymanager.transactions.cash_in.alternative_income

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class AlternativeIncomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlternativeIncomeRepository

    val displayAllAlternativeIncomes: LiveData<List<AlternativeIncome>>
    val allAlternativeIncomeRevenueByName: LiveData<List<NameQuantityDouble>>
    val allAlternativeIncomeRevenue: LiveData<Double>

    init {
        val alternativeIncomeDao = AppDatabase.getDatabase(application).alternativeIncomeDao()
        repository = AlternativeIncomeRepository(alternativeIncomeDao)
        displayAllAlternativeIncomes = repository.displayAllAlternativeIncome
        allAlternativeIncomeRevenueByName = repository.allAlternativeIncomeRevenueByName
        allAlternativeIncomeRevenue = repository.allAlternativeIncomeRevenue
    }

    fun displayAlternativeIncomeByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<AlternativeIncome>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayAlternativeIncomeByDate(number, firstDate, lastDate)
        }
        return repository.displayAlternativeIncomeByDate(number, firstDate, lastDate)
    }
    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun delete(alternativeIncome: AlternativeIncome) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(alternativeIncome)
        }
    }

    fun addAlternativeIncome(alternativeIncome: AlternativeIncome) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlternativeIncome(alternativeIncome)
        }
    }
    fun alternativeIncomeRevenuePerDuration(firstDate: Date, lastDate: Date): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.alternativeIncomeRevenuePerDuration(firstDate, lastDate)
        }
        return repository.alternativeIncomeRevenuePerDuration(firstDate, lastDate)
    }

    fun alternativeIncomeRevenueByName(firstDate: Date, lastDate: Date): LiveData<List<NameQuantityDouble>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.alternativeIncomeRevenueByName(firstDate, lastDate)
        }
        return repository.alternativeIncomeRevenueByName(firstDate, lastDate)
    }

    fun updateAlternativeIncome(
        alternativeIncomeId: Int, date: Date, customer:String, itemPurchased: String, itemPurchased_Quantity: Int, itemPurchased_Cost: Double, amountReceived: Double, amountOwed:Double, receiptNumber:String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlternativeIncome(
                alternativeIncomeId,
                date,
                customer,
                itemPurchased,
                itemPurchased_Quantity,
                itemPurchased_Cost,
                amountReceived,
                amountOwed,
                receiptNumber,
                shortNotes
            )
        }
    }
/*
    fun deleteAlternativeIncome(alternativeIncomeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(alternativeIncomeId)

        }
    }*/

}
