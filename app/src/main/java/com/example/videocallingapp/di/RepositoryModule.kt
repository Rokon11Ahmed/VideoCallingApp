package com.example.videocallingapp.di

import com.example.videocallingapp.data.repository.UserRepository
import com.example.videocallingapp.data.repository.UserRepositoryImp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase{
        return Firebase.database
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseDatabase
    ): UserRepository{
        return UserRepositoryImp(database)
    }
}