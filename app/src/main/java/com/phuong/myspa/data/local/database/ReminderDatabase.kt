package com.phuong.myspa.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phuong.myspa.data.local.dao.ReminderDao
import com.phuong.myspa.data.local.entity.ReminderEntity
import com.phuong.myspa.data.local.converter.InstantConverter

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [InstantConverter::class])
abstract class ReminderDatabase: RoomDatabase() {

    abstract val reminderDao: ReminderDao
}