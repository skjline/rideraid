package com.skjline.rideraid.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class WindowsUtils {
    private WindowsUtils() {
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(wrapDrawable, context.getColor(color));
        } else {
            DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));
        }

        item.setIcon(wrapDrawable);
    }

    public static void lockScreenDim(Window window, boolean lock) {
        if (lock) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
