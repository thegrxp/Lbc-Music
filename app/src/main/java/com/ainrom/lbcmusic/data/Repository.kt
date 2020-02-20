package com.ainrom.lbcmusic.data

import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import com.ainrom.lbcmusic.data.local.db.LbcMusicDatabase
import com.ainrom.lbcmusic.data.remote.NetworkBoundResource
import com.ainrom.lbcmusic.data.remote.WebService
import com.ainrom.lbcmusic.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val db: LbcMusicDatabase, // if multiple local data sources I would have added a `LocalDataSource` class
    private val webService: WebService // if multiple remote data sources I would have added a `RemoteDataSource` class
) {

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun category(category: Int): Flow<Resource<PagedList<AlbumEntity>>> =
        object : NetworkBoundResource<PagedList<AlbumEntity>, List<AlbumEntity>>() {
            override fun query(): Flow<PagedList<AlbumEntity>> =
                db.albumDao().getCategory(category).toLiveData(50).asFlow()

            override suspend fun fetch(): List<AlbumEntity> = webService.fetchAlbums()

            override suspend fun saveFetchResult(data: List<AlbumEntity>) =
                db.albumDao().insertAlbums(data)

            override suspend fun shouldFetch(data: PagedList<AlbumEntity>?): Boolean {
                return data == null || data.isEmpty()
            }
        }.asFlow()

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun albums(): Flow<Resource<PagedList<AlbumEntity>>> =
        object : NetworkBoundResource<PagedList<AlbumEntity>, List<AlbumEntity>>() {
            override fun query(): Flow<PagedList<AlbumEntity>> =
                db.albumDao().getAlbums().toLiveData(50).asFlow()

            override suspend fun fetch(): List<AlbumEntity> = webService.fetchAlbums()

            override suspend fun saveFetchResult(data: List<AlbumEntity>) =
                db.albumDao().insertAlbums(data)

            override suspend fun shouldFetch(data: PagedList<AlbumEntity>?): Boolean {
                return data == null || data.isEmpty()
            }
        }.asFlow()

    fun refresh() = flow {
        try {
            emit(Resource.loading(null))
            val refreshedAlbums = withContext(Dispatchers.Default) { webService.fetchAlbums() }
            db.albumDao().insertAlbums(refreshedAlbums)
            emit(Resource.success(refreshedAlbums))
        } catch (e: Exception) {
            emit(e.message?.let { message -> Resource.error("Oh no! Something went wrong. $message", null) })
        }
    }

    suspend fun deleteAlbum(id: Long) = db.albumDao().deleteAlbum(id)
    suspend fun getAlbum(id: Long): AlbumEntity? = db.albumDao().getAlbum(id)
}