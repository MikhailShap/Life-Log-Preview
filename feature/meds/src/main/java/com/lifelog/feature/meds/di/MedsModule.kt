package com.lifelog.feature.meds.di

import android.content.Context
import com.lifelog.feature.meds.reminders.ReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedsModule {

    @Provides
    @Singleton
    fun provideReminderManager(@ApplicationContext context: Context): ReminderManager {
        return ReminderManager(context)
    }
}
