package com.sleepysoong.rokacalendar.ui

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sleepysoong.rokacalendar.image.StickerBitmapFactory
import com.sleepysoong.rokacalendar.model.ServiceProgress
import com.sleepysoong.rokacalendar.share.StickerImageExporter
import com.sleepysoong.rokacalendar.ui.theme.RokaCalendarTheme
import java.time.LocalDate
import kotlinx.coroutines.launch

@Composable
fun RokaCalendarApp() {
    RokaCalendarTheme {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        var enlistDateText by rememberSaveable { mutableStateOf(LocalDate.now().minusMonths(3).toString()) }
        var dischargeDateText by rememberSaveable { mutableStateOf(LocalDate.now().plusMonths(15).toString()) }

        val enlistDate = remember(enlistDateText) { LocalDate.parse(enlistDateText) }
        val dischargeDate = remember(dischargeDateText) { LocalDate.parse(dischargeDateText) }
        val progress = remember(enlistDate, dischargeDate) {
            ServiceProgress.calculate(enlistDate = enlistDate, dischargeDate = dischargeDate)
        }
        val validationMessage = remember(enlistDate, dischargeDate, progress) {
            if (progress == null) {
                "전역일은 입대일보다 뒤여야 합니다."
            } else {
                null
            }
        }
        val previewBitmap: Bitmap? = remember(progress) {
            progress?.let(StickerBitmapFactory::create)
        }

        RokaStickerScreen(
            enlistDate = enlistDate,
            dischargeDate = dischargeDate,
            progress = progress,
            previewBitmap = previewBitmap,
            validationMessage = validationMessage,
            onEnlistDateClick = {
                showDatePicker(
                    context = context,
                    initialDate = enlistDate,
                ) { selectedDate ->
                    enlistDateText = selectedDate.toString()
                }
            },
            onDischargeDateClick = {
                showDatePicker(
                    context = context,
                    initialDate = dischargeDate,
                ) { selectedDate ->
                    dischargeDateText = selectedDate.toString()
                }
            },
            onCopyClick = {
                val bitmap = previewBitmap ?: return@RokaStickerScreen
                coroutineScope.launch {
                    val result = StickerImageExporter.copyToClipboard(context, bitmap)
                    val message = if (result.isSuccess) {
                        "이미지를 클립보드에 복사했습니다. 붙여넣기를 지원하는 앱에서 사용할 수 있어요."
                    } else {
                        result.exceptionOrNull()?.message ?: "이미지 복사에 실패했습니다."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            onSaveClick = {
                val bitmap = previewBitmap ?: return@RokaStickerScreen
                coroutineScope.launch {
                    val result = StickerImageExporter.saveToGallery(context, bitmap)
                    val message = if (result.isSuccess) {
                        "${result.getOrNull()} 저장 완료"
                    } else {
                        result.exceptionOrNull()?.message ?: "이미지 저장에 실패했습니다."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
        )
    }
}

private fun showDatePicker(
    context: android.content.Context,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth,
    ).show()
}
