package com.example.pkompoultrymanager.transactions.cash_in

import android.os.Bundle
import java.sql.Date

interface CashInCommunicator {
    fun passAdvancedPurchase(
        AdvancePurchaseId: String,
        Date: String,
        Customer: String,
        ProductPurchased: String,
        ProductPurchased_Cost: String,
        AmountReceived: String,
        ReceiptNumber: String,
        ProductDeliveredDate: String?,
        HasBeenDelivered: Boolean,
        MoneyReturned:Boolean,
        AmountReturned:String,
        shortNotes: String
    )

    fun passAlternativeIncome(
         AlternativeIncomeId: String,
         Date: String,
         ItemPurchased: String,
         Customer: String,
         ItemPurchasedQuantity: String,
         ItemPurchasedCost: String,
         AmountReceived: String,
         ReceiptNumber: String,
         shortNotes: String
    )

    fun passInvestment(
         InvestmentId: String,
         Date: String,
         Investor: String,
         AmountInvested: String,
         TransactionID: String,
         ShortNotes: String
    )
    fun passLoans(
         LoansId: String,
         Date: String,
         Lender: String,
         LoanAmount: String,
         TransactionID: String,
         ShortNotes: String
    )


}