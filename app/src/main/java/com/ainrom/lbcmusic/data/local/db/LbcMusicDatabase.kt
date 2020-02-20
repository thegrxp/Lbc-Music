package com.ainrom.lbcmusic.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ainrom.lbcmusic.data.local.album.AlbumDao
import com.ainrom.lbcmusic.data.local.album.AlbumEntity

@Database(
    entities = [(AlbumEntity::class)],
    version = 1,
    exportSchema = false
)

abstract class LbcMusicDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}