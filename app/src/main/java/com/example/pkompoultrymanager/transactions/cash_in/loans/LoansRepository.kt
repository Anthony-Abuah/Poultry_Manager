package com.example.pkompoultrymanager.transactions.cash_in.loans

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.pkompoultrymanager.transactions.cash_in.investment.Investment
import java.sql.Date
import java.time.LocalDate
import java.util.*

class LoansRepository(private val loansDao: LoansDao) {
/*
    val number:Int = 100
    val amountReceived:Double = 0.00
    val lender:String = ""
    val transactionID:String = ""




    @RequiresApi(Build.VERSION_CODES.O)
    val firstDate:Date = Date.valueOf(LocalDate.now().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    val lastDate:Date = Date.valueOf(LocalDate.now().toString())


    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoans: LiveData<List<Loans>> =
        loansDao.displayLoans(number, firstDate, lastDate )

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoansForTransactionID: LiveData<List<Loans>> =
        loansDao.displayLoansForTransactionID(transactionID, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoansForItemPurchased: LiveData<List<Loans>> =
        loansDao.displayLoansForLender(lender, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val displayLoansForLoanAmountReceived: LiveData<List<Loans>> =
        loansDao.displayLoansForLoanAmountReceived(amountReceived, firstDate, lastDate)


    @RequiresApi(Build.VERSION_CODES.O)
    val sumOfLoanAmountReceivedForLender: LiveData<Double> =
        loansDao.sumOfLoanAmountReceivedForLender(lender, firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val numberOfLoansReceived: LiveData<Int> =
        loansDao.numberOfLoansReceived(firstDate, lastDate)

    @RequiresApi(Build.VERSION_CODES.O)
    val sumTotalOfAmountInvested: LiveData<Double> =
        loansDao.sumTotalOfLoanAmountReceived(firstDate, lastDate)
*/
    val displayAllLoans: LiveData<List<Loans>> =
        loansDao.displayAllLoans()

    suspend fun addLoans(loans: Loans){
        loansDao.addLoans(loans)
    }
    fun displayLoansByDate(number: Int, firstDate: Date, lastDate: Date): LiveData<List<Loans>> =
        loansDao.displayLoansByDate(number, firstDate, lastDate )

    suspend fun updateLoansInfo(loansId: Int, date: Date, lender:String, amount: Double, transactionID: String, shortNotes:String){
        loansDao.updateLoansInfo(loansId, date, lender, amount, transactionID, shortNotes)
    }

     fun deleteAll(){
        loansDao.deleteAll()
    }
    suspend fun delete(loans: Loans){
        loansDao.delete(loans)
    }

}