package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface LoanRepaymentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addLoanRepayment(loanRepayment: LoanRepayment)

    @Query("SELECT * FROM LoanRepayment ORDER BY LoanRepaymentId DESC ")
    fun displayAllLoanRepayment(): LiveData<List<LoanRepayment>>

    @Query("SELECT * FROM LoanRepayment WHERE Date BETWEEN :firstDate AND :lastDate  ORDER BY Date DESC LIMIT :number")
    fun displayLoanRepaymentByDate(number:Int,firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>>

    @Query("UPDATE LoanRepayment SET Date=:date, Lender =:lender, AmountRepaid=:amount, PaymentMethod =:paymentMethod, TransactionID=:transactionID, shortNotes=:shortNotes WHERE LoanRepaymentID LIKE :loanRepaymentId")
    suspend fun updateLoanRepayment(loanRepaymentId: Int, date: Date, lender:String, paymentMethod: String, amount:Double, transactionID: String, shortNotes:String)

    @Delete
    suspend fun delete(loanRepayment: LoanRepayment)

    @Query("DELETE FROM LoanRepayment")
    fun deleteAll()

    @Query("SELECT * FROM LoanRepayment WHERE Lender LIKE :lender AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoanRepaymentForLender(lender:String, firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>>


    @Query("SELECT * FROM LoanRepayment WHERE PaymentMethod LIKE :paymentMethod AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoanRepaymentForPaymentMethod(paymentMethod: String, firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>>


    @Query("SELECT * FROM LoanRepayment WHERE TransactionID = :transactionID AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoanRepaymentForTransactionID(transactionID: String, firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>>


    @Query("SELECT * FROM LoanRepayment WHERE AmountRepaid >= :amount AND Date BETWEEN :firstDate AND :lastDate")
    fun displayLoanRepaymentForLoanAmountRepaid(amount: Double, firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>>


    @Query("SELECT SUM(AmountRepaid) FROM LoanRepayment WHERE Date BETWEEN :firstDate AND :lastDate")
    fun sumOfTotalLoanAmountRepaid(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(AmountRepaid) FROM LoanRepayment WHERE Lender LIKE :lender AND Date BETWEEN :firstDate AND :lastDate")
    fun sumOfLoanAmountRepaidForLender(lender: String, firstDate: Date, lastDate: Date): LiveData<Double>


}