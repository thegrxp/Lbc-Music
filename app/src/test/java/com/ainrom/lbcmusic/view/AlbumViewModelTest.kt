package com.ainrom.lbcmusic.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ainrom.lbcmusic.data.Repository
import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import com.ainrom.lbcmusic.utils.CoroutinesTestRule
import com.ainrom.lbcmusic.utils.Event
import com.ainrom.lbcmusic.view.albums.album.AlbumViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class AlbumViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(Repository::class.java)
    private val albumViewModel = AlbumViewModel(repository)

    @Test
    fun testInitNull() = runBlocking {
        albumViewModel.initAlbum(null)
        verify(repository, never()).getAlbum(3L)
        assertNull(albumViewModel.albumSelected.value)
    }

    @Test
    fun testInitResultToUI() = runBlocking {
        val album = AlbumEntity(1, 2, "title", "cover", "thumbnail")
        `when`(repository.getAlbum(1L)).thenReturn(album)
        val observer = mock(Observer::class.java)
        albumViewModel.albumSelected.observeForever(observer as Observer<AlbumEntity>)
        albumViewModel.initAlbum(1L)
        verify(repository).getAlbum(1L)
        verify(observer).onChanged(album)
    }

    @Test
    fun testCloseAlbum() = runBlocking {
        val observer = mock(Observer::class.java)
        albumViewModel.closeClicked.observeForever(observer as Observer<Event<Boolean>>)
        albumViewModel.closeAlbum()
        assert((albumViewModel.closeClicked.value as Event<Boolean>).peekContent())
    }
}
