package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.NameQuantityAmount
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import java.sql.Date
import java.time.LocalDate
import java.util.*

class OperationalExpensesRepository(private val operationalExpensesDao: OperationalExpensesDao) {

    val displayAllOperationalExpenses: LiveData<List<OperationalExpenses>> =
        operationalExpensesDao.displayAllOperationalExpenses()

    val allOperationalExpensesByDate: LiveData<List<NameQuantityAmount>> =
        operationalExpensesDao.allOperationalExpensesByDate()

    val allOperationalExpensesAmountByName: LiveData<List<NameQuantityDouble>> =
        operationalExpensesDao.allOperationalExpensesAmountByName()

    val amountOfAllOperationalExpenses: LiveData<Double> =
        operationalExpensesDao.amountOfAllOperationalExpenses()

    suspend fun operationalExpensesByDate(firstDate: Date, lastDate: Date): List<NameQuantityAmount> =
        operationalExpensesDao.operationalExpensesByDate(firstDate, lastDate)

    fun operationalExpensesAmountByName(firstDate: Date, lastDate: Date) : LiveData<List<NameQuantityDouble>> =
        operationalExpensesDao.operationalExpensesAmountByName(firstDate, lastDate)

    fun amountOfOperationalExpensesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        operationalExpensesDao.amountOfOperationalExpensesPerDuration(firstDate, lastDate)

    fun displayOperationalExpensesByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<OperationalExpenses>> =
        operationalExpensesDao.displayOperationalExpensesByDate(number, firstDate, lastDate )

    suspend fun addOperationalExpenses(operationalExpenses: OperationalExpenses){
        operationalExpensesDao.addOperationalExpenses(operationalExpenses)
    }

    suspend fun updateOperationalExpensesInfo(operationalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String){
        operationalExpensesDao.updateOperationalExpensesInfo(operationalExpensesId, date, expenseName, paymentMethod, amount, transactionID, shortNotes)
    }

     fun deleteAll(){
        operationalExpensesDao.deleteAll()
    }
    suspend fun delete(operationalExpenses: OperationalExpenses){
        operationalExpensesDao.delete(operationalExpenses)
    }

}