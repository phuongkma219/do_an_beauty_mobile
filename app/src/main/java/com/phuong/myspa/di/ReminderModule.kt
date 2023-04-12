package com.phuong.myspa.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.phuong.myspa.data.repository.ReminderRepositoryImpl
import com.phuong.myspa.data.local.dao.ReminderDao
import com.phuong.myspa.data.local.database.ReminderDatabase
import com.phuong.myspa.domain.repository.ReminderRepository
import com.phuong.myspa.domain.repository.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {


    @Provides
    @Singleton
    fun provideReminderDatabase(app: Application): ReminderDatabase {
        return Room.databaseBuilder(
            app, ReminderDatabase::class.java, "reminder_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(
        db: ReminderDatabase,
        @ApplicationContext appContext: Context
    ): ReminderRepository {
        return ReminderRepositoryImpl(db.reminderDao, appContext)
    }

    @Provides
    @Singleton
    fun provideInsertReminderUseCase(reminderRepo: ReminderRepository): InsertReminderUseCase {
        return InsertReminderUseCase(reminderRepo)
    }

    @Provides
    @Singleton
    fun provideDeleteReminderUseCase(reminderRepo: ReminderRepository): DeleteReminderUseCase {
        return DeleteReminderUseCase(reminderRepo)
    }

    @Provides
    @Singleton
    fun provideGetReminderByIdUseCase(reminderRepo: ReminderRepository): GetReminderByIdUseCase {
        return GetReminderByIdUseCase(reminderRepo)
    }

    @Provides
    @Singleton
    fun provideUpdateReminderUseCase(reminderRepo: ReminderRepository): UpdateReminderUseCase {
        return UpdateReminderUseCase(reminderRepo)
    }

    @Provides
    @Singleton
    fun provideGetRemindersUseCase(reminderRepo: ReminderRepository): GetRemindersUseCase {
        return GetRemindersUseCase(reminderRepo)
    }

    @Provides
    @Singleton
    fun provideReminderDao(db: ReminderDatabase): ReminderDao = db.reminderDao


    @Provides
    @Singleton
    fun provideReminderUseCases(
        insertReminderUseCase: InsertReminderUseCase,
        deleteReminderUseCase: DeleteReminderUseCase,
        getReminderByIdUseCase: GetReminderByIdUseCase,
        updateReminderUseCase: UpdateReminderUseCase,
        getRemindersUseCase: GetRemindersUseCase,
    ): ReminderUseCases {
        return ReminderUseCases(
            insertReminderUseCase = insertReminderUseCase,
            deleteReminderUseCase = deleteReminderUseCase,
            getReminderByIdUseCase = getReminderByIdUseCase,
            updateReminderUseCase = updateReminderUseCase,
            getRemindersUseCase = getRemindersUseCase
        )
    }
}