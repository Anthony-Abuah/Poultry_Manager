package com.example.pkompoultrymanager.health.vaccination

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date


@Dao
interface VaccinationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addVaccination(vaccination: Vaccination)

    @Query("SELECT * FROM Vaccination ORDER BY vaccinationId DESC " )
    fun displayAllVaccination(): LiveData<List<Vaccination>>

    @Query("SELECT * FROM Vaccination WHERE VaccinationDate BETWEEN :firstDate AND :lastDate ORDER BY vaccinationId DESC LIMIT :number" )
    fun displayVaccination(number: Int, firstDate: Date, lastDate: Date): LiveData<List<Vaccination>>

    @Query("UPDATE Vaccination SET vaccinationName=:vaccinationName, vaccinationDate=:vaccinationDate, vaccinatedDisease=:vaccinatedDisease, vaccinationCost=:vaccinationCost, vaccinatedFlock=:vaccinatedFlock, numberOfVaccinatedBirds=:numberOfVaccinatedBirds, vaccinationAdministrator=:vaccinationAdministrator, shortNotes=:shortNotes WHERE vaccinationId LIKE :vaccinationID")
    suspend fun updateVaccination( vaccinationID: Int, vaccinationName: String, vaccinationDate: Date, vaccinatedDisease: String, vaccinationCost:Double, vaccinatedFlock: String, numberOfVaccinatedBirds:Int, vaccinationAdministrator:String, shortNotes:String)

    @Delete
    suspend fun delete(vaccination: Vaccination)

    @Query("DELETE FROM Vaccination WHERE vaccinationId LIKE :vaccinationID")
    fun deleteVaccination(vaccinationID: Int)

    @Query("DELETE FROM Vaccination")
    fun deleteAll()


    @Query("SELECT * FROM Vaccination WHERE vaccinatedFlock LIKE :vaccinatedFlock AND vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun displayFlockMedicated(vaccinatedFlock:String, firstDate: Date, lastDate: Date): LiveData<List<Vaccination>>


    @Query("SELECT * FROM Vaccination WHERE vaccinationName LIKE :vaccinationName AND vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun displayVaccinationApplied(vaccinationName: String, firstDate: Date, lastDate: Date): LiveData<List<Vaccination>>


    @Query("SELECT * FROM Vaccination WHERE vaccinationCost >= :vaccinationCost AND vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun displayVaccinationInfoByCost(vaccinationCost: Double, firstDate: Date, lastDate: Date): LiveData<List<Vaccination>>


    @Query("SELECT SUM(vaccinationCost) FROM Vaccination WHERE vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun costOfVaccinationPerDuration(firstDate: Date, lastDate: Date): LiveData<Double>

    @Query("SELECT SUM(vaccinationCost) FROM Vaccination")
    fun costOfAllVaccinations(): LiveData<Double>

    @Query("SELECT SUM(numberOfVaccinatedBirds) FROM Vaccination WHERE vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun numberOfVaccinatedBirdsPerDuration(firstDate: Date, lastDate: Date): LiveData<Int>

    @Query("SELECT SUM(numberOfVaccinatedBirds) FROM Vaccination")
    fun numberOfAllVaccinatedBirds(): LiveData<Int>

    @Query("SELECT COUNT(vaccinationCost) FROM Vaccination WHERE vaccinationCost >= :vaccinationCost AND vaccinationDate BETWEEN :firstDate AND :lastDate")
    fun numberOfVaccinationByCost(vaccinationCost: Double, firstDate: Date, lastDate: Date): LiveData<Int>

}