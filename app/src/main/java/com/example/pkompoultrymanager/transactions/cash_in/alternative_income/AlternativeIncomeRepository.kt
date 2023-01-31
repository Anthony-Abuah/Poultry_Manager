package com.example.pkompoultrymanager.transactions.cash_in.alternative_income

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.DateNameAmount
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import java.sql.Date
import java.time.LocalDate
import java.util.*

class AlternativeIncomeRepository(private val alternativeIncomeDao: AlternativeIncomeDao) {



    val allAlternativeIncomeRevenue: LiveData<Double> =
        alternativeIncomeDao.allAlternativeIncomeRevenue()

    val displayAllAlternativeIncome: LiveData<List<AlternativeIncome>> =
        alternativeIncomeDao.displayAllAlternativeIncome()

    val allItemPurchasedCostByDate: LiveData<List<DateNameAmount>> =
        alternativeIncomeDao.allItemPurchasedCostByDate()


    fun displayAlternativeIncomeByDate(number: Int, firstDate: Date, lastDate: Date):  LiveData<List<AlternativeIncome>> =
        alternativeIncomeDao.displayAlternativeIncomeByDate(number, firstDate, lastDate)

    val allAlternativeIncomeRevenueByName:  LiveData<List<NameQuantityDouble>> =
        alternativeIncomeDao.allAlternativeIncomeRevenueByName()

    fun alternativeIncomeRevenueByName(firstDate: Date, lastDate: Date):  LiveData<List<NameQuantityDouble>> =
        alternativeIncomeDao.alternativeIncomeRevenueByName(firstDate, lastDate)

    fun alternativeIncomeRevenuePerDuration(firstDate: Date, lastDate: Date): LiveData<Double> =
        alternativeIncomeDao.alternativeIncomeRevenuePerDuration(firstDate, lastDate)

    suspend fun addAlternativeIncome(alternativeIncome: AlternativeIncome){
        alternativeIncomeDao.addAlternativeIncome(alternativeIncome)
    }

    suspend fun itemPurchasedCostByDate(firstDate: Date, lastDate: Date){
        alternativeIncomeDao.itemPurchasedCostByDate(firstDate, lastDate)
    }

    suspend fun updateAlternativeIncome(alternativeIncomeId: Int, date: Date, customer:String, itemPurchased: String, itemPurchased_Quantity: Int, itemPurchased_Cost: Double, amountReceived: Double, amountOwed:Double, receiptNumber:String, shortNotes:String){
        alternativeIncomeDao.updateAlternativeIncome(alternativeIncomeId, date, customer, itemPurchased, itemPurchased_Quantity, itemPurchased_Cost, amountReceived, amountOwed, receiptNumber, shortNotes)
    }

     fun deleteAll(){
        alternativeIncomeDao.deleteAll()
    }
    suspend fun delete(alternativeIncome: AlternativeIncome){
        alternativeIncomeDao.delete(alternativeIncome)
    }

}