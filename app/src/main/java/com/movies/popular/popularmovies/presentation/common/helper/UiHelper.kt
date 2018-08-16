package com.movies.popular.popularmovies.presentation.common.helper

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.di.scope.ActivityScope
import javax.inject.Inject


/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 9/20/17
 */
@ActivityScope
class UiHelper
@Inject
constructor(private val activity: Activity) {
    private var errorToast: Toast? = null
    private var uploadingDialog: AlertDialog? = null

    fun showErrorToast(message: String?) {
        if (errorToast != null) {
            errorToast!!.cancel()
        }
        errorToast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        errorToast!!.show()
    }

    fun showUploading(show: Boolean) {
        if (show) showUploading() else hideUploading()
    }

    fun showUploading() {
        if (uploadingDialog == null) {
            uploadingDialog = AlertDialog.Builder(activity)
                    .setView(R.layout.view_loading_dialog)
                    .show()
            uploadingDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            uploadingDialog!!.setCancelable(false)
        } else {
            uploadingDialog!!.show()
        }
    }

    fun hideUploading() {
        if (uploadingDialog != null) {
            uploadingDialog!!.dismiss()
        }
    }

    fun showMessage(message: String) {
        if (errorToast != null) {
            errorToast!!.cancel()
        }
        errorToast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        errorToast!!.show()
    }

    fun showKeyBoard(view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

}
