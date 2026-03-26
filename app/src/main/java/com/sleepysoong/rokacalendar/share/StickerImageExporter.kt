package com.sleepysoong.rokacalendar.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object StickerImageExporter {
    suspend fun saveToGallery(context: Context, bitmap: Bitmap): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val fileName = createFileName()
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/RokaCalendar",
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: error("이미지 저장 위치를 만들지 못했습니다.")

            resolver.openOutputStream(uri)?.use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    error("PNG 저장에 실패했습니다.")
                }
            } ?: error("이미지 출력 스트림을 열지 못했습니다.")

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            fileName
        }
    }

    suspend fun copyToClipboard(context: Context, bitmap: Bitmap): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val uri = writeBitmapToCache(context, bitmap)
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newUri(
                context.contentResolver,
                "복무 진행률 이미지",
                uri,
            )
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun writeBitmapToCache(context: Context, bitmap: Bitmap): Uri {
        val directory = File(context.cacheDir, "shared_images").apply {
            mkdirs()
        }
        val file = File(directory, createFileName())
        FileOutputStream(file).use { outputStream ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                error("클립보드용 PNG 생성에 실패했습니다.")
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
    }

    private fun createFileName(): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        return "roka_progress_${timestamp}.png"
    }
}
