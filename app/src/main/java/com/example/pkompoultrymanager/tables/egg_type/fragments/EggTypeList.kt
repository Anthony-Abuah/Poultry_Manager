package com.example.pkompoultrymanager.tables.egg_type.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.flock.addition.fragments.FlockAdditionForm
import com.example.pkompoultrymanager.tables.TablesCommunicator
import com.example.pkompoultrymanager.tables.egg_type.EggType
import com.example.pkompoultrymanager.tables.egg_type.EggTypeAdapter
import com.example.pkompoultrymanager.tables.egg_type.EggTypeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EggTypeList : Fragment() {
    private lateinit var mEggTypeViewModel: EggTypeViewModel
    private lateinit var communicator: TablesCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_egg_type_list, container, false)

        val eggTypeList = view.findViewById<RecyclerView>(R.id.rvEggTypeList)
        val adapter = EggTypeAdapter()
        eggTypeList.adapter = adapter
        eggTypeList.layoutManager= LinearLayoutManager(requireContext())

        mEggTypeViewModel = ViewModelProvider(this)[EggTypeViewModel::class.java]
        mEggTypeViewModel.displayAllEggTypes.observe(viewLifecycleOwner
        ) { eggType -> adapter.setData(eggType) }


        adapter.setOnItemClickListener(object : EggTypeAdapter.OnItemClickListener{
            override fun onDeleteEggTypeClickListener(eggType: EggType) {
                val popupMenu = PopupMenu(requireContext(),view.findViewById(R.id.ivUpdateDeleteMenu_EggTypeLayout))
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(context, Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder){
                                setTitle("Are you sure you want to permanently delete this egg Type?")

                                setNegativeButton("No"){
                                        dialog,  which -> eggTypeList.adapter = adapter
                                    eggTypeList.layoutManager= LinearLayoutManager(requireContext())
                                    Toast.makeText(requireContext(), "Egg Type not removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setPositiveButton("Yes"){
                                        dialog, where ->
                                    mEggTypeViewModel.delete(eggType)
                                    Toast.makeText(requireContext(), "Egg Type removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val eggTypeName = eggType.EggTypeName
                            val description = eggType.EggTypeDescription
                            val eggTypeId = eggType.EggTypeId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passEggType(eggTypeId,eggTypeName, description)
                            true
                        }
                        else-> true
                    }
                }
                popupMenu.show()
                true

            }

            override fun onItemClickListener(eggType: EggType) {
                val eggTypeName = eggType.EggTypeName
                val description = eggType.EggTypeDescription
                val eggTypeId = eggType.EggTypeId.toString()
                communicator = activity as TablesCommunicator
                communicator.passEggType(eggTypeId,eggTypeName, description)
            }
        })



        val addEggType = view.findViewById<FloatingActionButton>(R.id.fabAddEggType)
        addEggType.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddEggType()
            ).addToBackStack(null).commit()
        }


        return view
    }
}