package com.example.owlfriend.di

import com.example.owlfriend.data.repository.SessionRepositoryImpl
import com.example.owlfriend.data.repository.SubjectRepositoryImpl
import com.example.owlfriend.data.repository.TaskRepositoryImpl
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
    abstract fun bindSubjectRepository(impl: SubjectRepositoryImpl) : SubjectRepositoryImpl

    @Singleton
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl) : TaskRepositoryImpl

    @Singleton
    @Binds
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl) : SessionRepositoryImpl
}