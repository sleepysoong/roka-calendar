package com.sleepysoong.rokacalendar.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.sleepysoong.rokacalendar.model.ServiceProgress

object StickerBitmapFactory {
    private const val DEFAULT_WIDTH = 1600
    private const val DEFAULT_HEIGHT = 400

    // 색상 상수
    private val BACKGROUND_COLOR = Color.parseColor("#2962FF")
    private val TRACK_COLOR = Color.parseColor("#1E4BD8")
    private val WHITE = Color.WHITE
    private val WHITE_ALPHA = Color.argb(180, 255, 255, 255)
    private val DARK_BLUE = Color.parseColor("#0F172A")

    fun create(
        progress: ServiceProgress,
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 배경 (파란색)
        canvas.drawColor(BACKGROUND_COLOR)

        val padding = width * 0.04f
        val contentWidth = width - padding * 2

        // === 상단 헤더 영역 ===
        val headerY = height * 0.35f

        // "전역" 타이틀 (왼쪽, 굵은 흰색)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            textSize = height * 0.18f
            typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText("전역", padding, headerY, titlePaint)

        // 전역 날짜 (전역 옆, 반투명 흰색)
        val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE_ALPHA
            textSize = height * 0.13f
        }
        val titleWidth = titlePaint.measureText("전역")
        val dateText = "${progress.dischargeDate.year}년 ${progress.dischargeDate.monthValue.toString().padStart(2, '0')}월 ${progress.dischargeDate.dayOfMonth.toString().padStart(2, '0')}일"
        canvas.drawText(dateText, padding + titleWidth + width * 0.02f, headerY, datePaint)

        // D-Day (오른쪽 끝)
        val dDayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE_ALPHA
            textSize = height * 0.13f
        }
        val remainingDays = progress.totalDays - progress.elapsedDays
        val dDayText = "D-$remainingDays"
        val dDayWidth = dDayPaint.measureText(dDayText)
        canvas.drawText(dDayText, width - padding - dDayWidth, headerY, dDayPaint)

        // === 프로그레스 바 영역 ===
        val barTop = height * 0.50f
        val barBottom = height * 0.85f
        val barHeight = barBottom - barTop
        val barRadius = barHeight * 0.2f

        // 트랙 (배경 바 - 어두운 파란색)
        val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = TRACK_COLOR
        }
        val trackRect = RectF(padding, barTop, width - padding, barBottom)
        canvas.drawRoundRect(trackRect, barRadius, barRadius, trackPaint)

        // 인디케이터 (채워지는 부분 - 흰색)
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
        }
        val fillWidth = contentWidth * progress.progressRatio
        if (fillWidth > 0f) {
            val fillRect = RectF(padding, barTop, padding + fillWidth, barBottom)
            canvas.drawRoundRect(fillRect, barRadius, barRadius, fillPaint)
        }

        // 진행률 텍스트 (바 내부 왼쪽)
        val percentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = DARK_BLUE
            textSize = barHeight * 0.55f
            typeface = Typeface.MONOSPACE
        }
        val percentText = "${progress.progressPercentText}%"
        val textY = barTop + (barHeight / 2f) + (percentPaint.textSize / 3f)
        canvas.drawText(percentText, padding + width * 0.02f, textY, percentPaint)

        return bitmap
    }
}
