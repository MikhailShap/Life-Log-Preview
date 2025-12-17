package com.lifelog.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lifelog.core.data.local.converter.Converters
import com.lifelog.core.data.local.dao.EntryDao
import com.lifelog.core.data.local.dao.MedDao
import com.lifelog.core.data.local.dao.MoodDao
import com.lifelog.core.data.local.dao.SleepDao
import com.lifelog.core.data.local.dao.VideoNoteDao
import com.lifelog.core.data.local.entity.EntryEntity
import com.lifelog.core.data.local.entity.MedEntity
import com.lifelog.core.data.local.entity.MoodEntity
import com.lifelog.core.data.local.entity.SleepEntity
import com.lifelog.core.data.local.entity.VideoNoteEntity

@Database(
    entities = [
        EntryEntity::class, 
        VideoNoteEntity::class, 
        MedEntity::class,
        MoodEntity::class,
        SleepEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun videoNoteDao(): VideoNoteDao
    abstract fun medDao(): MedDao
    abstract fun moodDao(): MoodDao
    abstract fun sleepDao(): SleepDao
}
