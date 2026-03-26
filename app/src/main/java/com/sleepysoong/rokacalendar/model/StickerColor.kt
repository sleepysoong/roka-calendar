package com.sleepysoong.rokacalendar.model

import android.graphics.Color

enum class StickerColor(
    val displayName: String,
    val backgroundColor: Int,
    val trackColor: Int,
) {
    BLUE(
        displayName = "파랑",
        backgroundColor = Color.parseColor("#2962FF"),
        trackColor = Color.parseColor("#1E4BD8"),
    ),
    RED(
        displayName = "빨강",
        backgroundColor = Color.parseColor("#D32F2F"),
        trackColor = Color.parseColor("#B71C1C"),
    ),
    GREEN(
        displayName = "초록",
        backgroundColor = Color.parseColor("#388E3C"),
        trackColor = Color.parseColor("#1B5E20"),
    ),
    YELLOW(
        displayName = "노랑",
        backgroundColor = Color.parseColor("#F9A825"),
        trackColor = Color.parseColor("#F57F17"),
    ),
    PINK(
        displayName = "분홍",
        backgroundColor = Color.parseColor("#E91E63"),
        trackColor = Color.parseColor("#C2185B"),
    ),
    ORANGE(
        displayName = "주황",
        backgroundColor = Color.parseColor("#FF5722"),
        trackColor = Color.parseColor("#E64A19"),
    ),
    PURPLE(
        displayName = "보라",
        backgroundColor = Color.parseColor("#7B1FA2"),
        trackColor = Color.parseColor("#6A1B9A"),
    ),
    TEAL(
        displayName = "청록",
        backgroundColor = Color.parseColor("#00796B"),
        trackColor = Color.parseColor("#004D40"),
    ),
}
