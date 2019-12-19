package com.pss9.rider.common

import android.content.Context
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat

interface WindowsUtils {

    companion object {
        fun tintMenuIcon(context: Context, item: MenuItem, @ColorRes color: Int) {
            val normalDrawable = item.icon
            val wrapDrawable = DrawableCompat.wrap(normalDrawable)

            DrawableCompat.setTint(wrapDrawable, context.getColor(color))
            item.icon = wrapDrawable
        }

        fun lockScreenDim(window: Window, lock: Boolean) {
            if (lock) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
