package com.example.pkompoultrymanager.health

import android.os.Bundle
import java.sql.Date

interface HealthCommunicator {
    fun passMedication(
         MedicationId: String,
         MedicationName: String,
         MedicationDate: String,
         MedicationAdministrator: String,
         MedicationCost: String,
         MedicatedFlock: String,
         NumberOfMedicatedBirds: String,
         MedicatedDisease: String,
         ShortNotes: String
    )
    fun passVaccination(
        VaccinationId: String,
        VaccinationName: String,
        VaccinationDate: String,
        VaccinationAdministrator: String,
        VaccinationCost: String,
        VaccinatedFlock: String,
        NumberOfVaccinatedBirds: String,
        VaccinatedDisease: String,
        ShortNotes: String
    )
}