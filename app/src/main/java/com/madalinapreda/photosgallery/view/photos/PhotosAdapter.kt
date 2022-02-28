package com.madalinapreda.photosgallery.view.photos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.madalinapreda.photosgallery.R
import com.madalinapreda.photosgallery.databinding.PhotoItemBinding
import com.madalinapreda.photosgallery.model.Photo
import com.squareup.picasso.Picasso

class PhotosAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    private var photosList: ArrayList<Photo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding: PhotoItemBinding = PhotoItemBinding.inflate(
            LayoutInflater.from(fragment.requireContext()), parent, false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photosList[position]
        Picasso.get()
            .load(photo.url)
            .fit()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_broken_image)
            .into(holder.image)
        holder.title.text = photo.title.capitalize()
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(photos: List<Photo>) {
        photosList.clear()
        photosList.addAll(photos)
        notifyDataSetChanged()
    }

    class PhotoViewHolder(view: PhotoItemBinding) : RecyclerView.ViewHolder(view.root) {
        val image = view.photoIv
        val title = view.photoTitle
    }
}