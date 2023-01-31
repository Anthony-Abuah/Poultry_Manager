package com.example.pkompoultrymanager.transactions.cash_out.capital_expenses

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface CapitalExpensesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCapitalExpenses(capitalExpenses: CapitalExpenses)

    @Query("SELECT * FROM CapitalExpenses ORDER BY CapitalExpensesId DESC ")
    fun displayAllCapitalExpenses(): LiveData<List<CapitalExpenses>>

    @Query("SELECT * FROM CapitalExpenses WHERE :lastDate > Date > :firstDate  ORDER BY CapitalExpensesId DESC LIMIT :number")
    fun displayCapitalExpenses(number:Int,firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>

    @Query("SELECT * FROM CapitalExpenses WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayCapitalExpensesByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>

    @Query("UPDATE CapitalExpenses SET Date=:date, ExpenseName =:expenseName, ExpenseAmount=:amount, PaymentMethod =:paymentMethod, TransactionID=:transactionID, shortNotes=:shortNotes WHERE CapitalExpensesID LIKE :capitalExpensesId")
    suspend fun updateCapitalExpensesInfo(capitalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String)

    @Delete
    suspend fun delete(capitalExpenses: CapitalExpenses)

    @Query("DELETE FROM CapitalExpenses")
    fun deleteAll()

    @Query("SELECT * FROM CapitalExpenses WHERE ExpenseName LIKE :expenseName AND Date BETWEEN :firstDate AND :lastDate")
    fun displayCapitalExpensesForExpenseName(expenseName:String, firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>


    @Query("SELECT * FROM CapitalExpenses WHERE PaymentMethod LIKE :paymentMethod AND Date BETWEEN :firstDate AND :lastDate")
    fun displayCapitalExpensesForPaymentMethod(paymentMethod: String, firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>


    @Query("SELECT * FROM CapitalExpenses WHERE TransactionID = :transactionID AND Date BETWEEN :firstDate AND :lastDate")
    fun displayCapitalExpensesForTransactionID(transactionID: String, firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>


    @Query("SELECT * FROM CapitalExpenses WHERE ExpenseAmount >= :amount AND Date BETWEEN :firstDate AND :lastDate")
    fun displayCapitalExpensesForExpenseAmount(amount: Double, firstDate: Date, lastDate: Date): LiveData<List<CapitalExpenses>>


    @Query("SELECT SUM(ExpenseAmount) FROM CapitalExpenses WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumTotalOfExpenseAmount(firstDate: Date, lastDate: Date): LiveData<Double>


    @Query("SELECT SUM(ExpenseAmount) FROM CapitalExpenses WHERE ExpenseName LIKE :expenseName AND Date BETWEEN :firstDate AND :lastDate")
    fun sumOfExpenseAmountForExpenseName(expenseName: String, firstDate: Date, lastDate: Date): LiveData<Double>


}