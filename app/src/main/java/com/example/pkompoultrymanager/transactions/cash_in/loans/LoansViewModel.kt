package com.example.pkompoultrymanager.transactions.cash_in.loans

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class LoansViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllLoans: LiveData<List<Loans>>
    private val repository: LoansRepository

    init {
        val loansDao = AppDatabase.getDatabase(application).loansDao()
        repository = LoansRepository(loansDao)
        displayAllLoans = repository.displayAllLoans
    }
    fun displayLoansByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<Loans>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayLoansByDate(number, firstDate, lastDate)
        }
        return repository.displayLoansByDate(number, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addLoans(loans: Loans) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoans(loans)
        }
    }

    fun updateLoansInfo(
        loansId: Int, date: Date, lender:String, amount: Double, transactionID: String, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLoansInfo(
                loansId,
                date,
                lender,
                amount,
                transactionID,
                shortNotes
            )
        }
    }

    fun delete(loans: Loans) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(loans)

        }
    }

}
