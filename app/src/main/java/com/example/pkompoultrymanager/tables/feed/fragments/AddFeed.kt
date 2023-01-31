package com.example.pkompoultrymanager.tables.feed.fragments


import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.feed.Feed
import com.example.pkompoultrymanager.tables.feed.FeedViewModel
import com.example.pkompoultrymanager.tables.feed.fragments.FeedList

class AddFeed : Fragment() {
    private lateinit var mFeedViewModel: FeedViewModel
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_feed, container, false)

        mFeedViewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        val save = view.findViewById<Button>(R.id.btnSave_AddFeedForm)
        save.setOnClickListener {
            insertDataToDatabase()
        }


        return view
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun insertDataToDatabase() {
        val feedName =
            view?.findViewById<EditText>(R.id.etFeedName_AddFeedForm)?.text.toString()
        val feedType =
            view?.findViewById<EditText>(R.id.etFeedType_AddFeedForm)?.text.toString()
        val feedBrand =
            view?.findViewById<EditText>(R.id.etFeedBrand_AddFeedForm)?.text.toString()
         val feedSource =
            view?.findViewById<AutoCompleteTextView>(R.id.actvFeedSource_AddFeedForm)?.text.toString()
        val shortNotes =
            view?.findViewById<EditText>(R.id.etShortNotes_AddFeedForm)?.text.toString()

        if (inputCheck(feedName)) {
            view?.findViewById<TextView>(R.id.tvFeedName_AddFeedForm)?.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add Feed Name", Toast.LENGTH_LONG).show()
        } else if (inputCheck(feedType)) {
            view?.findViewById<TextView>(R.id.tvFeedName_AddFeedForm)?.setTextColor(resources.getColor(R.color.black))
            view?.findViewById<TextView>(R.id.tvFeedType_AddFeedForm)?.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add Feed Type", Toast.LENGTH_LONG).show()
        } else {
            val feed = Feed(0, feedName, feedType, feedBrand, feedSource, shortNotes)
            mFeedViewModel.addFeed(feed)

            view?.findViewById<EditText>(R.id.etFeedName_AddFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.etFeedBrand_AddFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.etFeedType_AddFeedForm)?.text?.clear()
            view?.findViewById<AutoCompleteTextView>(R.id.actvFeedSource_AddFeedForm)?.text?.clear()

            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun inputCheck(feedName: String): Boolean {
        return (TextUtils.isEmpty(feedName))
    }

}