package com.sleepysoong.rokacalendar.ui

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sleepysoong.rokacalendar.image.StickerBitmapFactory
import com.sleepysoong.rokacalendar.model.ServiceProgress
import com.sleepysoong.rokacalendar.model.StickerColor
import com.sleepysoong.rokacalendar.share.StickerImageExporter
import com.sleepysoong.rokacalendar.ui.theme.RokaCalendarTheme
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RokaCalendarApp() {
    RokaCalendarTheme {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val prefs = remember { context.getSharedPreferences("roka_prefs", Context.MODE_PRIVATE) }

        var enlistDateText by rememberSaveable {
            mutableStateOf(
                prefs.getString("enlist_date", null) ?: LocalDate.now().minusMonths(3).toString()
            )
        }
        var dischargeDateText by rememberSaveable {
            mutableStateOf(
                prefs.getString("discharge_date", null) ?: LocalDate.now().plusMonths(15).toString()
            )
        }
        var targetDateText by rememberSaveable {
            mutableStateOf(LocalDate.now().toString())
        }
        var decimalPlaces by rememberSaveable { mutableIntStateOf(5) }
        var selectedColorOrdinal by rememberSaveable { mutableIntStateOf(StickerColor.BLUE.ordinal) }

        val selectedColor = StickerColor.entries[selectedColorOrdinal]

        var tick by remember { mutableLongStateOf(0L) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000L)
                tick++
            }
        }

        val enlistDate = remember(enlistDateText) { LocalDate.parse(enlistDateText) }
        val dischargeDate = remember(dischargeDateText) { LocalDate.parse(dischargeDateText) }
        val targetDate = remember(targetDateText) { LocalDate.parse(targetDateText) }

        val progress = remember(enlistDate, dischargeDate, targetDate, tick) {
            ServiceProgress.calculate(
                enlistDate = enlistDate,
                dischargeDate = dischargeDate,
                targetDate = targetDate
            )
        }
        val validationMessage = remember(enlistDate, dischargeDate, progress) {
            if (progress == null) {
                "전역일은 입대일보다 뒤여야 합니다."
            } else {
                null
            }
        }
        val previewBitmap: Bitmap? = remember(progress, tick, decimalPlaces, selectedColor) {
            progress?.let { StickerBitmapFactory.create(it, decimalPlaces, selectedColor) }
        }

        RokaStickerScreen(
            enlistDate = enlistDate,
            dischargeDate = dischargeDate,
            targetDate = targetDate,
            progress = progress,
            decimalPlaces = decimalPlaces,
            selectedColor = selectedColor,
            previewBitmap = previewBitmap,
            validationMessage = validationMessage,
            onEnlistDateClick = {
                showDatePicker(
                    context = context,
                    initialDate = enlistDate,
                ) { selectedDate ->
                    enlistDateText = selectedDate.toString()
                    prefs.edit().putString("enlist_date", enlistDateText).apply()
                }
            },
            onDischargeDateClick = {
                showDatePicker(
                    context = context,
                    initialDate = dischargeDate,
                ) { selectedDate ->
                    dischargeDateText = selectedDate.toString()
                    prefs.edit().putString("discharge_date", dischargeDateText).apply()
                }
            },
            onTargetDateClick = {
                showDatePicker(
                    context = context,
                    initialDate = targetDate,
                ) { selectedDate ->
                    targetDateText = selectedDate.toString()
                }
            },
            onDecimalPlacesChange = { decimalPlaces = it },
            onColorChange = { selectedColorOrdinal = it.ordinal },
            onCopyClick = {
                val bitmap = previewBitmap ?: return@RokaStickerScreen
                coroutineScope.launch {
                    val result = StickerImageExporter.copyToClipboard(context, bitmap)
                    val message = if (result.isSuccess) {
                        "이미지를 클립보드에 복사했습니다."
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
    context: Context,
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
