package com.example.pkompoultrymanager.transactions.cash_in.loans

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface LoansDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLoans(loans: Loans)

    @Query("SELECT * FROM Loans ORDER BY LoanID DESC ")
    fun displayAllLoans(): LiveData<List<Loans>>

    @Query("SELECT * FROM Loans WHERE :lastDate > Date > :firstDate  ORDER BY LoanID DESC LIMIT :number")
    fun displayLoans(number:Int,firstDate: Date, lastDate: Date): LiveData<List<Loans>>

    @Query("SELECT * FROM Loans WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayLoansByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<Loans>>

    @Query("UPDATE Loans SET Date=:date, Lender=:lender, LoanAmountReceived=:amount, TransactionID=:transactionID, shortNotes=:shortNotes WHERE LoanId LIKE :loansId")
    suspend fun updateLoansInfo(loansId: Int, date: Date, lender:String, amount: Double, transactionID: String, shortNotes:String)

    @Delete
    suspend fun delete(loans: Loans)

    @Query("DELETE FROM Loans")
    fun deleteAll()

    @Query("SELECT * FROM Loans WHERE Lender LIKE :lender AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoansForLender(lender:String, firstDate: Date, lastDate: Date): LiveData<List<Loans>>


    @Query("SELECT * FROM Loans WHERE TransactionID = :transactionID AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoansForTransactionID(transactionID: String, firstDate: Date, lastDate: Date): LiveData<List<Loans>>

    @Query("SELECT * FROM Loans WHERE LoanAmountReceived >= :amount AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoansForLoanAmountReceived(amount: Double, firstDate: Date, lastDate: Date): LiveData<List<Loans>>


    @Query("SELECT COUNT(Date) FROM Loans WHERE Date BETWEEN :firstDate AND :lastDate")
    fun numberOfLoansReceived(firstDate: Date, lastDate: Date): LiveData<Int>


    @Query("SELECT SUM(LoanAmountReceived) FROM Loans WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumTotalOfLoanAmountReceived(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(LoanAmountReceived) FROM Loans WHERE Lender LIKE :lender AND Date BETWEEN :firstDate AND :lastDate")
    fun sumOfLoanAmountReceivedForLender(lender:String, firstDate: Date, lastDate: Date): LiveData<Double>


}