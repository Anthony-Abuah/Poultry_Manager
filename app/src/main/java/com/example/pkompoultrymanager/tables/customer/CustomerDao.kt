package com.example.pkompoultrymanager.tables.customer

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomer(customer: Customer)

    @Query("SELECT * FROM Customer ORDER BY CustomerId ASC")
    fun displayCustomer(): LiveData<List<Customer>>

    @Query("SELECT CustomerName FROM Customer ORDER BY CustomerId ASC")
    fun displayAllCustomerNames(): LiveData<List<String>>

    @Query("UPDATE Customer SET CustomerName=:customerName, CustomerContact=:customerContact, CustomerLocation=:customerLocation, shortNotes=:shortNotes WHERE CustomerId LIKE :customerNumber")
    suspend fun updateCustomerInfo(customerNumber: Int, customerName: String, customerContact: String, customerLocation: String, shortNotes: String)

    @Query("DELETE FROM Customer WHERE CustomerId =:customerId")
    fun deleteCustomer(customerId: Int)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("DELETE FROM Customer")
    fun deleteAll()



}