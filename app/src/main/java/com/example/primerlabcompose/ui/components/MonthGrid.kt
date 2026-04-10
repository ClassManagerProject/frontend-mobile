package com.example.primerlabcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primerlabcompose.data.model.MonthYear
import com.example.primerlabcompose.data.model.SimpleDate
import com.example.primerlabcompose.ui.theme.AppColors

// ── Cuadrícula mensual ────────────────────────────────────────────────────────

@Composable
fun MonthGrid(
    monthYear: MonthYear,
    selectedDay: SimpleDate?,
    dotsOnDays: Set<Int>,
    onDayClick: (SimpleDate) -> Unit
) {
    val startOffset = monthYear.firstDayOfWeek()
    val daysInMonth = monthYear.daysInMonth()
    val rows = (startOffset + daysInMonth + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val dayNumber = row * 7 + col - startOffset + 1
                    if (dayNumber < 1 || dayNumber > daysInMonth) {
                        Spacer(Modifier.weight(1f))
                    } else {
                        val date = SimpleDate(dayNumber, monthYear.month, monthYear.year)
                        DayCell(
                            day       = dayNumber,
                            isSelected = date == selectedDay,
                            hasEvent  = dayNumber in dotsOnDays,
                            modifier  = Modifier.weight(1f),
                            onClick   = { onDayClick(date) }
                        )
                    }
                }
            }
        }
    }
}

// ── Celda de día ──────────────────────────────────────────────────────────────

@Composable
fun DayCell(
    day: Int,
    isSelected: Boolean,
    hasEvent: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) AppColors.Indigo600 else Color.Transparent)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text       = day.toString(),
            fontSize   = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color      = if (isSelected) Color.White else AppColors.TextDark,
            textAlign  = TextAlign.Center
        )
        if (hasEvent) {
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White.copy(alpha = 0.7f) else AppColors.DotColor)
            )
        }
    }
}

