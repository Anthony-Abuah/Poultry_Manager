package com.example.pkompoultrymanager.tables.vaccine_administrator

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkompoultrymanager.tables.customer.VaccineAdministrator


@Dao
interface VaccineAdministratorDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addVaccineAdministrator(vaccineAdministrator: VaccineAdministrator)

    @Query("SELECT * FROM VaccineAdministrator ORDER BY AdministratorId ASC")
    fun displayVaccineAdministrator(): LiveData<List<VaccineAdministrator>>

    @Query("UPDATE VaccineAdministrator SET AdministratorName=:vaccineAdministratorName, AdministratorContact=:vaccineAdministratorContact, AdministratorLocation=:vaccineAdministratorLocation, shortNotes=:shortNotes WHERE AdministratorId LIKE :vaccineAdministratorId")
    suspend fun updateVaccineAdministrator(vaccineAdministratorId:Int, vaccineAdministratorName: String, vaccineAdministratorContact: String, vaccineAdministratorLocation: String, shortNotes: String)

    @Query("DELETE FROM VaccineAdministrator WHERE AdministratorId =:vaccineAdministratorId")
    fun deleteVaccineAdministrator(vaccineAdministratorId: Int)

    @Delete
    suspend fun delete(vaccineAdministrator: VaccineAdministrator)

    @Query("DELETE FROM VaccineAdministrator")
    fun deleteAll()



}