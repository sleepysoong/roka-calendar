package com.sleepysoong.rokacalendar.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleepysoong.rokacalendar.model.ServiceProgress
import java.time.LocalDate

private val BlueBackground = Color(0xFF2962FF)
private val DarkBlueTrack = Color(0xFF1E4BD8)
private val DarkText = Color(0xFF0F172A)

@Composable
fun RokaStickerScreen(
    enlistDate: LocalDate,
    dischargeDate: LocalDate,
    progress: ServiceProgress?,
    previewBitmap: Bitmap?,
    validationMessage: String?,
    onEnlistDateClick: () -> Unit,
    onDischargeDateClick: () -> Unit,
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

            if (progress != null) {
                ProgressPreviewCard(
                    progress = progress,
                    previewBitmap = previewBitmap,
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
private fun ProgressPreviewCard(
    progress: ServiceProgress,
    previewBitmap: Bitmap?,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BlueBackground),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "전역",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                    Text(
                        text = progress.dischargeDate.toKoreanText(),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                    )
                }
                Text(
                    text = "D-${progress.remainingDays}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkBlueTrack),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.progressRatio.coerceIn(0f, 1f))
                        .height(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White),
                )
                Text(
                    text = "${progress.progressPercentText}%",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    color = DarkText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (previewBitmap != null) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "이미지 미리보기",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f)
                        .clip(RoundedCornerShape(8.dp)),
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = "진행률 스티커 미리보기",
                )
            }
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
