package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.OperationalExpenses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class CapitalExpensesViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllCapitalExpensess: LiveData<List<CapitalExpenses>>
    private val repository: CapitalExpensesRepository

    init {
        val capitalExpensesDao = AppDatabase.getDatabase(application).capitalExpensesDao()
        repository = CapitalExpensesRepository(capitalExpensesDao)
        displayAllCapitalExpensess = repository.displayAllCapitalExpenses
    }
    fun displayCapitalExpensesByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<CapitalExpenses>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayCapitalExpensesByDate(number, firstDate, lastDate)
        }
        return repository.displayCapitalExpensesByDate(number, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun delete(capitalExpenses: CapitalExpenses) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(capitalExpenses)
        }
    }

    fun addCapitalExpenses(capitalExpenses: CapitalExpenses) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCapitalExpenses(capitalExpenses)
        }
    }

    fun updateCapitalExpensesInfo(
        capitalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCapitalExpensesInfo(
                capitalExpensesId,
                date,
                expenseName,
                paymentMethod,
                amount,
                transactionID,
                shortNotes
            )
        }
    }
/*
    fun deleteCapitalExpenses(capitalExpensesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(capitalExpensesId)

        }
    }*/

}
