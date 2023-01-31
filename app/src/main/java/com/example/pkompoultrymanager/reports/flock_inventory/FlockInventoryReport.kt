package com.example.pkompoultrymanager.reports.flock_inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.reports.feed_inventory.fragments.GeneralFeedReport
import com.example.pkompoultrymanager.reports.flock_inventory.fragments.GeneralFlockReport

class FlockInventoryReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flock_inventory_report)

        val flockInventoryReportActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbFlockInventoryReportActivityToolbar)
        setSupportActionBar(flockInventoryReportActivityToolbar)

        val generalFlockReport = GeneralFlockReport()
        setCurrentFragment(generalFlockReport)


    }
    //App bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFlockInventoryReportActivity, fragment)
            commit()
        }
    }

}