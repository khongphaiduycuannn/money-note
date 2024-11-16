package com.ndmq.moneynote.di

import android.content.Context
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.data.source.local.AppDao
import com.ndmq.moneynote.data.source.local.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(
        @ApplicationContext context: Context
    ): AppDao {
        return AppRoomDatabase.getDatabase(context).appDao()
    }

    @Provides
    @Singleton
    fun provideRepository(appDao: AppDao): AppRepository {
        return AppRepository(appDao)
    }
}