package com.simplemobiletools.camera

import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.simplemobiletools.camera.Preview.Companion.config
import com.simplemobiletools.camera.activities.MainActivity
import com.simplemobiletools.camera.extensions.getOutputMediaFile
import com.simplemobiletools.commons.extensions.getFileDocument
import com.simplemobiletools.commons.extensions.needsStupidWritePermissions
import com.simplemobiletools.commons.extensions.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.ref.WeakReference

class PhotoProcessor(val activity: MainActivity, val uri: Uri?) : AsyncTask<ByteArray, Void, String>() {
    companion object {
        private val TAG = PhotoProcessor::class.java.simpleName
        private var mActivity: WeakReference<MainActivity>? = null
    }

    init {
        mActivity = WeakReference(activity)
    }

    override fun doInBackground(vararg params: ByteArray): String {
        var fos: OutputStream? = null
        val path: String
        try {
            if (uri != null) {
                path = uri.path
            } else {
                path = activity.getOutputMediaFile(true)
            }

            if (path.isEmpty()) {
                return ""
            }

            val photoFile = File(path)
            if (activity.needsStupidWritePermissions(path)) {
                if (config.treeUri.isEmpty()) {
                    activity.runOnUiThread {
                        activity.toast(R.string.save_error_internal_storage)
                    }
                    config.savePhotosFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
                    return ""
                }
                var document = activity.getFileDocument(path, config.treeUri)
                document = document?.createFile("", path.substring(path.lastIndexOf('/') + 1))
                fos = activity.contentResolver.openOutputStream(document?.uri)
            } else {
                fos = FileOutputStream(photoFile)
            }

            val data = params[0]
            fos?.write(data)
            fos?.close()
            return photoFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "PhotoProcessor file not found: $e")
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                Log.e(TAG, "PhotoProcessor close ioexception $e")
            }
        }

        return ""
    }

    override fun onPostExecute(path: String) {
        super.onPostExecute(path)
        mActivity?.get()?.mediaSaved(path)
    }

    interface MediaSavedListener {
        fun mediaSaved(path: String)
    }
}
