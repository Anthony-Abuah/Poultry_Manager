package com.example.pkompoultrymanager.transactions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.transactions.cash_in.CashInActivity
import com.example.pkompoultrymanager.transactions.cash_out.CashOutActivity

class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)


        val clCashInActivity = view.findViewById<ConstraintLayout>(R.id.clCashInTransaction_TransactionFragment)
        val clCashOutActivity = view.findViewById<ConstraintLayout>(R.id.clCashOutTransaction_TransactionFragment)

        clCashInActivity.setOnClickListener {
            startActivity(Intent(context, CashInActivity::class.java))
        }
        clCashOutActivity.setOnClickListener {
            startActivity(Intent(context, CashOutActivity::class.java))
        }


        return view
    }

}