package com.github.drjacky.imagepicker.adapter

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.drjacky.imagepicker.databinding.ImagepickerLayoutRecyclerviewItemCropBinding

class ImageCropAdapter(
    val activity: Activity,
    val images: ArrayList<Uri>,
    private val listener: CropListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ImagepickerLayoutRecyclerviewItemCropBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
        return ImageCropHolder(view)

    }

    interface CropListener {
        fun onClickCrop(index: Int, image: Uri)
        fun onClickClose(image: Uri)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageCropHolder).bind(images[position], position, isLoading, listener)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setLoading(loading: Boolean) {
        if (this.isLoading != loading) {
            this.isLoading = loading
            notifyDataSetChanged()
        }
    }

    fun updateItem(index: Int, uri: Uri) {
        images[index] = uri
        notifyItemChanged(index)
//        if (images.any { it.id == image.id }) {
//            val removeItem = images.single { it.id == image.id }
//            val removeIndex = images.indexOf(removeItem)
//            images.set(removeIndex, Uri)
//            notifyItemChanged(removeIndex)
//        }
    }

    fun removeImage(image: Uri) {
        val index = images.indexOf(image)
        images.removeAt(index)
        notifyItemRemoved(index)
    }

    class ImageCropHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ImagepickerLayoutRecyclerviewItemCropBinding.bind(itemView)
        fun bind(image: Uri, index: Int, isLoading: Boolean, listener: CropListener) {
//            itemView.imgCrop.setImageUri(
//                Uri.fromFile(File(image.path)),
//                Uri.fromFile(File.createTempFile(img,".$ext"))
//            )
            // val imgFile = File(image.path)
            //  itemView.imgCrop.setImageURI(Uri.fromFile(imgFile))
            binding.imgCrop.load(image)
//            Glide.with(activity)
//                .load(imgFile)
//                .into(binding.imgCrop)
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnCrop.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnCrop.visibility = View.VISIBLE
            }
            binding.btnCrop.setOnClickListener {
                listener.onClickCrop(index, image)
            }
            binding.btnClose.setOnClickListener {
                listener.onClickClose(image)
            }
        }
    }

}
