package com.example.pkompoultrymanager.tables.lender_investor.fragments


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
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestor
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestorAdapter
import com.example.pkompoultrymanager.tables.lender_investor.LenderInvestorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LenderInvestorList : Fragment() {
    private lateinit var mLenderInvestorViewModel: LenderInvestorViewModel
    private lateinit var communicator: TablesCommunicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lender_investor_list, container, false)

        val lenderInvestorList = view.findViewById<RecyclerView>(R.id.rvLenderInvestorList)
        val adapter = LenderInvestorAdapter()
        lenderInvestorList.adapter = adapter
        lenderInvestorList.layoutManager = LinearLayoutManager(requireContext())


        mLenderInvestorViewModel = ViewModelProvider(this)[LenderInvestorViewModel::class.java]
        mLenderInvestorViewModel.displayAllLendersOrInvestors.observe(
            viewLifecycleOwner
        ) { lenderInvestor -> adapter.setData(lenderInvestor) }


        adapter.setOnItemClickListener(object : LenderInvestorAdapter.OnItemClickListener {
            override fun onDeleteLenderInvestorClickListener(lenderInvestor: LenderInvestor, position: Int) {
                val popupMenu = PopupMenu(requireContext(), lenderInvestorList[position].findViewById(R.id.ivUpdateDeleteMenu_PersonLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder) {
                                setTitle("Are you sure you want to permanently delete this lenderInvestor?")

                                setNegativeButton("No") { dialog, which ->
                                    lenderInvestorList.adapter = adapter
                                    lenderInvestorList.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    Toast.makeText(
                                        requireContext(), "LenderInvestor not removed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setPositiveButton("Yes") { dialog, where ->
                                    mLenderInvestorViewModel.delete(lenderInvestor)
                                    Toast.makeText(
                                        requireContext(), "LenderInvestor removed",
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
                            val lenderInvestorName = lenderInvestor.LenderInvestorName
                            val lenderInvestorContact = lenderInvestor.LenderInvestorContact
                            val lenderInvestorLocation = lenderInvestor.LenderInvestorLocation
                            val shortNotes = lenderInvestor.shortNotes
                            val lenderInvestorId = lenderInvestor.LenderInvestorId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passLenderInvestor(
                                lenderInvestorId,
                                lenderInvestorName,
                                lenderInvestorContact,
                                lenderInvestorLocation,
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

            override fun onItemClickListener(lenderInvestor: LenderInvestor) {
                val lenderInvestorName = lenderInvestor.LenderInvestorName
                val lenderInvestorContact = lenderInvestor.LenderInvestorContact
                val lenderInvestorLocation = lenderInvestor.LenderInvestorLocation
                val shortNotes = lenderInvestor.shortNotes
                val lenderInvestorId = lenderInvestor.LenderInvestorId.toString()
                communicator = activity as TablesCommunicator
                communicator.passLenderInvestor(
                    lenderInvestorId,
                    lenderInvestorName,
                    lenderInvestorContact,
                    lenderInvestorLocation,
                    shortNotes
                )
            }
        })


        val addLenderInvestor = view.findViewById<FloatingActionButton>(R.id.fabAddLenderInvestor)
        addLenderInvestor.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddLenderInvestor()
            ).addToBackStack(null).commit()
        }

        return view
    }
}