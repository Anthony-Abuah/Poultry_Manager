package com.example.pkompoultrymanager.inventory

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
import com.example.pkompoultrymanager.inventory.eggs.EggsActivity
import com.example.pkompoultrymanager.inventory.feed.FeedActivity
import com.example.pkompoultrymanager.inventory.flock.FlockActivity

class InventoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        val eggInventoryActivityImage = view.findViewById<ConstraintLayout>(R.id.clEggInventory_InventoryFragment)
        val feedInventoryActivityImage = view.findViewById<ConstraintLayout>(R.id.clFeedInventory_InventoryFragment)
        val flockInventoryActivityImage = view.findViewById<ConstraintLayout>(R.id.clFlockInventory_InventoryFragment)


        eggInventoryActivityImage.setOnClickListener {
            startActivity(Intent(context, EggsActivity::class.java))
        }
        feedInventoryActivityImage.setOnClickListener {
            startActivity(Intent(context, FeedActivity::class.java))
        }
        flockInventoryActivityImage.setOnClickListener {
            startActivity(Intent(context, FlockActivity::class.java))
        }

        return view
    }

}