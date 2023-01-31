package com.example.pkompoultrymanager.transactions.cash_out.loan_repayment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import java.sql.Date
import java.time.LocalDate
import java.util.*

class LoanRepaymentRepository(private val loanRepaymentDao: LoanRepaymentDao) {
/*

    val number: Int = 100
    val amount: Double = 0.00
    val lender: String = ""
    val transactionID: String = ""
    val paymentMethod: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    val firstDate: Date = Date.valueOf(LocalDate.now().toString())

    @RequiresApi(Build.VERSION_CODES.O)
    val lastDate: Date = Date.valueOf(LocalDate.now().toString())


    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoanRepayment: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepayment(number, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoanRepaymentForTransactionID: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepaymentForTransactionID(transactionID, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoanRepaymentForPaymentMethod: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepaymentForPaymentMethod(paymentMethod, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoanRepaymentForLender: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepaymentForLender(lender, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoanRepaymentForLoanAmountRepaid: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepaymentForLoanAmountRepaid(amount, firstDate, lastDate)


    @RequiresApi(Build.VERSION_CODES.O)
    val sumOfLoanAmountRepaidForLender: LiveData<Double> =
        loanRepaymentDao.sumOfLoanAmountRepaidForLender(lender, firstDate, lastDate)


    @RequiresApi(Build.VERSION_CODES.O)
    val sumOfTotalLoanAmountRepaid: LiveData<Double> =
        loanRepaymentDao.sumOfTotalLoanAmountRepaid(firstDate, lastDate)
*/
    val displayAllLoanRepayment: LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayAllLoanRepayment()

    fun displayLoanRepaymentByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<LoanRepayment>> =
        loanRepaymentDao.displayLoanRepaymentByDate(number, firstDate, lastDate )

    suspend fun addLoanRepayment(loanRepayment: LoanRepayment) {
        loanRepaymentDao.addLoanRepayment(loanRepayment)
    }

    suspend fun updateLoanRepayment(
        loanRepaymentId: Int,
        date: Date,
        lender: String,
        paymentMethod: String,
        amount: Double,
        transactionID: String,
        shortNotes: String
    ) {
        loanRepaymentDao.updateLoanRepayment(
            loanRepaymentId,
            date,
            lender,
            paymentMethod,
            amount,
            transactionID,
            shortNotes
        )
    }

    fun deleteAll() {
        loanRepaymentDao.deleteAll()
    }

    suspend fun delete(loanRepayment: LoanRepayment) {
        loanRepaymentDao.delete(loanRepayment)
    }

}