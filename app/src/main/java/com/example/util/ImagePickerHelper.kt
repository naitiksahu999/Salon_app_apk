package com.example.util

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

/**
 * Handles picking photos directly from device Gallery or Files,
 * copying them permanently into the application's internal files directory,
 * and returning a stable file path / file URI that Coil and Room can store and display.
 */
object ImagePickerHelper {
    private const val TAG = "ImagePickerHelper"

    fun copyUriToInternalStorage(context: Context, sourceUri: Uri): String? {
        return try {
            val imagesDir = File(context.filesDir, "salon_uploads")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val fileName = "img_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg"
            val destFile = File(imagesDir, fileName)

            val inputStream: InputStream? = context.contentResolver.openInputStream(sourceUri)
            if (inputStream == null) {
                Log.e(TAG, "Cannot open input stream for URI: $sourceUri")
                return null
            }

            val outputStream = FileOutputStream(destFile)
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            destFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error saving picked image to internal storage", e)
            null
        }
    }
}
