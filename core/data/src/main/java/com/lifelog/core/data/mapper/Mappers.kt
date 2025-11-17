package com.lifelog.core.data.mapper

import com.lifelog.core.data.local.entity.EntryEntity
import com.lifelog.core.data.local.entity.MedEntity
import com.lifelog.core.data.local.entity.VideoNoteEntity
import com.lifelog.core.domain.model.Entry
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.model.VideoNote

fun Entry.toEntity() = EntryEntity(
    id = id,
    date = date,
    mood = mood,
    energy = energy,
    anxiety = anxiety,
    sleepHours = sleepHours,
    notes = notes,
    tags = tags,
    videoNoteIds = videoNoteIds
)

fun EntryEntity.toDomain() = Entry(
    id = id,
    date = date,
    mood = mood,
    energy = energy,
    anxiety = anxiety,
    sleepHours = sleepHours,
    notes = notes,
    tags = tags,
    videoNoteIds = videoNoteIds
)

fun VideoNote.toEntity() = VideoNoteEntity(
    id = id,
    createdAt = createdAt,
    uri = uri,
    thumbUri = thumbUri,
    duration = duration
)

fun VideoNoteEntity.toDomain() = VideoNote(
    id = id,
    createdAt = createdAt,
    uri = uri,
    thumbUri = thumbUri,
    duration = duration
)

fun Med.toEntity() = MedEntity(
    id = id,
    name = name,
    dosage = dosage,
    timeOfDay = timeOfDay
)

fun MedEntity.toDomain() = Med(
    id = id,
    name = name,
    dosage = dosage,
    timeOfDay = timeOfDay
)
