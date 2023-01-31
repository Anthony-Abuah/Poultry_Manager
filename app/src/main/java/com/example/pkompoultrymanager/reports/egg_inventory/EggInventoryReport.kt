package com.example.pkompoultrymanager.reports.egg_inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.MainFragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.reports.egg_inventory.fragments.GeneralEggReport

class EggInventoryReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg_inventory_report)

        val eggInventoryReportActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbEggInventoryReportActivityToolbar)
        setSupportActionBar(eggInventoryReportActivityToolbar)

        val generalEggReport = GeneralEggReport()
        setCurrentFragment(generalEggReport)


    }
    //App bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flEggInventoryReportActivity, fragment)
            commit()
        }
    }

}