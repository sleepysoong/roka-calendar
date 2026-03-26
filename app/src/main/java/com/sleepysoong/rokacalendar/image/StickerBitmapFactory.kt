package com.sleepysoong.rokacalendar.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.sleepysoong.rokacalendar.model.ServiceProgress
import kotlin.math.roundToInt

object StickerBitmapFactory {
    private const val DEFAULT_WIDTH = 1600
    private const val DEFAULT_HEIGHT = 400

    fun create(
        progress: ServiceProgress,
        width: Int = DEFAULT_WIDTH,
        height: Int = DEFAULT_HEIGHT,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val outerPadding = width * 0.03f
        val cardRect = RectF(
            outerPadding,
            height * 0.08f,
            width - outerPadding,
            height - (height * 0.08f),
        )
        val cardRadius = height * 0.16f

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(235, 255, 255, 255)
        }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#111827")
            textSize = height * 0.13f
            isFakeBoldText = true
        }
        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#374151")
            textSize = height * 0.10f
        }
        val captionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#6B7280")
            textSize = height * 0.075f
        }
        val barBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E5E7EB")
        }
        val barFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#22C55E")
        }

        canvas.drawRoundRect(cardRect, cardRadius, cardRadius, cardPaint)

        val titleX = cardRect.left + width * 0.05f
        val titleBaseline = cardRect.top + height * 0.20f
        canvas.drawText("전역까지 ${progress.progressPercentText}%", titleX, titleBaseline, titlePaint)

        val detailBaseline = titleBaseline + height * 0.14f
        canvas.drawText(
            "${progress.elapsedDays}일 / ${progress.totalDays}일 진행",
            titleX,
            detailBaseline,
            bodyPaint,
        )

        val periodBaseline = detailBaseline + height * 0.11f
        canvas.drawText(
            "${progress.enlistDate.toKoreanDateText()} ~ ${progress.dischargeDate.toKoreanDateText()}",
            titleX,
            periodBaseline,
            captionPaint,
        )

        val barLeft = titleX
        val barTop = cardRect.bottom - height * 0.18f
        val barRight = cardRect.right - width * 0.05f
        val barBottom = barTop + height * 0.10f
        val barRadius = (barBottom - barTop) / 2f
        val barRect = RectF(barLeft, barTop, barRight, barBottom)

        canvas.drawRoundRect(barRect, barRadius, barRadius, barBackgroundPaint)

        val fillWidth = (barRect.width() * progress.progressRatio).coerceAtLeast(0f)
        if (fillWidth > 0f) {
            val fillRect = RectF(barLeft, barTop, barLeft + fillWidth, barBottom)
            canvas.drawRoundRect(fillRect, barRadius, barRadius, barFillPaint)
        }

        return bitmap
    }

    private fun java.time.LocalDate.toKoreanDateText(): String {
        return "${year}년 ${monthValue}월 ${dayOfMonth}일"
    }
}
