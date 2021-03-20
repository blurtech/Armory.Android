package tech.blur.armory.presentation.common

import android.content.res.Resources

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
val Float.dp: Float get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

val Int.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
val Float.sp: Float get() = (this * Resources.getSystem().displayMetrics.scaledDensity)