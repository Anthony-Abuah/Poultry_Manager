package com.example.pkompoultrymanager.tables.customer.fragments


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
import com.example.pkompoultrymanager.inventory.feed.addition.fragments.FeedInventoryAdditionForm
import com.example.pkompoultrymanager.tables.TablesCommunicator
import com.example.pkompoultrymanager.tables.customer.Customer
import com.example.pkompoultrymanager.tables.customer.CustomerAdapter
import com.example.pkompoultrymanager.tables.customer.CustomerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomerList : Fragment() {
    private lateinit var mCustomerViewModel: CustomerViewModel
    private lateinit var communicator: TablesCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customer_list, container, false)

        val customerList = view.findViewById<RecyclerView>(R.id.rvCustomerList)
        val adapter = CustomerAdapter()
        customerList.adapter = adapter
        customerList.layoutManager= LinearLayoutManager(requireContext())

        mCustomerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]
        mCustomerViewModel.displayAllCustomers.observe(viewLifecycleOwner
        ) { customer -> adapter.setData(customer) }


        adapter.setOnItemClickListener(object : CustomerAdapter.OnItemClickListener{
            override fun onDeleteCustomerClickListener(customer: Customer) {
                val popupMenu = PopupMenu(requireContext(),view,Gravity.RELATIVE_LAYOUT_DIRECTION)
                popupMenu.inflate(R.menu.update_delete_menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.miDelete -> {
                            Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                            val builder = AlertDialog.Builder(context,Gravity.END)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.delete_prompt_layout, null)
                            with(builder){
                                setTitle("Are you sure you want to permanently delete this customer?")

                                setNegativeButton("No"){
                                        dialog,  which -> customerList.adapter = adapter
                                    customerList.layoutManager= LinearLayoutManager(requireContext())
                                    Toast.makeText(requireContext(), "Customer not removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setPositiveButton("Yes"){
                                        dialog, where ->
                                    mCustomerViewModel.delete(customer)
                                    Toast.makeText(requireContext(), "Customer removed",
                                        Toast.LENGTH_LONG).show()
                                }
                                setView(dialogLayout)
                                show()
                            }

                            true
                        }
                        R.id.miUpdate -> {
                            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show()
                            val customerName = customer.CustomerName
                            val customerContact = customer.CustomerContact
                            val customerLocation = customer.CustomerLocation
                            val shortNotes = customer.shortNotes
                            val customerId = customer.CustomerId.toString()
                            communicator = activity as TablesCommunicator
                            communicator.passCustomer(customerId, customerName, customerContact, customerLocation,shortNotes)
                            true
                        }
                        else-> true
                    }
                }
                popupMenu.show()
                true

            }
            override fun onItemClickListener(customer: Customer) {
                val customerName = customer.CustomerName
                val customerContact = customer.CustomerContact
                val customerLocation = customer.CustomerLocation
                val shortNotes = customer.shortNotes
                val customerId = customer.CustomerId.toString()
                communicator = activity as TablesCommunicator
                communicator.passCustomer(customerId, customerName, customerContact, customerLocation,shortNotes)
            }
        })




        val addCustomer = view.findViewById<FloatingActionButton>(R.id.fabAddCustomer)
        addCustomer.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flTablesActivity,
                AddCustomer()
            ).addToBackStack(null).commit()
        }



        return view
    }
}