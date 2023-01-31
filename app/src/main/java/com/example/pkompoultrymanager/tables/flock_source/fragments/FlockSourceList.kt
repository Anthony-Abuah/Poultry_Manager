package com.example.pkompoultrymanager.tables.flock_source.fragments


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
import com.example.pkompoultrymanager.tables.flock_source.FlockSource
import com.example.pkompoultrymanager.tables.flock_source.FlockSourceAdapter
import com.example.pkompoultrymanager.tables.flock_source.FlockSourceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FlockSourceList : Fragment() {
    private lateinit var mFlockSourceViewModel: FlockSourceViewModel
    private lateinit var communicator: TablesCommunicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flock_source_list, container, false)

        val flockSourceList = view.findViewById<RecyclerView>(R.id.rvFlockSourceList)
        val adapter = FlockSourceAdapter()
        flockSourceList.adapter = adapter
        flockSourceList.layoutManager = LinearLayoutManager(requireContext())


        mFlockSourceViewModel = ViewModelProvider(this)[FlockSourceViewModel::class.java]
        mFlockSourceViewModel.displayAllFlockSources.observe(
            viewLifecycleOwner
        ) { flockSource -> adapter.setData(flockSource) }


        adapter.setOnItemClickListener(object : FlockSourceAdapter.OnItemClickListener {
            override fun onDeleteFlockSourceClickListener(flockSource: FlockSource, position: Int) {
                val popupMenu = PopupMenu(requireContext(), flockSourceList[position].findViewById(R.id.ivUpdateDeleteMenu_PersonLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this flockSource?")

                                setNegativeButton("No") { dialog, which ->
                                    flockSourceList.adapter = adapter
                                    flockSourceList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "FlockSource not removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mFlockSourceViewModel.delete(flockSource)
                                    Toast.makeText(
                                        requireContext(), "FlockSource removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                            val flockSourceName = flockSource.FlockSourceName
                            val flockSourceContact = flockSource.FlockSourceContact
                            val flockSourceLocation = flockSource.FlockSourceLocation
                            val shortNotes = flockSource.shortNotes
                            val flockSourceId = flockSource.FlockSourceId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passFlockSource(
                                flockSourceId,
                                flockSourceName,
                                flockSourceContact,
                                flockSourceLocation,
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

            override fun onItemClickListener(flockSource: FlockSource) {
                val flockSourceName = flockSource.FlockSourceName
                val flockSourceContact = flockSource.FlockSourceContact
                val flockSourceLocation = flockSource.FlockSourceLocation
                val shortNotes = flockSource.shortNotes
                val flockSourceId = flockSource.FlockSourceId.toString()
                communicator = activity as TablesCommunicator
                communicator.passFlockSource(
                    flockSourceId,
                    flockSourceName,
                    flockSourceContact,
                    flockSourceLocation,
                    shortNotes
                )
            }
        })


        val addFlockSource = view.findViewById<FloatingActionButton>(R.id.fabAddFlockSource)
        addFlockSource.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddFlockSource()
            ).commit()
        }

        return view
    }
}