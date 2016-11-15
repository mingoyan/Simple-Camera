package com.simplemobiletools.camera

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Camera
import android.os.Environment

class Config(context: Context) {
    private val mPrefs: SharedPreferences

    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    init {
        mPrefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
    }

    var isFirstRun: Boolean
        get() = mPrefs.getBoolean(IS_FIRST_RUN, true)
        set(firstRun) = mPrefs.edit().putBoolean(IS_FIRST_RUN, firstRun).apply()

    var isDarkTheme: Boolean
        get() = mPrefs.getBoolean(IS_DARK_THEME, false)
        set(isDarkTheme) = mPrefs.edit().putBoolean(IS_DARK_THEME, isDarkTheme).apply()

    var savePhotosFolder: String
        get() = mPrefs.getString(SAVE_PHOTOS, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
        set(path) = mPrefs.edit().putString(SAVE_PHOTOS, path).apply()

    var forceRatioEnabled: Boolean
        get() = mPrefs.getBoolean(FORCE_RATIO, true)
        set(enabled) = mPrefs.edit().putBoolean(FORCE_RATIO, enabled).apply()

    // todo: delete this
    val maxResolution: Int
        get() = mPrefs.getInt(MAX_RESOLUTION, -1)

    var maxPhotoResolution: Int
        get() = mPrefs.getInt(MAX_PHOTO_RESOLUTION, oldDefaultResolution)
        set(maxRes) = mPrefs.edit().putInt(MAX_PHOTO_RESOLUTION, maxRes).apply()

    private val oldDefaultResolution: Int
        get() {
            return when (maxResolution) {
                1 -> EIGHT_MPX
                2 -> 0
                else -> FIVE_MPX
            }
        }

    var maxVideoResolution: Int
        get() {
            val maxRes = mPrefs.getInt(MAX_VIDEO_RESOLUTION, P720)
            return when (maxRes) {
                0 -> P480
                2 -> P1080
                else -> P720
            }
        }
        set(maxRes) = mPrefs.edit().putInt(MAX_VIDEO_RESOLUTION, maxRes).apply()

    var isSoundEnabled: Boolean
        get() = mPrefs.getBoolean(SOUND, true)
        set(enabled) = mPrefs.edit().putBoolean(SOUND, enabled).apply()

    var lastUsedCamera: Int
        get() = mPrefs.getInt(LAST_USED_CAMERA, Camera.CameraInfo.CAMERA_FACING_BACK)
        set(cameraId) = mPrefs.edit().putInt(LAST_USED_CAMERA, cameraId).apply()

    var lastFlashlightState: Boolean
        get() = mPrefs.getBoolean(LAST_FLASHLIGHT_STATE, false)
        set(enabled) = mPrefs.edit().putBoolean(LAST_FLASHLIGHT_STATE, enabled).apply()

    var treeUri: String
        get() = mPrefs.getString(TREE_URI, "")
        set(uri) = mPrefs.edit().putString(TREE_URI, uri).apply()
}