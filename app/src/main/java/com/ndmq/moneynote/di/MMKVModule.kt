package com.ndmq.moneynote.di

import android.content.Context
import com.ndmq.moneynote.data.repository.MMKVRepository
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MMKVModule {

    @Provides
    @Singleton
    fun provideMMKV(
        @ApplicationContext context: Context
    ): MMKV {
        MMKV.initialize(context)
        return MMKV.defaultMMKV()
    }

    @Provides
    @Singleton
    fun provideRepository(mmkv: MMKV): MMKVRepository {
        return MMKVRepository(mmkv)
    }
}