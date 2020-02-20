package com.ainrom.lbcmusic.utils

import com.ainrom.lbcmusic.data.local.album.AlbumEntity

fun dummyAlbums(): List<AlbumEntity> {
    val list = mutableListOf<AlbumEntity>()
    for (i in 1L..10L) {
        list.add(AlbumEntity(
            id = i,
            category = 2,
            title = "title $i",
            cover = "cover $i",
            thumbnail = "thumbnail $i"
        ))
    }
    return list
}