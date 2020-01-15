package com.android.imagegallery.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.audiovideoplayer.binding.FragmentDataBindingComponent
import com.android.imagegallery.R
import com.android.imagegallery.databinding.FragmentImageListBinding
import com.android.imagegallery.di.Injectable
import com.android.imagegallery.dialog.ImageDialogHelper
import com.android.imagegallery.model.Image
import com.android.imagegallery.ui.MainActivity
import com.android.imagegallery.ui.MainViewModel
import com.android.imagegallery.ui.adapter.ImageAdapter
import com.android.imagegallery.util.AppExecutors
import com.android.imagegallery.util.autoCleared
import com.android.imagegallery.views.GridAutofitLayoutManager
import javax.inject.Inject


class ImageListFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentImageListBinding>()
    var imagesAdapter by autoCleared<ImageAdapter>()

    private lateinit var imageDialogHelper: ImageDialogHelper

    private lateinit var callback: ImageListFragmentCallback

    companion object {
        fun newInstance() = ImageListFragment()
    }

    interface ImageListFragmentCallback {
        fun onDownload(image: Image)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ImageListFragmentCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        imagesAdapter = ImageAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors, clickCallback = {
                imageDialogHelper = ImageDialogHelper(it, true,
                    download = { image ->
                        image.let {
                            callback.onDownload(image)
                        }
                    })
                imageDialogHelper.attachTo(context as FragmentActivity)
            })

        binding.imageList.layoutManager = context?.let { GridAutofitLayoutManager(it, 200) }

        binding.imageList.adapter = imagesAdapter

        viewModel.getImages()
    }

    private fun initRecyclerView() {
        viewModel.images.observe(viewLifecycleOwner, Observer { result ->
            imagesAdapter.submitList(result.hits)
        })
    }
}