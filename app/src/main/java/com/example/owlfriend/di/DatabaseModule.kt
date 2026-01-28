package com.example.owlfriend.di

import android.app.Application
import androidx.room.Room
import com.example.owlfriend.data.local.AppDatabase
import com.example.owlfriend.data.local.SessionDao
import com.example.owlfriend.data.local.SubjectDao
import com.example.owlfriend.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ) : AppDatabase {
            return Room.databaseBuilder(application, AppDatabase::class.java,
                "owlfriend.db").build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase) : SubjectDao {
        return database.subjectDao()
    }
    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase) : SessionDao {
        return database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase) : TaskDao {
        return database.taskDao()
    }
}