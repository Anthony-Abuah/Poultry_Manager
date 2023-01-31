package com.example.pkompoultrymanager.tables.customer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CustomerRepository(private val customerDao: CustomerDao) {

    val displayAllCustomers: LiveData<List<Customer>> = customerDao.displayCustomer()

    val displayAllCustomerNames: LiveData<List<String>> = customerDao.displayAllCustomerNames()


    suspend fun addCustomer(customer: Customer){
        customerDao.addCustomer(customer)
    }

    suspend fun updateCustomerInfo(customerNumber: Int, customerName: String, customerContact: String, customerLocation: String, shortNotes: String){
        customerDao.updateCustomerInfo(customerNumber, customerName, customerContact, customerLocation, shortNotes)
    }

     fun deleteAll(){
        customerDao.deleteAll()
    }
    suspend fun delete(customer: Customer){
        customerDao.delete(customer)
    }
    fun deleteCustomer(customerId: Int){
        customerDao.deleteCustomer(customerId)
    }


}