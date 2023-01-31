package com.example.pkompoultrymanager.tables

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.customer.fragments.CustomerList
import com.example.pkompoultrymanager.tables.egg_type.fragments.EggTypeList
import com.example.pkompoultrymanager.tables.feed.fragments.FeedList
import com.example.pkompoultrymanager.tables.feed_source.fragments.FeedSourceList
import com.example.pkompoultrymanager.tables.flock_source.fragments.FlockSourceList
import com.example.pkompoultrymanager.tables.lender_investor.fragments.LenderInvestorList
import com.example.pkompoultrymanager.tables.vaccine_administrator.fragments.AdministratorList

class TablesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tables, container, false)

        val clCustomer = view.findViewById<ConstraintLayout>(R.id.clCustomer_TablesFragment)
        val clFeed = view.findViewById<ConstraintLayout>(R.id.clFeed_TablesFragment)
        val clFeedSource = view.findViewById<ConstraintLayout>(R.id.clFeedSource_TablesFragment)
        val clFlockSource = view.findViewById<ConstraintLayout>(R.id.clFlockSource_TablesFragment)
        val clLenderOrInvestor = view.findViewById<ConstraintLayout>(R.id.clLenderInvestor_TablesFragment)
        val clEggType = view.findViewById<ConstraintLayout>(R.id.clEggType_TablesFragment)
        val clVaccineAdministrator = view.findViewById<ConstraintLayout>(R.id.clVaccineAdministrator_TablesFragment)

        clCustomer.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                CustomerList()
            ).addToBackStack(null).commit()
        }
        clFeed.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                FeedList()
            ).addToBackStack(null).commit()
        }
        clFeedSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                FeedSourceList()
            ).addToBackStack(null).commit()
        }
        clFlockSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                FlockSourceList()
            ).addToBackStack(null).commit()
        }
        clLenderOrInvestor.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                LenderInvestorList()
            ).addToBackStack(null).commit()
        }
        clEggType.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                EggTypeList()
            ).addToBackStack(null).commit()
        }
        clVaccineAdministrator.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AdministratorList()
            ).addToBackStack(null).commit()
        }


        return view
    }

}