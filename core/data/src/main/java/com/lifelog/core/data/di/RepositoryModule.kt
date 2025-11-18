package com.lifelog.core.data.di

import com.lifelog.core.data.repository.EntryRepositoryImpl
import com.lifelog.core.data.repository.MedRepositoryImpl
import com.lifelog.core.data.repository.SettingsRepositoryImpl
import com.lifelog.core.data.repository.VideoNoteRepositoryImpl
import com.lifelog.core.domain.repository.EntryRepository
import com.lifelog.core.domain.repository.MedRepository
import com.lifelog.core.domain.repository.SettingsRepository
import com.lifelog.core.domain.repository.VideoNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEntryRepository(impl: EntryRepositoryImpl): EntryRepository

    @Binds
    @Singleton
    abstract fun bindVideoNoteRepository(impl: VideoNoteRepositoryImpl): VideoNoteRepository

    @Binds
    @Singleton
    abstract fun bindMedRepository(impl: MedRepositoryImpl): MedRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
