package com.sleepysoong.rokacalendar.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.math.roundToInt

private val BlueBackground = Color(0xFF2962FF)

@Composable
fun RokaStickerScreen(
    enlistDate: LocalDate,
    dischargeDate: LocalDate,
    decimalPlaces: Int,
    previewBitmap: Bitmap?,
    validationMessage: String?,
    onEnlistDateClick: () -> Unit,
    onDischargeDateClick: () -> Unit,
    onDecimalPlacesChange: (Int) -> Unit,
    onCopyClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HeaderCard()

            DateSection(
                enlistDate = enlistDate,
                dischargeDate = dischargeDate,
                onEnlistDateClick = onEnlistDateClick,
                onDischargeDateClick = onDischargeDateClick,
            )

            DecimalPlacesSlider(
                decimalPlaces = decimalPlaces,
                onDecimalPlacesChange = onDecimalPlacesChange,
            )

            if (previewBitmap != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f)
                        .clip(RoundedCornerShape(16.dp)),
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = "진행률 스티커",
                )
            } else {
                ErrorCard(message = validationMessage ?: "날짜를 확인해 주세요.")
            }

            ActionButtons(
                enabled = previewBitmap != null,
                onCopyClick = onCopyClick,
                onSaveClick = onSaveClick,
            )
        }
    }
}

@Composable
private fun HeaderCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "전역 스티커 만들기",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "입대일과 전역일을 설정하면 전역까지 진행률을 4:1 이미지로 바로 만들 수 있어요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DateSection(
    enlistDate: LocalDate,
    dischargeDate: LocalDate,
    onEnlistDateClick: () -> Unit,
    onDischargeDateClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DatePickerCard(
            modifier = Modifier.weight(1f),
            label = "입대일",
            date = enlistDate,
            onClick = onEnlistDateClick,
        )
        DatePickerCard(
            modifier = Modifier.weight(1f),
            label = "전역일",
            date = dischargeDate,
            onClick = onDischargeDateClick,
        )
    }
}

@Composable
private fun DatePickerCard(
    modifier: Modifier = Modifier,
    label: String,
    date: LocalDate,
    onClick: () -> Unit,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = date.toKoreanText(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
            ) {
                Text(text = "날짜 선택")
            }
        }
    }
}

@Composable
private fun DecimalPlacesSlider(
    decimalPlaces: Int,
    onDecimalPlacesChange: (Int) -> Unit,
) {
    Card {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "소수점 자리수",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "$decimalPlaces 자리",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Slider(
                value = decimalPlaces.toFloat(),
                onValueChange = { onDecimalPlacesChange(it.roundToInt()) },
                valueRange = 0f..20f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = BlueBackground,
                    activeTrackColor = BlueBackground,
                ),
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    enabled: Boolean,
    onCopyClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            enabled = enabled,
            onClick = onCopyClick,
        ) {
            Text(text = "복사")
        }
        Button(
            modifier = Modifier.weight(1f),
            enabled = enabled,
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(containerColor = BlueBackground),
        ) {
            Text(text = "저장")
        }
    }
}

private fun LocalDate.toKoreanText(): String = "${year}년 ${monthValue}월 ${dayOfMonth}일"
