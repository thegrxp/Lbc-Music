package com.ainrom.lbcmusic.di

import android.app.Application
import android.content.Context
import com.ainrom.lbcmusic.data.Repository
import com.ainrom.lbcmusic.view.albums.AlbumsListFragment
import com.ainrom.lbcmusic.view.albums.album.AlbumFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        WebServiceModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun inject(albumFragment: AlbumFragment)
    fun inject(albumsListFragment: AlbumsListFragment)

    fun engine(): Repository
    fun context(): Context
}