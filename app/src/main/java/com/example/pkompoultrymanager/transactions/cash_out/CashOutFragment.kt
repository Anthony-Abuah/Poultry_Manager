package com.example.pkompoultrymanager.transactions.cash_out

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
import com.example.pkompoultrymanager.transactions.cash_out.capital_expenses.fragments.CapitalExpensesList
import com.example.pkompoultrymanager.transactions.cash_out.loan_repayment.fragments.LoanRepaymentList
import com.example.pkompoultrymanager.transactions.cash_out.operational_expenses.fragments.OperationalExpensesList

class CashOutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cash_out, container, false)

        val clOperationalExpenses = view.findViewById<ConstraintLayout>(R.id.clOperationalExpenses_CashOutFragment)
        val clCapitalExpenses = view.findViewById<ConstraintLayout>(R.id.clCapitalExpenses_CashOutFragment)
        val clLoanRepayment = view.findViewById<ConstraintLayout>(R.id.clLoanRepayment_CashOutFragment)


        clOperationalExpenses.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                OperationalExpensesList()
            ).addToBackStack(null).commit()
        }
        clCapitalExpenses.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                CapitalExpensesList()
            ).addToBackStack(null).commit()
        }
        clLoanRepayment.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flCashOutActivity,
                LoanRepaymentList()
            ).addToBackStack(null).commit()
        }


        return view
    }

}