package com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface AdvancePurchaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAdvancePurchase(advancedPurchase: AdvancePurchase)

    @Query("SELECT * FROM AdvancePurchase  ORDER BY Date DESC")
    fun displayAllAdvancePurchase(): LiveData<List<AdvancePurchase>>

    @Query("SELECT * FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayAdvancePurchaseByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>

    @Query("SELECT * FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY AdvancePurchaseId DESC")
    fun displayAdvancePurchaseFromAtoB(firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>

    @Query("UPDATE AdvancePurchase SET Date=:date, Customer=:customer, ItemPurchased=:itemPurchased, ItemPurchased_Cost =:itemPurchased_Cost, AmountReceived=:amountReceived, Customer=:customer, HasBeenDelivered=:hasBeenDelivered, ItemDeliveryDate=:productDeliveryDate, MoneyReturned=:hasMoneyBeenReturned, ReceiptNumber=:receiptNumber, AmountReturned=:amountReturned, shortNotes=:shortNotes WHERE AdvancePurchaseId LIKE :advancedPurchaseId")
    suspend fun updateAdvancePurchase(advancedPurchaseId: Int, date: Date, customer: String, itemPurchased: String, itemPurchased_Cost: String, amountReceived: Double, receiptNumber:String, hasBeenDelivered:Boolean, productDeliveryDate: Date?, hasMoneyBeenReturned:Boolean, amountReturned: Double, shortNotes:String)

    @Delete
    suspend fun delete(advancedPurchase: AdvancePurchase)

    @Query("DELETE FROM AdvancePurchase")
    fun deleteAll()

    @Query("SELECT * FROM AdvancePurchase WHERE Customer LIKE :customer AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForCustomer(customer:String, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>


    @Query("SELECT * FROM AdvancePurchase WHERE ItemPurchased LIKE :itemPurchased AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForItemPurchased(itemPurchased:String, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>


    @Query("SELECT * FROM AdvancePurchase WHERE ReceiptNumber LIKE :receiptNumber AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForReceiptNumber(receiptNumber: String, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>


    @Query("SELECT * FROM AdvancePurchase WHERE ItemPurchased_Cost >= :itemPurchased_Cost AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForItemPurchasedCost(itemPurchased_Cost: Double, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>

    @Query("SELECT * FROM AdvancePurchase WHERE HasBeenDelivered >= :hasBeenSupplied AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForItemSupplied(hasBeenSupplied: Boolean, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>


    @Query("SELECT * FROM AdvancePurchase WHERE AmountReceived >= :amountReceived AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAdvancePurchaseForAmountReceived(amountReceived: Double, firstDate: Date, lastDate: Date): LiveData<List<AdvancePurchase>>



    @Query("SELECT SUM(AmountReceived) FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumOfAmountReceived(firstDate: Date, lastDate: Date): LiveData<Double>


    @Query("SELECT SUM(ItemPurchased_Cost) FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumOfItemPurchasedCost(firstDate: Date, lastDate: Date): LiveData<Double>


    @Query("SELECT SUM(AmountReceived) FROM AdvancePurchase WHERE Customer LIKE :customer AND Date BETWEEN :firstDate AND :lastDate")
    fun sumOfAdvancePurchaseForCustomer(customer:String, firstDate: Date, lastDate: Date): LiveData<Int>


    @Query("SELECT COUNT(Date) FROM AdvancePurchase WHERE Customer LIKE :customer AND Date BETWEEN :firstDate AND :lastDate")
    fun numberOfAdvancePurchaseForCustomer(customer:String, firstDate: Date, lastDate: Date): LiveData<Int>


    @Query("SELECT SUM(AmountReceived) FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumOfTotalAmountReceived(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(ItemPurchased_Cost) FROM AdvancePurchase WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumOfTotalItemPurchasedCost(firstDate: Date, lastDate: Date): LiveData<Int>



}