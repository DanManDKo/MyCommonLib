package com.sprinklebit.library.presentation.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


/**
 * Created with Android Studio.
 * User: Danil Konovalenko
 * Date: 7/18/18
 * Time: 11:43 AM
 */
class CenterIconButton : AppCompatButton {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                    attrs,
                    com.sprinklebit.library.R.styleable.CenterIconButton)
            val tintColor = typedArray.getColor(
                    com.sprinklebit.library.R.styleable.CenterIconButton_cib_drawableTint,
                    -1)
            val text = typedArray.getText(
                    com.sprinklebit.library.R.styleable.CenterIconButton_cib_text)
            val iconId = typedArray.getResourceId(
                    com.sprinklebit.library.R.styleable.CenterIconButton_cib_drawable, -1)

            if (iconId != -1 && text != null) {
                val buttonLabel = SpannableString(" $text")
                var icon = ContextCompat.getDrawable(context, iconId)

                if (tintColor != -1) {
                    icon = DrawableCompat.wrap(icon!!).mutate()
                    DrawableCompat.setTint(icon, tintColor)
                }

                icon!!.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
                val imageSpan = ImageSpan(icon, ImageSpan.ALIGN_BOTTOM)
                buttonLabel.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                this.text = buttonLabel
            }

            typedArray.recycle()
        }
    }

}