package com.example.pkompoultrymanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.pkompoultrymanager.health.HealthActivity
import com.example.pkompoultrymanager.inventory.InventoryActivity
import com.example.pkompoultrymanager.reminder.ReminderActivity
import com.example.pkompoultrymanager.reports.ReportActivity
import com.example.pkompoultrymanager.tables.TablesActivity
import com.example.pkompoultrymanager.transactions.TransactionActivity

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)


        val llInventory = view.findViewById<LinearLayout>(R.id.llInventory_MainFragment)
        val llHealth = view.findViewById<LinearLayout>(R.id.llHealth_MainFragment)
        val llTransactions = view.findViewById<LinearLayout>(R.id.llTransactions_MainFragment)
        val llReports = view.findViewById<LinearLayout>(R.id.llReport_MainFragment)
        val llTables = view.findViewById<LinearLayout>(R.id.llTables_MainFragment)
        val llBudget = view.findViewById<LinearLayout>(R.id.llBudget_MainFragment)

/*
        val ivInventory = view.findViewById<ImageView>(R.id.ivInventory_MainFragment)
        val ivHealth = view.findViewById<ImageView>(R.id.ivHealth_MainFragment)
        val ivTransactions = view.findViewById<ImageView>(R.id.ivTransactions_MainFragment)
        val ivReports = view.findViewById<ImageView>(R.id.ivReport_MainFragment)
        val ivTables = view.findViewById<ImageView>(R.id.ivTables_MainFragment)
        val ivBudget = view.findViewById<ImageView>(R.id.ivBudget_MainFragment)


        val tvInventory = view.findViewById<TextView>(R.id.tvInventory_MainFragment)
        val tvHealth = view.findViewById<TextView>(R.id.tvHealth_MainFragment)
        val tvTransactions = view.findViewById<TextView>(R.id.tvTransactions_MainFragment)
        val tvReports = view.findViewById<TextView>(R.id.tvReport_MainFragment)
        val tvTables = view.findViewById<TextView>(R.id.tvTables_MainFragment)
        val tvBudget = view.findViewById<TextView>(R.id.tvBudget_MainFragment)
*/

        llInventory.setOnClickListener {
            startActivity(Intent(context, InventoryActivity::class.java))
        }

        llReports.setOnClickListener {
            startActivity(Intent(context, ReportActivity::class.java))
        }

        llHealth.setOnClickListener {
            startActivity(Intent(context, HealthActivity::class.java))
        }

        llTables.setOnClickListener {
            startActivity(Intent(context, TablesActivity::class.java))
        }

        llTransactions.setOnClickListener {
            startActivity(Intent(context, TransactionActivity::class.java))
        }
        llBudget.setOnClickListener {
            startActivity(Intent(context, ReminderActivity::class.java))
        }
        /*
        ivInventory.setOnClickListener {
            startActivity(Intent(context, InventoryActivity::class.java))
        }

        tvInventory.setOnClickListener {
            startActivity(Intent(context, InventoryActivity::class.java))
        }

        ivHealth.setOnClickListener {
            startActivity(Intent(context, HealthActivity::class.java))
        }

        tvHealth.setOnClickListener {
            startActivity(Intent(context, HealthActivity::class.java))
        }

        ivTransactions.setOnClickListener {
            startActivity(Intent(context, TransactionActivity::class.java))
        }
        tvTransactions.setOnClickListener {
            startActivity(Intent(context, TransactionActivity::class.java))
        }
        ivBudget.setOnClickListener {
            startActivity(Intent(context, ReminderActivity::class.java))
        }
        tvBudget.setOnClickListener {
            startActivity(Intent(context, ReminderActivity::class.java))
        }
        ivTables.setOnClickListener {
            startActivity(Intent(context, TablesActivity::class.java))
        }
        tvTables.setOnClickListener {
            startActivity(Intent(context, TablesActivity::class.java))
        }
        ivReports.setOnClickListener {
            startActivity(Intent(context, ReportActivity::class.java))
        }
        tvReports.setOnClickListener {
            startActivity(Intent(context, ReportActivity::class.java))
        }
*/
        return view
    }

}