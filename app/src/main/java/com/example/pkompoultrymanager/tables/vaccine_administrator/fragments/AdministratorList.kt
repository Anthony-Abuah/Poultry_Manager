package com.example.pkompoultrymanager.tables.vaccine_administrator.fragments


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
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorAdapter
import com.example.pkompoultrymanager.tables.vaccine_administrator.VaccineAdministratorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdministratorList : Fragment() {
    private lateinit var mVaccineAdministratorViewModel: VaccineAdministratorViewModel
    private lateinit var communicator: TablesCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_administrator_list, container, false)

        val vaccineAdministratorList = view.findViewById<RecyclerView>(R.id.rvAdministratorList)
        val adapter = VaccineAdministratorAdapter()
        vaccineAdministratorList.adapter = adapter
        vaccineAdministratorList.layoutManager= LinearLayoutManager(requireContext())

        mVaccineAdministratorViewModel = ViewModelProvider(this)[VaccineAdministratorViewModel::class.java]
        mVaccineAdministratorViewModel.displayAllAdministrators.observe(viewLifecycleOwner
        ) { vaccineAdministrator -> adapter.setData(vaccineAdministrator) }


        adapter.setOnItemClickListener(object : VaccineAdministratorAdapter.OnItemClickListener{
            override fun onDeleteVaccineAdministratorClickListener(vaccineAdministrator: VaccineAdministrator, position: Int) {
                val popupMenu = PopupMenu(requireContext(),vaccineAdministratorList[position].findViewById(R.id.ivUpdateDeleteMenu_PersonLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder){
                                setTitle("Are you sure you want to permanently delete this vaccineAdministrator?")

                                setNegativeButton("No"){
                                        dialog,  which -> vaccineAdministratorList.adapter = adapter
                                    vaccineAdministratorList.layoutManager= LinearLayoutManager(requireContext())
                                    Toast.makeText(requireContext(), "Administrator not removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setPositiveButton("Yes"){
                                        dialog, where ->
                                    mVaccineAdministratorViewModel.delete(vaccineAdministrator)
                                    Toast.makeText(requireContext(), "Administrator removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val vaccineAdministratorName = vaccineAdministrator.AdministratorName
                            val vaccineAdministratorContact = vaccineAdministrator.AdministratorContact
                            val vaccineAdministratorLocation = vaccineAdministrator.AdministratorLocation
                            val shortNotes = vaccineAdministrator.shortNotes
                            val vaccineAdministratorId = vaccineAdministrator.AdministratorId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passVaccineAdministrator(vaccineAdministratorId, vaccineAdministratorName, vaccineAdministratorContact, vaccineAdministratorLocation,shortNotes)
                            true
                        }
                        else-> true
                    }
                }
                popupMenu.show()
                true

            }

            override fun onItemClickListener(vaccineAdministrator: VaccineAdministrator) {
                val vaccineAdministratorName = vaccineAdministrator.AdministratorName
                val vaccineAdministratorContact = vaccineAdministrator.AdministratorContact
                val vaccineAdministratorLocation = vaccineAdministrator.AdministratorLocation
                val shortNotes = vaccineAdministrator.shortNotes
                val vaccineAdministratorId = vaccineAdministrator.AdministratorId.toString()
                communicator = activity as TablesCommunicator
                communicator.passVaccineAdministrator(vaccineAdministratorId, vaccineAdministratorName, vaccineAdministratorContact, vaccineAdministratorLocation,shortNotes)
            }
        })



        val addAdministrator = view.findViewById<FloatingActionButton>(R.id.fabAddAdministrator)
        addAdministrator.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddAdministrator()
            ).addToBackStack(null).commit()
        }

        return view
    }
}