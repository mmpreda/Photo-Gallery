package com.madalinapreda.photosgallery.view.albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.madalinapreda.photosgallery.R
import com.madalinapreda.photosgallery.databinding.FragmentAlbumsBinding
import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.viewmodel.AlbumsViewModel

class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private val albumsViewModel: AlbumsViewModel by navGraphViewModels(R.id.gallery_nav_graph)

    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.albumsRv.layoutManager = LinearLayoutManager(requireContext())
        albumsAdapter = AlbumsAdapter(this@AlbumsFragment)
        binding.albumsRv.adapter = albumsAdapter

        binding.albumsLoader.visibility = View.VISIBLE
        binding.albumsLoader.animate()

        albumsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            showError()
        }
        albumsViewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.albumsLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.albumsLoader.animate()
        }
        albumsViewModel.getAlbumsWithAuthors().observe(viewLifecycleOwner) {
            albumsAdapter.setAlbums(it)
            if (it.isNotEmpty()) {
                showList()
            } else {
                showError()
            }
        }
    }

    private fun showError() {
        binding.albumsRv.visibility = View.GONE
        binding.noDataTv.visibility = View.VISIBLE
    }

    private fun showList() {
        binding.albumsRv.visibility = View.VISIBLE
        binding.noDataTv.visibility = View.GONE
    }

    fun navigateToAlbumContent(album: Album) {
        findNavController().navigate(
            AlbumsFragmentDirections.navigateToPhotos(album)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}