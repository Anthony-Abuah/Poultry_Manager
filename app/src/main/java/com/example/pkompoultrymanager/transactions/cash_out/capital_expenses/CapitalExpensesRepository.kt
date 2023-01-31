package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import java.sql.Date
import java.time.LocalDate
import java.util.*

class CapitalExpensesRepository(private val capitalExpensesDao: CapitalExpensesDao) {
/*
    val number:Int = 100
    val amount:Double = 0.00
    val expense:String = ""
    val transactionID:String = ""
    val paymentMethod:String = ""



    @RequiresApi(Build.VERSION_CODES.O)
    val firstDate:Date = Date.valueOf(LocalDate.now().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    val lastDate:Date = Date.valueOf(LocalDate.now().toString())


    @RequiresApi(Build.VERSION_CODES.O)
    val displayCapitalExpenses: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpenses(number, firstDate, lastDate )

    @RequiresApi(Build.VERSION_CODES.O)
    val displayCapitalExpensesForTransactionID: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpensesForTransactionID(transactionID, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayCapitalExpensesForExpenseName: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpensesForExpenseName(expense, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayCapitalExpensesForExpenseAmount: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpensesForExpenseAmount(amount, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayCapitalExpensesForPaymentMethod: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpensesForPaymentMethod(paymentMethod, firstDate, lastDate)


    @RequiresApi(Build.VERSION_CODES.O)
    val sumOfExpenseAmountForExpenseName: LiveData<Double> =
        capitalExpensesDao.sumOfExpenseAmountForExpenseName(expense, firstDate, lastDate)


    @RequiresApi(Build.VERSION_CODES.O)
    val sumTotalOfExpenseAmount: LiveData<Double> =
        capitalExpensesDao.sumTotalOfExpenseAmount(firstDate, lastDate)
*/
    val displayAllCapitalExpenses: LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayAllCapitalExpenses()


    fun displayCapitalExpensesByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>> =
        capitalExpensesDao.displayCapitalExpensesByDate(number, firstDate, lastDate )

    suspend fun addCapitalExpenses(capitalExpenses: CapitalExpenses){
        capitalExpensesDao.addCapitalExpenses(capitalExpenses)
    }

    suspend fun updateCapitalExpensesInfo(capitalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String){
        capitalExpensesDao.updateCapitalExpensesInfo(capitalExpensesId, date, expenseName, paymentMethod, amount, transactionID, shortNotes)
    }

     fun deleteAll(){
        capitalExpensesDao.deleteAll()
    }
    suspend fun delete(capitalExpenses: CapitalExpenses){
        capitalExpensesDao.delete(capitalExpenses)
    }

}