package com.example.pkompoultrymanager.tables

import android.os.Bundle
import java.sql.Date

interface TablesCommunicator {
    fun passCustomer(Id: String, Name: String, Contact: String, Location: String, ShortNotes: String)
    fun passFeedSource(Id: String, Name: String, Contact: String, Location: String, ShortNotes: String)
    fun passFlockSource(Id: String, Name: String, Contact: String, Location: String, ShortNotes: String)
    fun passLenderInvestor(Id: String, Name: String, Contact: String, Location: String, ShortNotes: String)
    fun passEggType(Id: String, Name: String, Description:String)
    fun passVaccineAdministrator(Id: String, Name: String, Contact: String, Location: String, ShortNotes: String)
    fun passFeed(Id:String, Name: String, Type: String, Brand: String, Source: String, ShortNotes: String)

}