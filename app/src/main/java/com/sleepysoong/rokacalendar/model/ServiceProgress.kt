package com.sleepysoong.rokacalendar.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

data class ServiceProgress(
    val enlistDate: LocalDate,
    val dischargeDate: LocalDate,
    val today: LocalDate,
    val elapsedDays: Long,
    val totalDays: Long,
    val progressRatio: Float,
    val progressPercent: Double,
) {
    val progressPercentText: String
        get() = String.format(Locale.US, "%.5f", progressPercent)

    companion object {
        fun calculate(
            enlistDate: LocalDate,
            dischargeDate: LocalDate,
            today: LocalDate = LocalDate.now(),
        ): ServiceProgress? {
            if (!dischargeDate.isAfter(enlistDate)) {
                return null
            }

            val totalDays = ChronoUnit.DAYS.between(enlistDate, dischargeDate).coerceAtLeast(1)
            val elapsedDays = ChronoUnit.DAYS.between(enlistDate, today).coerceIn(0, totalDays)
            val ratio = elapsedDays.toFloat() / totalDays.toFloat()
            val percent = ratio * 100.0

            return ServiceProgress(
                enlistDate = enlistDate,
                dischargeDate = dischargeDate,
                today = today,
                elapsedDays = elapsedDays,
                totalDays = totalDays,
                progressRatio = ratio,
                progressPercent = percent,
            )
        }
    }
}
