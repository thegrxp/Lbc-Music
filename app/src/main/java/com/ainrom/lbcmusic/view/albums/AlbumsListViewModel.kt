package com.ainrom.lbcmusic.view.albums

import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ainrom.lbcmusic.data.Repository
import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import com.ainrom.lbcmusic.utils.Event
import com.ainrom.lbcmusic.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumsListViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    AlbumsListActionsViewModel {

    private val _filter = MutableLiveData(0)
    override val filter: LiveData<Int>
        get() = _filter

    private val _loadingStatus = MutableLiveData<Status>()
    override val loadingStatus: LiveData<Status>
        get() = _loadingStatus

    private val _errorStatus = MutableLiveData<Event<String?>>()
    override val errorStatus: LiveData<Event<String?>>
        get() = _errorStatus

    private val _alertDialogMessage = MutableLiveData<Event<Long>>()
    override val alertDialogMessage: LiveData<Event<Long>>
        get() = _alertDialogMessage

    private val _itemClicked = MutableLiveData<Event<AlbumEntity>>()
    override val itemClicked: LiveData<Event<AlbumEntity>>
        get() = _itemClicked

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val _filteredAlbums: LiveData<PagedList<AlbumEntity>?> =
        Transformations.switchMap(_filter) { filter ->
            if (filter == null || filter == 0) {
                repository.albums()
                    .map { content ->
                        _loadingStatus.postValue(content.status)
                        if (content.status == Status.ERROR) _errorStatus.postValue(Event(content.message))
                        content.data
                    }.asLiveData()
            } else {
                repository.category(filter)
                    .map { content ->
                        _loadingStatus.postValue(content.status)
                        if (content.status == Status.ERROR) _errorStatus.postValue(Event(content.message))
                        content.data
                    }.asLiveData()
            }
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override val filteredAlbums: LiveData<PagedList<AlbumEntity>?>
        get() = _filteredAlbums

    override fun filterSelected(filter: CharSequence) {
        var value = 0
        if (filter.isNotBlank()) value = filter.toString().toInt()
        _filter.value = value
    }

    override fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refresh().collect { content ->
                _loadingStatus.postValue(content?.status)
            }
        }
    }

    override fun openAlbum(album: AlbumEntity) {
        _itemClicked.value = Event(album)
    }

    override fun deleteDialog(view: View, album: AlbumEntity): Boolean {
        _alertDialogMessage.value = Event(album.id)
        return true
    }

    override fun deleteAlbum(deviceId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlbum(deviceId)
        }
    }

    override fun restoreFilter(filter: Int) {
        _filter.value = filter
    }
}

private interface AlbumsListActionsViewModel {
    val filteredAlbums: LiveData<PagedList<AlbumEntity>?>
    val loadingStatus: LiveData<Status>
    val errorStatus: LiveData<Event<String?>>
    val itemClicked: LiveData<Event<AlbumEntity>>
    val filter: LiveData<Int>
    val alertDialogMessage: LiveData<Event<Long>>
    fun openAlbum(album: AlbumEntity)
    fun deleteDialog(view: View, album: AlbumEntity): Boolean
    fun deleteAlbum(deviceId: Long)
    fun filterSelected(filter: CharSequence)
    fun refresh()
    fun restoreFilter(filter: Int)
}