package com.ainrom.lbcmusic.view.albums.album

import androidx.lifecycle.*
import com.ainrom.lbcmusic.data.Repository
import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import com.ainrom.lbcmusic.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    AlbumActionsViewModel {

    private val _closeClicked = MutableLiveData<Event<Boolean>>()
    override val closeClicked: LiveData<Event<Boolean>>
        get() = _closeClicked

    private val _albumSelected = MutableLiveData<AlbumEntity>()
    override val albumSelected: LiveData<AlbumEntity>
        get() = _albumSelected

    override fun initAlbum(albumId: Long?) {
        if (albumId != null)
            viewModelScope.launch(Dispatchers.IO) {
                _albumSelected.postValue(repository.getAlbum(albumId))
            }
    }

    override fun restoreAlbum(albumId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _albumSelected.postValue(repository.getAlbum(albumId))
        }
    }

    override fun closeAlbum() {
        _closeClicked.value = Event(true)
    }
}

private interface AlbumActionsViewModel {
    val closeClicked: LiveData<Event<Boolean>>
    val albumSelected: LiveData<AlbumEntity>
    fun closeAlbum()
    fun initAlbum(albumId: Long?)
    fun restoreAlbum(albumId: Long)
}