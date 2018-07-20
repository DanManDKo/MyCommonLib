package com.sprinklebit.library.utils

import android.content.Context
import android.util.DisplayMetrics


/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 4/15/18
 */
class SizeUtil {

    companion object {
        /**
         * This method converts dp unit to equivalent pixels, depending on device density.
         *
         * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent px equivalent to dp depending on device density
         */
        fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px A value in px (pixels) unit. Which we need to convert into db
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent dp equivalent to px value
         */
        fun convertPixelsToDp(px: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

}