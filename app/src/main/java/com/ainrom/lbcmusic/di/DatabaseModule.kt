package com.ainrom.lbcmusic.di

import android.app.Application
import androidx.room.Room
import com.ainrom.lbcmusic.data.local.album.AlbumDao
import com.ainrom.lbcmusic.data.local.db.LbcMusicDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): LbcMusicDatabase {
        return Room
            .databaseBuilder(app, LbcMusicDatabase::class.java, "lbcmusic.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAlbumDao(db: LbcMusicDatabase): AlbumDao {
        return db.albumDao()
    }
}