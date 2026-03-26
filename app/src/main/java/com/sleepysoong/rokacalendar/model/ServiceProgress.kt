package com.sleepysoong.rokacalendar.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

data class ServiceProgress(
    val enlistDate: LocalDate,
    val dischargeDate: LocalDate,
    val now: LocalDateTime,
    val elapsedDays: Long,
    val totalDays: Long,
    val progressRatio: Float,
    val progressPercent: Double,
) {
    val remainingDays: Long
        get() = totalDays - elapsedDays

    fun getProgressPercentText(decimalPlaces: Int): String {
        return String.format(Locale.US, "%.${decimalPlaces}f", progressPercent)
    }

    companion object {
        fun calculate(
            enlistDate: LocalDate,
            dischargeDate: LocalDate,
        ): ServiceProgress? {
            if (!dischargeDate.isAfter(enlistDate)) {
                return null
            }

            val now = LocalDateTime.now()
            val enlistDateTime = enlistDate.atStartOfDay()
            val dischargeDateTime = dischargeDate.atStartOfDay()

            val totalMillis = ChronoUnit.MILLIS.between(enlistDateTime, dischargeDateTime).coerceAtLeast(1)
            val elapsedMillis = ChronoUnit.MILLIS.between(enlistDateTime, now).coerceIn(0, totalMillis)

            val totalDays = ChronoUnit.DAYS.between(enlistDate, dischargeDate).coerceAtLeast(1)
            val elapsedDays = ChronoUnit.DAYS.between(enlistDate, now.toLocalDate()).coerceIn(0, totalDays)

            val ratio = elapsedMillis.toDouble() / totalMillis.toDouble()
            val percent = ratio * 100.0

            return ServiceProgress(
                enlistDate = enlistDate,
                dischargeDate = dischargeDate,
                now = now,
                elapsedDays = elapsedDays,
                totalDays = totalDays,
                progressRatio = ratio.toFloat(),
                progressPercent = percent,
            )
        }
    }
}
