package com.sleepysoong.rokacalendar.ui

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.sleepysoong.rokacalendar.model.ServiceProgress
import com.sleepysoong.rokacalendar.model.StickerColor
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun RokaStickerScreen(
    enlistDate: LocalDate,
    dischargeDate: LocalDate,
    targetDate: LocalDate,
    progress: ServiceProgress?,
    decimalPlaces: Int,
    selectedColor: StickerColor,
    previewBitmap: Bitmap?,
    validationMessage: String?,
    onEnlistDateClick: () -> Unit,
    onDischargeDateClick: () -> Unit,
    onTargetDateClick: () -> Unit,
    onDecimalPlacesChange: (Int) -> Unit,
    onColorChange: (StickerColor) -> Unit,
    onCopyClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val accentColor by animateColorAsState(
        targetValue = Color(selectedColor.backgroundColor),
        label = "accentColor",
    )
    val secondaryAccent by animateColorAsState(
        targetValue = Color(selectedColor.trackColor),
        label = "secondaryAccent",
    )
    val backdrop = rememberLayerBackdrop()

    Box(modifier = Modifier.fillMaxSize()) {
        LiquidGlassBackground(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop),
            accentColor = accentColor,
            secondaryAccent = secondaryAccent,
        )

        Scaffold(
            containerColor = Color.Transparent,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                HeroPanel(
                    backdrop = backdrop,
                    progress = progress,
                    decimalPlaces = decimalPlaces,
                    accentColor = accentColor,
                    secondaryAccent = secondaryAccent,
                    validationMessage = validationMessage,
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        DatePickerCard(
                            modifier = Modifier.weight(1f),
                            backdrop = backdrop,
                            label = "입대일",
                            date = enlistDate,
                            onClick = onEnlistDateClick,
                        )
                        DatePickerCard(
                            modifier = Modifier.weight(1f),
                            backdrop = backdrop,
                            label = "전역일",
                            date = dischargeDate,
                            onClick = onDischargeDateClick,
                        )
                    }

                    DatePickerCard(
                        modifier = Modifier.fillMaxWidth(),
                        backdrop = backdrop,
                        label = "기준일 (스티커에 표시될 날짜)",
                        date = targetDate,
                        onClick = onTargetDateClick,
                    )
                }

                ControlsPanel(
                    backdrop = backdrop,
                    decimalPlaces = decimalPlaces,
                    selectedColor = selectedColor,
                    accentColor = accentColor,
                    onDecimalPlacesChange = onDecimalPlacesChange,
                    onColorChange = onColorChange,
                )

                PreviewPanel(
                    backdrop = backdrop,
                    previewBitmap = previewBitmap,
                    validationMessage = validationMessage,
                )

                ActionButtons(
                    backdrop = backdrop,
                    enabled = previewBitmap != null,
                    accentColor = accentColor,
                    onCopyClick = onCopyClick,
                    onSaveClick = onSaveClick,
                )
            }
        }
    }
}

@Composable
private fun HeroPanel(
    backdrop: com.kyant.backdrop.Backdrop,
    progress: ServiceProgress?,
    decimalPlaces: Int,
    accentColor: Color,
    secondaryAccent: Color,
    validationMessage: String?,
) {
    GlassPanel(
        backdrop = backdrop,
        shape = RoundedCornerShape(36.dp),
        tintColor = accentColor.copy(alpha = 0.16f),
    ) {
        GlassTag(text = "ROKA Calendar")
        Text(
            text = "전역 스티커를 더 입체적으로",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "배경은 흐리고, 컨트롤은 떠 있게. 리퀴드 글래스 구조로 날짜 설정과 저장 동선을 다시 정리했습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        if (progress != null) {
            Text(
                text = "${progress.getProgressPercentText(decimalPlaces)}%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "${progress.elapsedDays}일째 진행 중 · 남은 기간 ${progress.remainingDays}일",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
            )
            GlassProgressBar(
                progress = progress.progressRatio,
                accentColor = accentColor,
                secondaryAccent = secondaryAccent,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatColumn(
                    modifier = Modifier.weight(1f),
                    label = "전체 복무",
                    value = "${progress.totalDays}일",
                )
                StatColumn(
                    modifier = Modifier.weight(1f),
                    label = "남은 기간",
                    value = "${progress.remainingDays}일",
                )
                StatColumn(
                    modifier = Modifier.weight(1f),
                    label = "기준일",
                    value = progress.now.toLocalDate().toKoreanText(),
                )
            }
        } else {
            Text(
                text = validationMessage ?: "날짜를 확인해 주세요.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun StatColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DatePickerCard(
    modifier: Modifier = Modifier,
    backdrop: com.kyant.backdrop.Backdrop,
    label: String,
    date: LocalDate,
    onClick: () -> Unit,
) {
    GlassPanel(
        backdrop = backdrop,
        modifier = modifier,
        tintColor = Color.White.copy(alpha = 0.14f),
        onClick = onClick,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )
        Text(
            text = date.toKoreanText(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "탭해서 날짜 변경",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
        )
    }
}

@Composable
private fun ControlsPanel(
    backdrop: com.kyant.backdrop.Backdrop,
    decimalPlaces: Int,
    selectedColor: StickerColor,
    accentColor: Color,
    onDecimalPlacesChange: (Int) -> Unit,
    onColorChange: (StickerColor) -> Unit,
) {
    GlassPanel(
        backdrop = backdrop,
        tintColor = accentColor.copy(alpha = 0.12f),
    ) {
        Text(
            text = "색상과 정밀도",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "스티커 배경 색과 퍼센트 소수점 자릿수를 동시에 조정합니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(StickerColor.entries) { color ->
                ColorItem(
                    backdrop = backdrop,
                    color = color,
                    isSelected = color == selectedColor,
                    onClick = { onColorChange(color) },
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "소수점 자리수",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            GlassTag(text = "$decimalPlaces 자리")
        }
        Slider(
            value = decimalPlaces.toFloat(),
            onValueChange = { onDecimalPlacesChange(it.roundToInt()) },
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f),
            ),
        )
    }
}

@Composable
private fun ColorItem(
    backdrop: com.kyant.backdrop.Backdrop,
    color: StickerColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    GlassPanel(
        backdrop = backdrop,
        modifier = Modifier.size(width = 72.dp, height = 96.dp),
        shape = RoundedCornerShape(24.dp),
        tintColor = Color(color.backgroundColor).copy(alpha = 0.22f),
        paddingValues = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 12.dp),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(color.backgroundColor))
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        Color.White.copy(alpha = 0.55f)
                    },
                    shape = CircleShape,
                )
                .align(Alignment.CenterHorizontally),
        )
        Text(
            text = color.displayName,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PreviewPanel(
    backdrop: com.kyant.backdrop.Backdrop,
    previewBitmap: Bitmap?,
    validationMessage: String?,
) {
    GlassPanel(
        backdrop = backdrop,
        tintColor = Color.White.copy(alpha = 0.14f),
    ) {
        Text(
            text = "스티커 프리뷰",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "출력 이미지는 4:1 비율로 유지됩니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )
        if (previewBitmap != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.Black.copy(alpha = 0.08f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.32f),
                        shape = RoundedCornerShape(26.dp),
                    )
                    .padding(10.dp),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f)
                        .clip(RoundedCornerShape(20.dp)),
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = "진행률 스티커",
                )
            }
        } else {
            Text(
                text = validationMessage ?: "날짜를 다시 확인해 주세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    backdrop: com.kyant.backdrop.Backdrop,
    enabled: Boolean,
    accentColor: Color,
    onCopyClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GlassActionButton(
            text = "복사",
            backdrop = backdrop,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            tintColor = Color.White.copy(alpha = 0.18f),
            onClick = onCopyClick,
        )
        GlassActionButton(
            text = "저장",
            backdrop = backdrop,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            tintColor = accentColor.copy(alpha = 0.32f),
            contentColor = Color.White,
            onClick = onSaveClick,
        )
    }
}

private fun LocalDate.toKoreanText(): String = "${year}년 ${monthValue}월 ${dayOfMonth}일"
