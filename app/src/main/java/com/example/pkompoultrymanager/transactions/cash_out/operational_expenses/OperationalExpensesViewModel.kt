package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.reports.egg_inventory.DateQuantityInteger
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.NameQuantityAmount
import com.example.pkompoultrymanager.transactions.cash_in.loans.Loans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class OperationalExpensesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: OperationalExpensesRepository


    val displayAllOperationalExpensess: LiveData<List<OperationalExpenses>>
    val allOperationalExpensesByDate: LiveData<List<NameQuantityAmount>>
    val allOperationalExpensesAmountByName: LiveData<List<NameQuantityDouble>>
    val amountOfAllOperationalExpenses: LiveData<Double>


    private val _operationalExpenses: MutableLiveData<Double?> = MutableLiveData(0.0)
    val operationalExpenses: LiveData<Double?> = _operationalExpenses

    init {
        val operationalExpensesDao = AppDatabase.getDatabase(application).operationalExpensesDao()
        repository = OperationalExpensesRepository(operationalExpensesDao)
        displayAllOperationalExpensess = repository.displayAllOperationalExpenses
        allOperationalExpensesByDate = repository.allOperationalExpensesByDate
        allOperationalExpensesAmountByName = repository.allOperationalExpensesAmountByName
        amountOfAllOperationalExpenses = repository.amountOfAllOperationalExpenses
    }

    fun displayOperationalExpensesByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<OperationalExpenses>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayOperationalExpensesByDate(number, firstDate, lastDate)
        }
        return repository.displayOperationalExpensesByDate(number, firstDate, lastDate)
    }

    fun amountOfOperationalExpensesPerDuration(firstDate: Date, lastDate: Date):LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.amountOfOperationalExpensesPerDuration(firstDate, lastDate)
        }
        return repository.amountOfOperationalExpensesPerDuration(firstDate, lastDate)
    }

    fun operationalExpensesAmountByName(firstDate: Date, lastDate: Date):LiveData<List<NameQuantityDouble>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.operationalExpensesAmountByName(firstDate, lastDate)
        }
        return repository.operationalExpensesAmountByName(firstDate, lastDate)
    }


    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun delete(operationalExpenses: OperationalExpenses) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(operationalExpenses)
        }
    }

    fun addOperationalExpenses(operationalExpenses: OperationalExpenses) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOperationalExpenses(operationalExpenses)
        }
    }

    fun updateOperationalExpensesInfo(
        operationalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOperationalExpensesInfo(
                operationalExpensesId,
                date,
                expenseName,
                paymentMethod,
                amount,
                transactionID,
                shortNotes
            )
        }
    }


}
