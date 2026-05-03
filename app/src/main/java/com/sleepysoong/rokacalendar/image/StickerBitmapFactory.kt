package com.sleepysoong.rokacalendar.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.sleepysoong.rokacalendar.model.ServiceProgress
import com.sleepysoong.rokacalendar.model.StickerColor

object StickerBitmapFactory {
    private const val DEFAULT_WIDTH = 1600
    private const val DEFAULT_HEIGHT = 400

    private val WHITE = Color.WHITE
    private val WHITE_ALPHA = Color.argb(180, 255, 255, 255)
    private val DARK_TEXT = Color.parseColor("#0F172A")

    // Pre-allocated Paint objects to avoid garbage collection churn during 10Hz updates
    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        textSize = DEFAULT_HEIGHT * 0.18f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE_ALPHA
        textSize = DEFAULT_HEIGHT * 0.13f
    }

    private val dDayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        textSize = DEFAULT_HEIGHT * 0.18f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
    }

    private val percentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = DARK_TEXT
        textSize = (DEFAULT_HEIGHT * 0.35f) * 0.55f
        typeface = Typeface.MONOSPACE
    }

    fun create(
        progress: ServiceProgress,
        decimalPlaces: Int = 5,
        stickerColor: StickerColor = StickerColor.BLUE,
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(stickerColor.backgroundColor)

        val padding = width * 0.04f
        val contentWidth = width - padding * 2

        val headerY = height * 0.35f

        // Adjust text sizes if dimensions change from default
        if (height != DEFAULT_HEIGHT) {
            titlePaint.textSize = height * 0.18f
            datePaint.textSize = height * 0.13f
            dDayPaint.textSize = height * 0.18f
            percentPaint.textSize = (height * 0.35f) * 0.55f
        }

        canvas.drawText("전역", padding, headerY, titlePaint)

        val titleWidth = titlePaint.measureText("전역")
        val dateText = "${progress.dischargeDate.year}년 ${progress.dischargeDate.monthValue.toString().padStart(2, '0')}월 ${progress.dischargeDate.dayOfMonth.toString().padStart(2, '0')}일"
        canvas.drawText(dateText, padding + titleWidth + width * 0.02f, headerY, datePaint)

        val remainingDays = progress.totalDays - progress.elapsedDays
        val dDayText = "D-$remainingDays"
        val dDayWidth = dDayPaint.measureText(dDayText)
        canvas.drawText(dDayText, width - padding - dDayWidth, headerY, dDayPaint)

        val barTop = height * 0.50f
        val barBottom = height * 0.85f
        val barHeight = barBottom - barTop
        val barRadius = barHeight * 0.5f

        trackPaint.color = stickerColor.trackColor
        val trackRect = RectF(padding, barTop, width - padding, barBottom)
        canvas.drawRoundRect(trackRect, barRadius, barRadius, trackPaint)

        val fillWidth = contentWidth * progress.progressRatio
        if (fillWidth > 0f) {
            val fillRect = RectF(padding, barTop, padding + fillWidth, barBottom)
            canvas.drawRoundRect(fillRect, barRadius, barRadius, fillPaint)
        }

        val percentText = "${progress.getProgressPercentText(decimalPlaces)}%"
        val textY = barTop + (barHeight / 2f) + (percentPaint.textSize / 3f)
        canvas.drawText(percentText, padding + width * 0.02f, textY, percentPaint)

        return bitmap
    }
}
