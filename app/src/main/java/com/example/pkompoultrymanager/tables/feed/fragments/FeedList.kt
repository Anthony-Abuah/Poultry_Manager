package com.example.pkompoultrymanager.tables.feed.fragments


import android.app.AlertDialog
import android.icu.text.Transliterator
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.tables.TablesCommunicator
import com.example.pkompoultrymanager.tables.feed.Feed
import com.example.pkompoultrymanager.tables.feed.FeedAdapter
import com.example.pkompoultrymanager.tables.feed.FeedViewModel
import com.example.pkompoultrymanager.tables.feed.fragments.AddFeed
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedList : Fragment() {
    private lateinit var mFeedViewModel: FeedViewModel
    private lateinit var communicator: TablesCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_list, container, false)

        val feedList = view.findViewById<RecyclerView>(R.id.rvFeedList)
        val adapter = FeedAdapter()
        feedList.adapter = adapter
        feedList.layoutManager= LinearLayoutManager(requireContext())

        mFeedViewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        mFeedViewModel.displayAllFeeds.observe(viewLifecycleOwner
        ) { feed -> adapter.setData(feed) }


        adapter.setOnItemClickListener(object : FeedAdapter.OnItemClickListener{
            override fun onDeleteFeedClickListener(feed: Feed, position: Int) {
                val popupMenu = PopupMenu(requireContext(),feedList[position].findViewById(R.id.ivUpdateDeleteMenu_FeedLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder){
                                setTitle("Are you sure you want to permanently delete this feed?")

                                setNegativeButton("No"){
                                        dialog,  which -> feedList.adapter = adapter
                                    feedList.layoutManager= LinearLayoutManager(requireContext())
                                    Toast.makeText(requireContext(), "Feed not removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setPositiveButton("Yes"){
                                        dialog, where ->
                                    mFeedViewModel.delete(feed)
                                    Toast.makeText(requireContext(), "Feed removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val feedName = feed.FeedName
                            val feedType = feed.FeedType
                            val feedBrand = feed.FeedBrand
                            val feedSource = feed.FeedSource
                            val shortNotes = feed.ShortNotes
                            val feedId = feed.FeedId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passFeed(feedId, feedName, feedType, feedBrand, feedSource, shortNotes)
                            true
                        }
                        else-> true
                    }
                }
                popupMenu.show()
                true

            }

            override fun onItemClickListener(feed: Feed) {
                val feedName = feed.FeedName
                val feedType = feed.FeedType
                val feedBrand = feed.FeedBrand
                val feedSource = feed.FeedSource
                val shortNotes = feed.ShortNotes
                val feedId = feed.FeedId.toString()
                communicator = activity as TablesCommunicator
                communicator.passFeed(feedId, feedName, feedType, feedBrand, feedSource, shortNotes)
            }
        })



        val addFeed = view.findViewById<FloatingActionButton>(R.id.fabAddFeed)
        addFeed.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddFeed()
            ).addToBackStack(null).commit()
        }


        return view
    }
}