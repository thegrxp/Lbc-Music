package com.ainrom.lbcmusic.view.albums.album

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ainrom.lbcmusic.di.Injector
import com.ainrom.lbcmusic.view.LaunchActivity
import com.ainrom.lbcmusic.view.ViewModelFactory
import javax.inject.Inject
import com.ainrom.lbcmusic.R
import com.ainrom.lbcmusic.databinding.FragmentAlbumBinding
import com.ainrom.lbcmusic.utils.*

class AlbumFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var binding: FragmentAlbumBinding
    private var albumId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Injector.get().inject(this)

        albumViewModel = ViewModelProvider(activity as LaunchActivity, this.viewModelFactory)
            .get(AlbumViewModel::class.java)

        albumId = arguments?.getLong(BUNDLE_ALBUM_ID) ?: 0L
        albumViewModel.initAlbum(albumId)

        binding = FragmentAlbumBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = albumViewModel
        }

        albumViewModel.closeClicked.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.removeFragment(this@AlbumFragment, R.id.fragment_container)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // I found using https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
        // with Dagger a bit cumbersome so I went for this solution. I would use it for an app in prod though.
        if (savedInstanceState != null)
            albumViewModel.restoreAlbum(savedInstanceState.getLong(SAVED_INSTANCE_ALBUM_ID))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
            outState.putLong(SAVED_INSTANCE_ALBUM_ID, albumId)
    }
}
