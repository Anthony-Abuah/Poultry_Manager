package com.example.pkompoultrymanager.transactions.cash_in.investment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class InvestmentViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllInvestments: LiveData<List<Investment>>
    private val repository: InvestmentRepository

    init {
        val investmentDao = AppDatabase.getDatabase(application).investmentDao()
        repository = InvestmentRepository(investmentDao)
        displayAllInvestments = repository.displayAllInvestments
    }

    fun displayInvestmentByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<Investment>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayInvestmentByDate(number, firstDate, lastDate)
        }
        return repository.displayInvestmentByDate(number, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addInvestment(investment: Investment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addInvestment(investment)
        }
    }

    fun updateInvestmentInfo(
        investmentId: Int, date: Date, investor:String, amount: Double, transactionID: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateInvestmentInfo(
                investmentId,
                date,
                investor,
                amount,
                transactionID,
                shortNotes
            )
        }
    }

    fun delete(investment: Investment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(investment)

        }
    }

}
