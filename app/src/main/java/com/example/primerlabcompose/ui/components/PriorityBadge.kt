package com.example.primerlabcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerlabcompose.data.model.Priority
import com.example.primerlabcompose.ui.theme.AppColors

// ── Badge de prioridad ─────────────────────────────────────────────────────────

@Composable
fun PriorityBadge(priority: Priority) {
    val (label, bg, fg) = when (priority) {
        Priority.HIGH   -> Triple("HIGH PRIORITY", AppColors.Indigo100,      AppColors.PriorityHigh)
        Priority.MEDIUM -> Triple("MEDIUM",         Color(0xFFE0F2FE),        AppColors.PriorityMed)
        Priority.LOW    -> Triple("LOW",             Color(0xFFE2E8F0),        Color(0xFF334155))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = fg, letterSpacing = 0.3.sp)
    }
}

// ── Badge de completado ────────────────────────────────────────────────────────

@Composable
fun CompletedBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AppColors.GreenCheck.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            "COMPLETED",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.GreenText,
            letterSpacing = 0.3.sp
        )
    }
}

