package com.example.pkompoultrymanager.reports.feed_inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.reports.egg_inventory.fragments.GeneralEggReport
import com.example.pkompoultrymanager.reports.feed_inventory.fragments.GeneralFeedReport

class FeedInventoryReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_inventory_report)

        val feedInventoryReportActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbFeedInventoryReportActivityToolbar)
        setSupportActionBar(feedInventoryReportActivityToolbar)

        val generalFeedReport = GeneralFeedReport()
        setCurrentFragment(generalFeedReport)


    }
    //App bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFeedInventoryReportActivity, fragment)
            commit()
        }
    }

}