package com.lifelog.core.data.di

import android.content.Context
import androidx.room.Room
import com.lifelog.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lifelog_database"
        ).build()
    }

    @Provides
    fun provideEntryDao(appDatabase: AppDatabase) = appDatabase.entryDao()

    @Provides
    fun provideVideoNoteDao(appDatabase: AppDatabase) = appDatabase.videoNoteDao()

    @Provides
    fun provideMedDao(appDatabase: AppDatabase) = appDatabase.medDao()
}
