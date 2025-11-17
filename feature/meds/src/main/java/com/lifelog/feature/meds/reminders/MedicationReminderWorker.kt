package com.lifelog.feature.meds.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lifelog.feature.meds.R

class MedicationReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val medName = inputData.getString(MED_NAME_KEY) ?: return Result.failure()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_medication) // You need to add this drawable
            .setContentTitle("Medication Reminder")
            .setContentText("It's time to take your $medName.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(medName.hashCode(), notification)

        return Result.success()
    }

    companion object {
        const val MED_NAME_KEY = "med_name"
        const val CHANNEL_ID = "medication_reminder_channel"
    }
}
