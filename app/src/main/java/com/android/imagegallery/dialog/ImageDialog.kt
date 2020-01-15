package com.android.imagegallery.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.imagegallery.R
import com.android.imagegallery.databinding.DialogImageBinding
import com.android.imagegallery.model.Image
import kotlinx.android.synthetic.main.dialog_image.*


class ImageDialogHelper(
    val image: Image,
    val cancelable: Boolean,
    val download: (image: Image) -> Unit): BaseDialogHelper(ImageDialogFragment())

class ImageDialogFragment: BaseDialogFragment<ImageDialogHelper>(){

    private lateinit var binding: DialogImageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_image, container, false)

        binding.imageData = helper.image

        isCancelable = helper.cancelable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDownload.setOnClickListener {
            helper.download(helper.image)
        }
    }
}