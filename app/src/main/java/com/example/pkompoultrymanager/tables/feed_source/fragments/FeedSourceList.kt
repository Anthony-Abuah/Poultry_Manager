package com.example.pkompoultrymanager.tables.feed_source.fragments


import android.app.AlertDialog
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
import com.example.pkompoultrymanager.tables.feed_source.FeedSource
import com.example.pkompoultrymanager.tables.feed_source.FeedSourceAdapter
import com.example.pkompoultrymanager.tables.feed_source.FeedSourceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedSourceList : Fragment() {
    private lateinit var mFeedSourceViewModel: FeedSourceViewModel
    private lateinit var communicator: TablesCommunicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_source_list, container, false)

        val feedSourceList = view.findViewById<RecyclerView>(R.id.rvFeedSourceList)
        val adapter = FeedSourceAdapter()
        feedSourceList.adapter = adapter
        feedSourceList.layoutManager = LinearLayoutManager(requireContext())


        mFeedSourceViewModel = ViewModelProvider(this)[FeedSourceViewModel::class.java]
        mFeedSourceViewModel.displayAllFeedSources.observe(
            viewLifecycleOwner
        ) { feedSource -> adapter.setData(feedSource) }


        adapter.setOnItemClickListener(object : FeedSourceAdapter.OnItemClickListener {
            override fun onDeleteFeedSourceClickListener(feedSource: FeedSource, position: Int) {
                val popupMenu = PopupMenu(requireContext(), feedSourceList[position].findViewById(R.id.ivUpdateDeleteMenu_PersonLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this feedSource?")

                                setNegativeButton("No") { dialog, which ->
                                    feedSourceList.adapter = adapter
                                    feedSourceList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "FeedSource not removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mFeedSourceViewModel.delete(feedSource)
                                    Toast.makeText(
                                        requireContext(), "FeedSource removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val feedSourceName = feedSource.FeedSourceName
                            val feedSourceContact = feedSource.FeedSourceContact
                            val feedSourceLocation = feedSource.FeedSourceLocation
                            val shortNotes = feedSource.shortNotes
                            val feedSourceId = feedSource.FeedSourceId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passFeedSource(
                                feedSourceId,
                                feedSourceName,
                                feedSourceContact,
                                feedSourceLocation,
                                shortNotes
                            )
                            true
                        }
                        else -> true
                    }
                }
                popupMenu.show()
                true

            }

            override fun onItemClickListener(feedSource: FeedSource) {
                val feedSourceName = feedSource.FeedSourceName
                val feedSourceContact = feedSource.FeedSourceContact
                val feedSourceLocation = feedSource.FeedSourceLocation
                val shortNotes = feedSource.shortNotes
                val feedSourceId = feedSource.FeedSourceId.toString()
                communicator = activity as TablesCommunicator
                communicator.passFeedSource(
                    feedSourceId,
                    feedSourceName,
                    feedSourceContact,
                    feedSourceLocation,
                    shortNotes
                )
            }
        })


        val addFeedSource = view.findViewById<FloatingActionButton>(R.id.fabAddFeedSource)
        addFeedSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddFeedSource()
            ).addToBackStack(null).commit()
        }


        return view
    }
}