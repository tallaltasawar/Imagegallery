package com.android.imagegallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.imagegallery.R
import com.android.imagegallery.adapter.DataBoundListAdapter
import com.android.imagegallery.databinding.ItemImageBinding
import com.android.imagegallery.model.Image
import com.android.imagegallery.util.AppExecutors

/**
 * A RecyclerView adapter for [Image] class.
 */
class ImageAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((image: Image) -> Unit)?
) : DataBoundListAdapter<Image, ItemImageBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.largeImageURL == newItem.largeImageURL
                    && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.type == newItem.type
                    && oldItem.likes == newItem.likes
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemImageBinding {
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_image,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.imageData.let {
                it?.let { it1 -> clickCallback?.invoke(it1) }
            }
        }
        return binding
    }

    override fun bind(binding: ItemImageBinding, item: Image, position: Int) {
        binding.imageData = item

    }
}
