package com.ainrom.lbcmusic.view.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ainrom.lbcmusic.R
import com.ainrom.lbcmusic.databinding.FragmentAlbumsListBinding
import com.ainrom.lbcmusic.di.Injector
import com.ainrom.lbcmusic.utils.*
import com.ainrom.lbcmusic.view.LaunchActivity
import com.ainrom.lbcmusic.view.ViewModelFactory
import com.ainrom.lbcmusic.view.albums.album.AlbumFragment
import javax.inject.Inject

class AlbumsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var albumsListViewModel: AlbumsListViewModel
    private lateinit var binding: FragmentAlbumsListBinding
    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Injector.get().inject(this)

        albumsListViewModel = ViewModelProvider(activity as LaunchActivity, this.viewModelFactory)
            .get(AlbumsListViewModel::class.java)

        adapter = AlbumsAdapter(albumsListViewModel)

        binding = FragmentAlbumsListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = albumsListViewModel
            rvDevices.adapter = adapter
            rvDevices.layoutManager = LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
        }

        albumsListViewModel.itemClicked.observe(viewLifecycleOwner, EventObserver { album ->
            val bundle = Bundle()
            bundle.putLong(BUNDLE_ALBUM_ID, album.id)
            val albumFragment = AlbumFragment()
            albumFragment.arguments = bundle
            parentFragmentManager.addFragment(albumFragment, R.id.fragment_container)
        })

        albumsListViewModel.alertDialogMessage.observe(viewLifecycleOwner, EventObserver { deviceId ->
                this@AlbumsListFragment.view?.rootView?.showAlertDialog(
                    messageTextRes = R.string.alert_dialog_delete_message,
                    actionClick = { albumsListViewModel.deleteAlbum(deviceId) }
                )
            }
        )

        albumsListViewModel.errorStatus.observe(viewLifecycleOwner, EventObserver { message ->
            message?.toast()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // I found using https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
        // with Dagger a bit cumbersome so I went for this solution. I would use it for an app in prod though.
        if (savedInstanceState != null)
            albumsListViewModel.restoreFilter(savedInstanceState.getInt(SAVED_INSTANCE_FILTER))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_INSTANCE_FILTER, albumsListViewModel.filter.value ?: 0)
    }
}
