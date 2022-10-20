package com.github.drjacky.imagepicker.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.github.drjacky.imagepicker.R
import com.github.drjacky.imagepicker.databinding.ImagepickerToolbarBinding


class
ImagePickerToolbar : RelativeLayout {

    private var titleText: TextView? = null
    private var doneText: TextView? = null
    private var selectedText: TextView? = null
    private var backImage: AppCompatImageView? = null
    private var cameraImage: AppCompatImageView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val binding = ImagepickerToolbarBinding
            .inflate(
                LayoutInflater.from(context), this
            )

        //View.inflate(context, R.layout.imagepicker_toolbar, this)
        if (isInEditMode) {
            return
        }

        titleText = binding.textToolbarTitle
        selectedText = binding.textToolbarCount
        doneText = binding.textToolbarDone
        backImage = binding.imageToolbarBack
        cameraImage = binding.imageToolbarCamera
    }

    fun config() {
        //this.config = config

        setBackgroundColor(ContextCompat.getColor(context, R.color.ucrop_color_widget_background))

        //titleText!!.text = if (config.isFolderMode) config.folderTitle else config.imageTitle
        titleText!!.setTextColor(ContextCompat.getColor(context, R.color.ucrop_color_widget_text))

        doneText!!.text = resources.getString(R.string.action_done)
        doneText!!.setTextColor(ContextCompat.getColor(context, R.color.ucrop_color_widget_text))

        backImage!!.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.ucrop_color_active_controls_color
            )
        )

        cameraImage!!.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.ucrop_color_active_controls_color
            )
        )
        cameraImage!!.visibility = View.GONE

        doneText!!.visibility = View.GONE

        selectedText!!.text = "0"
    }

    fun setTitle(title: String) {
        titleText!!.text = title
    }


    fun showDoneButton(isShow: Boolean) {
        doneText!!.visibility = if (isShow) View.VISIBLE else View.GONE
        selectedText!!.visibility = if (isShow) View.VISIBLE else View.GONE
        cameraImage!!.visibility = if (!isShow) View.VISIBLE else View.GONE
    }

    fun showOnlyDoneButton(isShow: Boolean) {
        doneText!!.visibility = if (isShow) View.VISIBLE else View.GONE
        selectedText!!.visibility = View.GONE
        cameraImage!!.visibility = View.GONE
    }

    fun updateSelectedCount(count: Int) {
        selectedText!!.text = "$count"
    }

    fun setOnBackClickListener(clickListener: View.OnClickListener) {
        backImage!!.setOnClickListener(clickListener)
    }

    fun setOnCameraClickListener(clickListener: View.OnClickListener) {
        cameraImage!!.setOnClickListener(clickListener)
    }

    fun setOnDoneClickListener(clickListener: View.OnClickListener) {
        doneText!!.setOnClickListener(clickListener)
    }

}