package com.example.pkompoultrymanager.reminder.fragments

import android.app.*
import android.content.Context.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.example.pkompoultrymanager.R
import com.example.pkompoultrymanager.inventory.flock.addition.fragments.FlockAdditionForm
import com.example.pkompoultrymanager.reminder.AlarmReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class AddReminder : Fragment() {

    private lateinit var tvDatePicker: TextView
    private lateinit var tvTimePicker: TextView
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_reminder, container, false)


        createNotificationChannel()

        tvTimePicker = view.findViewById(R.id.tvReminderTime_ReminderActivity)
        tvDatePicker = view.findViewById(R.id.tvReminderDate_ReminderActivity)

        val reminderTitle = view.findViewById<TextView>(R.id.etReminderTitle_ReminderActivity)

        val setReminder = view.findViewById<Button>(R.id.btnSetReminder_ReminderActivity)

        calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(calendar)
        }

        tvDatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        tvTimePicker.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Reminder Time")
                .build()

            timePicker.show(parentFragmentManager,"PKOM")

            timePicker.addOnPositiveButtonClickListener {
                if (timePicker.hour>12){
                    tvTimePicker.text = String.format("%02d", timePicker.hour - 12) + " : " + String.format("%02d", timePicker.minute)+ " PM"
                }
                else{tvTimePicker.text = String.format("%02d", timePicker.hour) + " : " + String.format("%02d", timePicker.minute)+ " AM"
                }
                calendar[Calendar.MINUTE] = timePicker.minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
            }
        }

        setReminder.setOnClickListener {
            setReminder()
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(
                R.id.flReminderActivity,
                ReminderList()
            ).addToBackStack(null).commit()
        }

        val selectFrequency = view.findViewById<AutoCompleteTextView>(R.id.actvSelectFrequency_ReminderActivity)
        val suggestion = arrayOf("Once", "Daily")
        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, suggestion)
        selectFrequency.setAdapter(adapter)



        return view
    }

    private fun updateDate(myCalendar: Calendar) {
        val myFormat =  "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = simpleDateFormat.format(myCalendar.time)
    }

    private fun setReminder() {
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0)

        alarmManager.setRepeating(

            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(context, "Reminder set Successfully", Toast.LENGTH_LONG).show()
    }

   /* private fun cancelAlarm() {
        alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0,intent,0)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }*/

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val name: CharSequence = "PKOMReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("PKOM", name, importance)
            channel.description = description
            context?.getSystemService(
                NotificationManager::class.java
            )?.createNotificationChannel(channel)
        }
    }



}