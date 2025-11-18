package com.lifelog.feature.meds.reminders

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lifelog.core.domain.model.Med
import java.util.concurrent.TimeUnit

object ReminderManager {

    fun startReminder(context: Context, med: Med) {
        val workManager = WorkManager.getInstance(context)
        val inputData = Data.Builder()
            .putString("medName", med.name)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MedicationReminderWorker>()
            .setInitialDelay(calculateInitialDelay(med.timeOfDay), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(
            "med_reminder_${med.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(context: Context, med: Med) {
        WorkManager.getInstance(context).cancelUniqueWork("med_reminder_${med.id}")
    }

    private fun calculateInitialDelay(timeOfDay: String): Long {
        // TODO: Implement proper time calculation
        return 10_000 // 10 seconds for testing
    }
}
