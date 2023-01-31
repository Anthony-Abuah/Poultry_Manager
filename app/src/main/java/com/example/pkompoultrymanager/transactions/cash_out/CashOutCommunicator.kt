package com.example.pkompoultrymanager.transactions.cash_out

import android.os.Bundle
import java.sql.Date
import java.sql.SQLTimeoutException

interface CashOutCommunicator {
    fun passCapitalExpenses(
         CapitalExpensesId: String,
         ExpenseDate: String,
         ExpenseName: String,
         PaymentMethod: String,
         ExpenseAmount: String,
         TransactionID: String,
         ShortNotes: String
    )

    fun passOperationalExpenses(
        OperationalExpensesId: String,
        ExpenseDate: String,
        ExpenseName: String,
        PaymentMethod: String,
        ExpenseAmount: String,
        TransactionID: String,
        ShortNotes: String
    )

    fun passLoanRepayment(
         LoanRepaymentId: String,
         LoanRepaymentDate: String,
         Lender: String,
         PaymentMethod: String,
         LoanAmountRepaid: String,
         TransactionID: String,
         ShortNotes: String
    )


}