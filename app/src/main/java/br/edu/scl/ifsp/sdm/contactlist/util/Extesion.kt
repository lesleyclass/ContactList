package br.edu.scl.ifsp.sdm.contactlist.util

import android.content.Intent
import android.os.Parcelable
import androidx.core.os.BuildCompat

@Suppress("DEPRECATION")
@OptIn(BuildCompat.PrereleaseSdkCheck::class)
fun <T : Parcelable> Intent.getParcelableExtraCompat(name: String?, clazz: Class<T>): T? {
    return if (BuildCompat.isAtLeastT()) {
        getParcelableExtra(name, clazz)
    } else {
        getParcelableExtra(name)
    }
}