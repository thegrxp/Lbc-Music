package com.ainrom.lbcmusic.data.remote

import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import retrofit2.http.*

interface WebService {

    @GET("img/shared/technical-test.json")
    suspend fun fetchAlbums(): List<AlbumEntity>
}
