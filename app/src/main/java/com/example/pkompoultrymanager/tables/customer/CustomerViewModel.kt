package com.example.pkompoultrymanager.tables.customer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkompoultrymanager.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) : AndroidViewModel(application) {


    val displayAllCustomers: LiveData<List<Customer>>
    val displayAllCustomerNames: LiveData<List<String>>
    private val repository: CustomerRepository

    init {
        val customerDao = AppDatabase.getDatabase(application).customerDao()
        repository = CustomerRepository(customerDao)
        displayAllCustomers = repository.displayAllCustomers
        displayAllCustomerNames = repository.displayAllCustomerNames
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCustomer(customer)
        }
    }

    fun delete(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(customer)
        }
    }

    fun updateCustomerInfo(
        customerNumber: Int, customerName: String, customerContact: String, customerLocation: String, shortNotes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCustomerInfo(
                customerNumber, customerName, customerContact, customerLocation, shortNotes
            )
        }
    }

    fun deleteCustomer(customerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCustomer(customerId)

        }
    }

}
