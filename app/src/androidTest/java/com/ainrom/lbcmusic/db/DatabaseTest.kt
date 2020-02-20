package com.ainrom.lbcmusic.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ainrom.lbcmusic.data.local.album.AlbumDao
import com.ainrom.lbcmusic.data.local.db.LbcMusicDatabase
import com.ainrom.lbcmusic.utils.dummyAlbums
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DatabaseTest {

    private lateinit var dao: AlbumDao
    private lateinit var db: LbcMusicDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, LbcMusicDatabase::class.java).build()
        dao = db.albumDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Not ideal to test all at once but had to hurry up!
     */
    @Test
    @Throws(Exception::class)
    fun insertAndLoadAndDelete() = runBlocking {
        dao.insertAlbums(dummyAlbums())

        val readAlbum = dao.getAlbum(4)
        assertThat(readAlbum, notNullValue())
        assertThat(readAlbum?.id, `is`(4L))
        assertThat(readAlbum?.category, `is`(2))
        assertThat(readAlbum?.title, `is`("title ${4}"))
        assertThat(readAlbum?.cover, `is`("cover ${4}"))
        assertThat(readAlbum?.thumbnail, `is`("thumbnail ${4}"))

        dao.deleteAlbum(4)
        val deletedAlbum = dao.getAlbum(4)
        assertNull(deletedAlbum)
    }
}