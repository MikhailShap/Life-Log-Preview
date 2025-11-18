package com.lifelog.feature.meds.reminders

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MedicationReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val medName = inputData.getString("medName") ?: return Result.failure()
        // TODO: Show notification
        return Result.success()
    }
}
