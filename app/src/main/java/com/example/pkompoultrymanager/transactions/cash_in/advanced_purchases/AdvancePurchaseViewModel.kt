package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

class AdvancePurchaseViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllAdvancePurchases: LiveData<List<AdvancePurchase>>
    private val repository: AdvancePurchaseRepository

    init {
        val advancePurchaseDao = AppDatabase.getDatabase(application).advancePurchaseDao()
        repository = AdvancePurchaseRepository(advancePurchaseDao)
        displayAllAdvancePurchases = repository.displayAllAdvancePurchase
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun delete(advancePurchase: AdvancePurchase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(advancePurchase)
        }
    }

    fun addAdvancePurchase(advancePurchase: AdvancePurchase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAdvancePurchase(advancePurchase)
        }
    }

    fun displayAdvancePurchaseFromAtoB(firstDate: Date, lastDate: Date):LiveData<List<AdvancePurchase>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayAdvancePurchaseFromAtoB(firstDate, lastDate)
        }
        return repository.displayAdvancePurchaseFromAtoB(firstDate,lastDate)
    }
    fun displayAdvancePurchaseByDate(number: Int, firstDate: Date, lastDate: Date):LiveData<List<AdvancePurchase>> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.displayAdvancePurchaseByDate(number, firstDate, lastDate)
        }
        return repository.displayAdvancePurchaseByDate(number, firstDate, lastDate)
    }

    fun updateAdvancePurchase(
        advancedPurchaseId: Int, date: Date, customer: String, itemPurchased: String, itemPurchased_Cost: String, amountReceived: Double, receiptNumber:String, hasBeenDelivered:Boolean, productDeliveryDate: Date?, hasMoneyBeenReturned:Boolean, amountReturned: Double, shortNotes:String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAdvancePurchase(
                advancedPurchaseId, date, customer, itemPurchased, itemPurchased_Cost, amountReceived, receiptNumber, hasBeenDelivered, productDeliveryDate, hasMoneyBeenReturned, amountReturned, shortNotes
            )
        }
    }
/*
    fun deleteAdvancePurchase(advancePurchaseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEggInventory(advancePurchaseId)

        }
    }*/

}
