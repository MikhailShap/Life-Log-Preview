package com.lifelog.feature.meds.reminders

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lifelog.core.domain.model.Med
import java.util.concurrent.TimeUnit

class ReminderManager(context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(med: Med) {
        // This is a simplified scheduling logic.
        // A real implementation would parse med.timeOfDay to calculate the initial delay.
        val initialDelay = 10L // seconds for testing

        val data = Data.Builder()
            .putString(MedicationReminderWorker.MED_NAME_KEY, med.name)
            .build()

        val reminderWorkRequest = OneTimeWorkRequestBuilder<MedicationReminderWorker>()
            .setInitialDelay(initialDelay, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        workManager.enqueueUniqueWork(
            med.name, // Use med name as unique work name
            ExistingWorkPolicy.REPLACE,
            reminderWorkRequest
        )
    }

    fun cancelReminder(med: Med) {
        workManager.cancelUniqueWork(med.name)
    }
}
