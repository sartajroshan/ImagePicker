package com.github.drjacky.imagepicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.drjacky.imagepicker.ImagePicker.Companion.EXTRA_IMAGES
import com.github.drjacky.imagepicker.adapter.ImageCropAdapter
import com.github.drjacky.imagepicker.databinding.ActivityImagePickerFinalBinding
import com.github.drjacky.imagepicker.provider.CropProvider

class ImagePickerFinalActivity : AppCompatActivity(), ImageCropAdapter.CropListener {
    private var cropImageIndex: Int? = null
    private lateinit var imageCropAdapter: ImageCropAdapter
    private val doneClickListener = View.OnClickListener { onDone() }
    private lateinit var binding: ActivityImagePickerFinalBinding
    private lateinit var mCropProvider: CropProvider

    private val cropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            mCropProvider.handleResult(it)
        }

    private fun onDone() {
        if (imageCropAdapter.images.size == 1) {
            setResult(imageCropAdapter.images.single())
        } else {
            setMultipleImageResult(imageCropAdapter.images)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        // Create Crop Provider
        mCropProvider = CropProvider(this, ::setCropImage) { cropLauncher.launch(it) }
        mCropProvider.onRestoreInstanceState(savedInstanceState)

        val images = intent.getParcelableArrayListExtra<Uri>(EXTRA_IMAGES)
        images?.let {
            Log.d("ImageFinal::", images.size.toString())
            imageCropAdapter = ImageCropAdapter(
                this@ImagePickerFinalActivity, it,
                this@ImagePickerFinalActivity
            )
            val layoutmngr = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

            binding.recyclerViewImages.apply {
                layoutManager = layoutmngr
                adapter = imageCropAdapter
            }
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.recyclerViewImages)

            binding.next.setOnClickListener { next() }
            binding.previous.setOnClickListener { previous() }

            singleImage(it.size <= 1)

            binding.recyclerViewImages.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (layoutmngr.findFirstVisibleItemPosition() == 0) {
                        binding.previous.visibility = View.GONE
                    } else {
                        binding.previous.visibility = View.VISIBLE
                    }
                    if (layoutmngr.findLastVisibleItemPosition() == images.size - 1) {
                        binding.next.visibility = View.GONE
                    } else {
                        binding.next.visibility = View.VISIBLE
                    }
                }
            })

        }

    }

    private fun setupToolbar() {
        binding.toolbar.let { imagePickerToolbar ->
            imagePickerToolbar.config()
            imagePickerToolbar.showOnlyDoneButton(true)
            imagePickerToolbar.setOnDoneClickListener(doneClickListener)
            imagePickerToolbar.setOnBackClickListener { finish() }
        }
    }

    private fun singleImage(isSingle: Boolean) {
        if (isSingle) {
            binding.next.visibility = View.GONE
            binding.previous.visibility = View.GONE
        } else {
            binding.next.visibility = View.VISIBLE
            binding.previous.visibility = View.VISIBLE
        }
    }

    private fun next() {
        val llayout = binding.recyclerViewImages.layoutManager as LinearLayoutManager
        binding.recyclerViewImages.smoothScrollToPosition(llayout.findLastVisibleItemPosition() + 1)
    }

    private fun previous() {
        val llayout = binding.recyclerViewImages.layoutManager as LinearLayoutManager
        binding.recyclerViewImages.smoothScrollToPosition(llayout.findFirstVisibleItemPosition() - 1)
    }

    override fun onClickCrop(index: Int, image: Uri) {
        this.cropImageIndex = index
        mCropProvider.startIntent(
            uri = image,
            cropOval = mCropProvider.isCropOvalEnabled(),
            cropFreeStyle = mCropProvider.isCropFreeStyleEnabled(),
            isCamera = intent.getBooleanExtra("isCamera", false),
            outputFormat = mCropProvider.outputFormat()
        )

    }

    override fun onClickClose(image: Uri) {
        imageCropAdapter.removeImage(image)
    }

    private fun setCropImage(uri: Uri) {
//        if (mCompressionProvider.isResizeRequired(uri)) {
//            mCompressionProvider.compress(uri = uri, outputFormat = mCropProvider.outputFormat())
//        }
        cropImageIndex?.let {
            imageCropAdapter.updateItem(cropImageIndex!!, uri)
        }

//        if (mCompressionProvider.isResizeRequired(uri)) {
//            mCompressionProvider.compress(uri = uri, outputFormat = mCropProvider.outputFormat())
//        } else {
//            setResult(uri)
//        }
    }

    private fun setResult(uri: Uri) {
        val intent = Intent()
        intent.data = uri
        intent.putExtra(ImagePicker.EXTRA_FILE_PATH, uri.path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setMultipleImageResult(uris: ArrayList<Uri>) {
        val intent = Intent()
        intent.putExtra(ImagePicker.MULTIPLE_FILES_PATH, uris)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}