package com.adamfils.acronimyser.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun Context.showDialog(
        title: String,
        message: String,
        positiveButton: String,
    ) {
        val builder = AlertDialog.Builder(this).setTitle(title)
            .setMessage(message).setPositiveButton(positiveButton) { dialog, _ ->
            dialog.cancel()
        }
        builder.create().show()
    }

}