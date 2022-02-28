package com.madalinapreda.photosgallery.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.madalinapreda.photosgallery.R
import com.madalinapreda.photosgallery.databinding.ActivityMainBinding
import com.madalinapreda.photosgallery.viewmodel.AlbumsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    var isConnectedNow: Boolean? = null

    private val albumsViewModel: AlbumsViewModel by viewModels {
        AlbumsViewModel.AlbumsViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        binding.toolbar.setupWithNavController(navController)

        albumsViewModel.connectivityLiveData.observe(this) { isConnected ->
            var message = ""
            if (isConnectedNow == false && isConnected == true) {
                message = getString(R.string.connectivity_back)
            } else if (!isConnected) {
                message = getString(R.string.connectivity_lost)
            }

            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            albumsViewModel.prepareAlbumsData()
            isConnectedNow = isConnected
        }
    }
}