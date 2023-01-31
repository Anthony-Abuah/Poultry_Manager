package com.example.pkompoultrymanager.tables.feed.fragments


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.feed.FeedViewModel

class UpdateFeed : Fragment() {

    private lateinit var mFeedViewModel: FeedViewModel

    var feedId: String? =""
    var feedName: String? =""
    var feedType: String? =""
    var feedBrand: String? =""
    var feedSource: String? =""
    var shortNotes: String? =""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_feed, container, false)



        mFeedViewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        feedId = arguments?.getString("id")
        feedName = arguments?.getString("name")
        feedType = arguments?.getString("type")
        feedSource = arguments?.getString("source")
        feedBrand = arguments?.getString("brand")
        shortNotes = arguments?.getString("shortNotes")

        val name= view.findViewById<EditText>(R.id.etFeedName_UpdateFeedForm)
        val type= view.findViewById<EditText>(R.id.etFeedType_UpdateFeedForm)
        val brand = view.findViewById<EditText>(R.id.etFeedBrand_UpdateFeedForm)
        val source= view.findViewById<AutoCompleteTextView>(R.id.actvFeedSource_UpdateFeedForm)
        val shortNote= view.findViewById<EditText>(R.id.etShortNotes_UpdateFeedForm)
        val id = view.findViewById<TextView>(R.id.tvFeedId_UpdateFeedForm)

        id.text = feedId
        name.setText(feedName)
        type.setText(feedType)
        brand.setText(feedBrand)
        source.setText(feedSource)
        shortNote.setText(shortNotes)

        val update = view.findViewById<Button>(R.id.btnUpdate_UpdateFeedForm)
        update.setOnClickListener {
            updateDataInDatabase()
        }



        return view
    }

    private fun updateDataInDatabase() {
        val name= view?.findViewById<EditText>(R.id.etFeedName_UpdateFeedForm)?.text.toString()
        val type= view?.findViewById<EditText>(R.id.etFeedType_UpdateFeedForm)?.text.toString()
        val brand = view?.findViewById<EditText>(R.id.etFeedBrand_UpdateFeedForm)?.text.toString()
        val source= view?.findViewById<AutoCompleteTextView>(R.id.actvFeedSource_UpdateFeedForm)?.text.toString()
        val shortNotes= view?.findViewById<EditText>(R.id.etShortNotes_UpdateFeedForm)?.text.toString()
        val id = view?.findViewById<TextView>(R.id.tvFeedId_UpdateFeedForm)?.text.toString()

        if (checkIfEmpty(name)) {
            view?.findViewById<TextView>(R.id.tvFeedName_UpdateFeedForm)?.setTextColor(resources.getColor(R.color.red))
            Toast.makeText(requireContext(), "Please Add Flock Source Name", Toast.LENGTH_LONG).show()
        } else {
            mFeedViewModel.updateFeed(id.toInt(),name,type,brand,source,shortNotes)

            view?.findViewById<EditText>(R.id.etFeedName_UpdateFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.etFeedType_UpdateFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.etFeedBrand_UpdateFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.actvFeedSource_UpdateFeedForm)?.text?.clear()
            view?.findViewById<EditText>(R.id.etShortNotes_UpdateFeedForm)?.text?.clear()

            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()


            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                FeedList()
            ).addToBackStack(null).commit()
        }
    }
    private fun checkIfEmpty(feedName: String): Boolean {
        return (TextUtils.isEmpty(feedName))
    }
}