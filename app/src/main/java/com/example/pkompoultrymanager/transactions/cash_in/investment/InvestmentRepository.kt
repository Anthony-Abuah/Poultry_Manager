package com.example.pkompoultrymanager.transactions.cash_in.investment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import java.sql.Date
import java.time.LocalDate
import java.util.*

class InvestmentRepository(private val investmentDao: InvestmentDao) {

    val displayAllInvestments: LiveData<List<Investment>> =
        investmentDao.displayAllInvestment()


    suspend fun addInvestment(investment: Investment){
        investmentDao.addInvestment(investment)
    }

    fun displayInvestmentByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<Investment>> =
        investmentDao.displayInvestmentByDate(number, firstDate, lastDate )


    suspend fun updateInvestmentInfo(investmentId: Int, date: Date, investor:String, amount: Double, transactionID: String, shortNotes:String){
        investmentDao.updateInvestmentInfo(investmentId, date, investor, amount, transactionID, shortNotes)
    }

     fun deleteAll(){
        investmentDao.deleteAll()
    }
    suspend fun delete(investment: Investment){
        investmentDao.delete(investment)
    }

}