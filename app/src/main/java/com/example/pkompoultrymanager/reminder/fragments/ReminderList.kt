package com.example.pkompoultrymanager.reminder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.eggs.addition.fragments.EggsAdditionForm
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReminderList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reminder_list, container, false)


        val addReminder = view.findViewById<FloatingActionButton>(R.id.fabAddReminder)
        addReminder.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReminderActivity,
                AddReminder()
            ).addToBackStack(null).commit()
        }

        return view
    }

}