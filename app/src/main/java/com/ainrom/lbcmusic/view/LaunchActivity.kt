package com.ainrom.lbcmusic.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ainrom.lbcmusic.R
import com.ainrom.lbcmusic.utils.replaceFragment
import com.ainrom.lbcmusic.view.albums.AlbumsListFragment

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        if (savedInstanceState == null)
            this@LaunchActivity.replaceFragment(AlbumsListFragment(), R.id.fragment_container)
    }
}
