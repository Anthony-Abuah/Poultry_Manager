package com.example.pkompoultrymanager.transactions.cash_in.alternative_income

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.DateNameAmount
import com.example.pkompoultrymanager.reports.income_expenses.DateQuantityAmount
import java.sql.Date


@Dao
interface AlternativeIncomeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAlternativeIncome(alternativeIncome: AlternativeIncome)

    @Query("SELECT * FROM AlternativeIncome  ORDER BY alternativeIncomeID DESC")
    fun displayAllAlternativeIncome(): LiveData<List<AlternativeIncome>>

    @Query("SELECT * FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayAlternativeIncomeByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>

    @Query("UPDATE AlternativeIncome SET Date=:date, Customer=:customer, ItemPurchased=:itemPurchased, ItemPurchased_Quantity=:itemPurchased_Quantity, AmountOwed =:amountOwed, ItemPurchased_Cost=:itemPurchased_Cost, AmountReceived=:amountReceived, ReceiptNumber=:receiptNumber, shortNotes=:shortNotes WHERE AlternativeIncomeId LIKE :alternativeIncomeId")
    suspend fun updateAlternativeIncome(alternativeIncomeId: Int, date: Date, customer:String, itemPurchased: String, itemPurchased_Quantity: Int, itemPurchased_Cost: Double, amountReceived: Double, amountOwed:Double, receiptNumber:String, shortNotes:String)

    @Delete
    suspend fun delete(alternativeIncome: AlternativeIncome)

    @Query("DELETE FROM AlternativeIncome")
    fun deleteAll()

    @Query("SELECT * FROM AlternativeIncome WHERE Customer LIKE :customer AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAlternativeIncomeForCustomer(customer:String, firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>

    @Query("SELECT COUNT(Date) FROM AlternativeIncome WHERE Customer LIKE :customer AND Date BETWEEN :firstDate AND :lastDate")
    fun numberOfAlternativeIncomeForCustomer(customer:String, firstDate: Date, lastDate: Date): LiveData<Int>


    @Query("SELECT * FROM AlternativeIncome WHERE ItemPurchased LIKE :itemPurchased AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAlternativeIncomeForItemPurchased(itemPurchased:String, firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>


    @Query("SELECT * FROM AlternativeIncome WHERE AmountOwed > :amountOwed AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAlternativeIncomeForAmountOwed(amountOwed: Double, firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>


    @Query("SELECT * FROM AlternativeIncome WHERE AmountReceived > :amountReceived AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAlternativeIncomeForAmountReceived(amountReceived: Double, firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>


    @Query("SELECT * FROM AlternativeIncome WHERE ItemPurchased_Cost >= :itemPurchased_Cost AND Date BETWEEN :firstDate AND :lastDate")
    fun displayAlternativeIncomeForItemPurchasedCost(itemPurchased_Cost: Double, firstDate: Date, lastDate: Date): LiveData<List<AlternativeIncome>>


    @Query("SELECT SUM(AmountReceived) FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumTotalOfAmountReceived(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(AmountOwed) FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumTotalOfAmountOwed(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT Date as date, ItemPurchased as name, SUM(ItemPurchased_Cost) as amount FROM AlternativeIncome GROUP BY Date ORDER BY Date ASC")
    fun allItemPurchasedCostByDate(): LiveData<List<DateNameAmount>>

    @Query("SELECT SUM(ItemPurchased_Cost) FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate")
    fun alternativeIncomeRevenuePerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(ItemPurchased_Cost) FROM AlternativeIncome ")
    fun allAlternativeIncomeRevenue(): LiveData<Double>

    @Query("SELECT Date as date, ItemPurchased as name, SUM(ItemPurchased_Cost) as amount FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate GROUP BY Date ORDER BY Date ASC")
    suspend fun itemPurchasedCostByDate(firstDate: Date, lastDate: Date): List<DateNameAmount>

    @Query("SELECT ItemPurchased as name, SUM(ItemPurchased_Cost) as quantity FROM AlternativeIncome WHERE Date BETWEEN :firstDate AND :lastDate GROUP BY ItemPurchased")
    fun alternativeIncomeRevenueByName(firstDate: Date, lastDate: Date): LiveData<List<NameQuantityDouble>>

    @Query("SELECT ItemPurchased as name, SUM(ItemPurchased_Cost) as quantity FROM AlternativeIncome GROUP BY ItemPurchased")
    fun allAlternativeIncomeRevenueByName(): LiveData<List<NameQuantityDouble>>


}