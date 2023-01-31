package com.example.pkompoultrymanager.transactions.cash_out.operational_expenses

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.reports.feed_inventory.NameQuantityDouble
import com.example.pkompoultrymanager.reports.income_expenses.NameQuantityAmount
import java.sql.Date


@Dao
interface OperationalExpensesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addOperationalExpenses(operationalExpenses: OperationalExpenses)

    @Query("SELECT * FROM OperationalExpenses ORDER BY OperationalExpensesId DESC ")
    fun displayAllOperationalExpenses(): LiveData<List<OperationalExpenses>>

    @Query("SELECT * FROM OperationalExpenses WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayOperationalExpensesByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<OperationalExpenses>>

    @Query("UPDATE OperationalExpenses SET Date=:date, ExpenseName =:expenseName, ExpenseAmount=:amount, PaymentMethod =:paymentMethod, TransactionID=:transactionID, shortNotes=:shortNotes WHERE OperationalExpensesID LIKE :operationalExpensesId")
    suspend fun updateOperationalExpensesInfo(operationalExpensesId: Int, date: Date, expenseName:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String)

    @Delete
    suspend fun delete(operationalExpenses: OperationalExpenses)

    @Query("DELETE FROM OperationalExpenses")
    fun deleteAll()

    @Query("SELECT ExpenseName as name, SUM(ExpenseAmount) as quantity FROM OperationalExpenses WHERE Date BETWEEN :firstDate AND :lastDate  GROUP BY ExpenseName ")
    fun operationalExpensesAmountByName(firstDate: Date, lastDate: Date): LiveData<List<NameQuantityDouble>>

    @Query("SELECT ExpenseName as name, SUM(ExpenseAmount) as quantity FROM OperationalExpenses GROUP BY ExpenseName ")
    fun allOperationalExpensesAmountByName(): LiveData<List<NameQuantityDouble>>

    @Query("SELECT Date as name, ExpenseName as quantity, ExpenseAmount as amount FROM OperationalExpenses ORDER BY Date ASC")
    fun allOperationalExpensesByDate(): LiveData<List<NameQuantityAmount>>

    @Query("SELECT Date as name, ExpenseName as quantity, ExpenseAmount as amount FROM OperationalExpenses WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date ASC")
    suspend fun operationalExpensesByDate(firstDate: Date, lastDate: Date): List<NameQuantityAmount>

    @Query("SELECT SUM(ExpenseAmount) FROM OperationalExpenses WHERE Date BETWEEN :firstDate AND :lastDate")
    fun amountOfOperationalExpensesPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(ExpenseAmount) FROM OperationalExpenses")
    fun amountOfAllOperationalExpenses(): LiveData<Double>




}