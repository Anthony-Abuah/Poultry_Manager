package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchase
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.AdvancePurchaseDao
import java.sql.Date
import java.time.LocalDate
import java.util.*

class AdvancePurchaseRepository(private val advancePurchaseDao: AdvancePurchaseDao) {

  /*  val firstDate = null
    val lastDate = null*/


/*

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    val lastDate:Date = Date.valueOf(LocalDate.now().toString())


    @RequiresApi(Build.VERSION_CODES.O)


    @RequiresApi(Build.VERSION_CODES.O)
    val displayAdvancePurchaseForCustomer: LiveData<List<AdvancePurchase>> =
        advancePurchaseDao.displayAdvancePurchaseForCustomer(customer, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayAdvancePurchaseForAmountReceived: LiveData<List<AdvancePurchase>> =
        advancePurchaseDao.displayAdvancePurchaseForAmountReceived(amountReceived, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val sumOfAdvancePurchaseForCustomer: LiveData<Int> =
        advancePurchaseDao.sumOfAdvancePurchaseForCustomer(customer, firstDate, lastDate)
*/

    val displayAllAdvancePurchase: LiveData<List<AdvancePurchase>> = advancePurchaseDao.displayAllAdvancePurchase()

    suspend fun addAdvancePurchase(advancePurchase: AdvancePurchase){
        advancePurchaseDao.addAdvancePurchase(advancePurchase)
    }

    fun displayAdvancePurchaseByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>> =
        advancePurchaseDao.displayAdvancePurchaseByDate(number, firstDate, lastDate )


    fun displayAdvancePurchaseFromAtoB(firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>> =
        advancePurchaseDao.displayAdvancePurchaseFromAtoB(firstDate, lastDate )


    suspend fun updateAdvancePurchase(advancedPurchaseId: Int, date: Date, customer: String, itemPurchased: String, itemPurchased_Cost: String, amountReceived: Double, receiptNumber:String, hasBeenDelivered:Boolean, productDeliveryDate: Date?, hasMoneyBeenReturned:Boolean, amountReturned: Double, shortNotes:String){
        advancePurchaseDao.updateAdvancePurchase(advancedPurchaseId, date, customer, itemPurchased, itemPurchased_Cost, amountReceived, receiptNumber, hasBeenDelivered, productDeliveryDate, hasMoneyBeenReturned, amountReturned, shortNotes)
    }

     fun deleteAll(){
        advancePurchaseDao.deleteAll()
    }
    suspend fun delete(advancePurchase: AdvancePurchase){
        advancePurchaseDao.delete(advancePurchase)
    }

}