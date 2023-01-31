package com.example.pkompoultrymanager.reminder

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pkompoultrymanager.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, ReminderActivity::class.java )
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)

        val builder = NotificationCompat.Builder(context!!, "PKOM")
            .setSmallIcon(R.drawable.reminder)
            .setContentTitle("Poultry Manager")
            .setContentText("View your reminder")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())

        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();

    }
}