package com.example.pkompoultrymanager.transactions.cash_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.transactions.cash_in.advanced_purchases.fragments.AdvancedPurchaseList
import com.example.pkompoultrymanager.transactions.cash_in.alternative_income.fragments.AlternativeIncomeList
import com.example.pkompoultrymanager.transactions.cash_in.investment.fragments.InvestmentList
import com.example.pkompoultrymanager.transactions.cash_in.loans.fragments.LoansList

class CashInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cash_in, container, false)


        val clAdvancedPurchase = view.findViewById<ConstraintLayout>(R.id.clAdvancePurchases_CashInFragment)
        val clAlternateIncome = view.findViewById<ConstraintLayout>(R.id.clAlternativeIncome_CashInFragment)
        val clInvestment = view.findViewById<ConstraintLayout>(R.id.clInvestment_CashInFragment)
        val clLoans = view.findViewById<ConstraintLayout>(R.id.clLoans_CashInFragment)



        clAdvancedPurchase.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AdvancedPurchaseList()
            ).addToBackStack(null).commit()
        }
        clAlternateIncome.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                AlternativeIncomeList()
            ).addToBackStack(null).commit()
        }
        clInvestment.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                InvestmentList()
            ).addToBackStack(null).commit()
        }
        clLoans.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashInActivity,
                LoansList()
            ).addToBackStack(null).commit()
        }


        return view
    }

}