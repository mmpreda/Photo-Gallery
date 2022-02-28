package com.madalinapreda.photosgallery.view.photos

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.madalinapreda.photosgallery.R
import com.madalinapreda.photosgallery.databinding.FragmentPhotosBinding
import com.madalinapreda.photosgallery.viewmodel.PhotosViewModel

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private val photosViewModel: PhotosViewModel by navGraphViewModels(R.id.gallery_nav_graph)

    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        fetchPhotosForCurrentAlbum()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
        binding.photosRv.layoutManager = GridLayoutManager(requireContext(), columns)

        photosAdapter = PhotosAdapter(this@PhotosFragment)
        binding.photosRv.adapter = photosAdapter

        photosViewModel.apiResponseLiveData.observe(viewLifecycleOwner) {
            photosAdapter.setPhotos(it)
            binding.photosRv.visibility = View.VISIBLE
        }

        photosViewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.photosLoader.visibility = View.VISIBLE
                binding.photosLoader.animate()
                binding.photosRv.visibility = View.GONE
                binding.noDataTv.visibility = View.GONE
            } else {
                binding.photosLoader.visibility = View.GONE
            }
        }

        photosViewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.noDataTv.visibility = if (it == null) View.GONE else View.VISIBLE
        }

        photosViewModel.connectivityLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                fetchPhotosForCurrentAlbum()
            }
        }
    }

    private fun fetchPhotosForCurrentAlbum() {
        val args: PhotosFragmentArgs by navArgs()
        val currentAlbum = args.album
        photosViewModel.getPhotosInAlbum(currentAlbum.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}