package com.example.pkompoultrymanager.transactions.cash_in.investment

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface InvestmentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addInvestment(investment: Investment)

    @Query("SELECT * FROM Investment  ORDER BY investmentID DESC ")
    fun displayAllInvestment(): LiveData<List<Investment>>

    @Query("SELECT * FROM Investment WHERE  Date BETWEEN :firstDate AND :lastDate ORDER BY Date DESC LIMIT :number")
    fun displayInvestmentByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<Investment>>

    @Query("UPDATE Investment SET Date=:date, Investor=:investor, AmountInvested=:amount, TransactionID=:transactionID, shortNotes=:shortNotes WHERE InvestmentId LIKE :investmentId")
    suspend fun updateInvestmentInfo(investmentId: Int, date: Date, investor:String, amount: Double, transactionID: String, shortNotes:String)

    @Delete
    suspend fun delete(investment: Investment)

    @Query("DELETE FROM Investment")
    fun deleteAll()

    @Query("SELECT * FROM Investment WHERE Investor LIKE :investor AND Date BETWEEN :firstDate AND :lastDate")
    fun displayInvestmentForInvestor(investor:String, firstDate: Date, lastDate: Date): LiveData<List<Investment>>


    @Query("SELECT * FROM Investment WHERE TransactionID = :transactionID AND Date BETWEEN :firstDate AND :lastDate")
    fun displayInvestmentForTransactionID(transactionID: String, firstDate: Date, lastDate: Date): LiveData<List<Investment>>


    @Query("SELECT * FROM Investment WHERE AmountInvested >= :amountInvested AND Date BETWEEN :firstDate AND :lastDate")
    fun displayInvestmentForAmountInvested(amountInvested: Double, firstDate: Date, lastDate: Date): LiveData<List<Investment>>


    @Query("SELECT COUNT(Date) FROM Investment WHERE Date BETWEEN :firstDate AND :lastDate")
    fun numberOfInvestments(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(AmountInvested) FROM Investment WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumTotalOfAmountInvested(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(AmountInvested) FROM Investment WHERE Investor LIKE :investor AND Date BETWEEN :firstDate AND :lastDate")
    fun sumOfAmountReceivedForInvestor(investor:String, firstDate: Date, lastDate: Date): LiveData<Double>


}