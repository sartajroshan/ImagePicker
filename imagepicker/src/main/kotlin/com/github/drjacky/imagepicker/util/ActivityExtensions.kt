package com.github.drjacky.imagepicker.util

import android.app.Activity
import android.content.Intent
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.ImagePickerActivity

/**
 * User has cancelled the task
 */
fun Activity.setResultCancel() {
    setResult(Activity.RESULT_CANCELED, ImagePickerActivity.getCancelledIntent(this))
    finish()
}

/**
 * Error occurred while processing image
 *
 * @param message Error Message
 */
fun Activity.setError(message: String) {
    val intent = Intent()
    intent.putExtra(ImagePicker.EXTRA_ERROR, message)
    setResult(ImagePicker.RESULT_ERROR, intent)
    finish()
}
