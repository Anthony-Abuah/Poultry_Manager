package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment

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

class LoanRepaymentViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllLoanRepayments: LiveData<List<LoanRepayment>>
    private val repository: LoanRepaymentRepository

    init {
        val loanRepaymentDao = AppDatabase.getDatabase(application).loanRepaymentDao()
        repository = LoanRepaymentRepository(loanRepaymentDao)
        displayAllLoanRepayments = repository.displayAllLoanRepayment
    }
    fun displayLoanRepaymentByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<LoanRepayment>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayLoanRepaymentByDate(number, firstDate, lastDate)
        }
        return repository.displayLoanRepaymentByDate(number, firstDate, lastDate)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun delete(loanRepayment: LoanRepayment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(loanRepayment)
        }
    }

    fun addLoanRepayment(loanRepayment: LoanRepayment) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoanRepayment(loanRepayment)
        }
    }

    fun updateLoanRepayment(
        loanRepaymentId: Int,
        date: Date,
        lender: String,
        paymentMethod: String,
        amount: Double,
        transactionID: String,
        shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLoanRepayment(
                loanRepaymentId,
                date,
                lender,
                paymentMethod,
                amount,
                transactionID,
                shortNotes
            )
        }
    }
/*
    fun deleteLoanRepayment(loanRepaymentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(loanRepaymentId)

        }
    }*/

}
