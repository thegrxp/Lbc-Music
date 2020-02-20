package com.ainrom.lbcmusic.data.local.album

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums ORDER BY id")
    fun getAlbums(): DataSource.Factory<Int, AlbumEntity>

    @Query("SELECT * FROM albums WHERE category = :category ORDER BY id")
    fun getCategory(category: Int): DataSource.Factory<Int, AlbumEntity>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: Long): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>?)

    @Query("DELETE FROM albums WHERE id = :id")
    suspend fun deleteAlbum(id: Long)
}