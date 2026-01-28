package com.example.owlfriend.di

import com.example.owlfriend.data.repository.SessionRepositoryImpl
import com.example.owlfriend.data.repository.SubjectRepositoryImpl
import com.example.owlfriend.data.repository.TaskRepositoryImpl
import com.example.owlfriend.domain.repository.SubjectRepository
import com.example.owlfriend.domain.repository.TaskRepository
import com.example.owlfriend.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class  RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSubjectRepository(impl: SubjectRepositoryImpl) : SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl) : TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl) : SessionRepository
}