package tech.blur.armory.data.providers

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream

class ResourceProvider(private val context: Context) {
    fun openRawResource(@RawRes id: Int): InputStream = context.resources.openRawResource(id)
}