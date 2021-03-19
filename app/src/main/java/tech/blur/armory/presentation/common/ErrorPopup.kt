package tech.blur.armory.presentation.common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tech.blur.armory.BuildConfig

class ErrorPopup {
    companion object {
        fun show(context: Context, title: String, text: String? = null, error: Exception? = null) {
            MaterialAlertDialogBuilder(context).apply {
                setTitle(title)
                setMessage(
                    text + if (BuildConfig.DEBUG) error?.let { "\n" + it.localizedMessage }
                        ?: "" else ""
                )
                setPositiveButton("OK") { _, _ ->  }
            }.show()
        }
    }
}