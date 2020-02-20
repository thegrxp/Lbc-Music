package com.ainrom.lbcmusic.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ainrom.lbcmusic.view.ViewModelFactory
import com.ainrom.lbcmusic.view.albums.AlbumsListViewModel
import com.ainrom.lbcmusic.view.albums.album.AlbumViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AlbumsListViewModel::class)
    abstract fun bindDevicesListViewModel(albumsViewModel: AlbumsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    abstract fun bindDeviceViewModel(devicesListViewModel: AlbumViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}