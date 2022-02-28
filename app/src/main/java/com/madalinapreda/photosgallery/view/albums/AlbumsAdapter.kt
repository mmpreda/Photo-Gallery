package com.madalinapreda.photosgallery.view.albums

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.madalinapreda.photosgallery.databinding.AlbumItemBinding
import com.madalinapreda.photosgallery.model.Album

class AlbumsAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    private var albumsWithAuthors: Map<Album, String> = mapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: AlbumItemBinding = AlbumItemBinding.inflate(
            LayoutInflater.from(fragment.requireContext()), parent, false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumsWithAuthors.keys.elementAt(position)

        holder.title.text = album.title
        holder.author.text = albumsWithAuthors.values.elementAt(position)
        holder.container.setOnClickListener {
            if (fragment is AlbumsFragment) {
                fragment.navigateToAlbumContent(album)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumsWithAuthors.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAlbums(albums: Map<Album, String>) {
        albumsWithAuthors = albums
        notifyDataSetChanged()
    }

    class AlbumViewHolder(view: AlbumItemBinding) : RecyclerView.ViewHolder(view.root) {
        val container = view.root
        val title = view.albumTitle
        val author = view.albumAuthor
    }
}