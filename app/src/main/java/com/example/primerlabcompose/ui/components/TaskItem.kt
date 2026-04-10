package com.example.primerlabcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primerlabcompose.data.model.Task
import com.example.primerlabcompose.ui.navigation.Screen
import com.example.primerlabcompose.ui.theme.AppColors

// ── Item de tarea en la lista ─────────────────────────────────────────────────

@Composable
fun TaskItem(
    task: Task,
    navController: NavController,
    onToggle: () -> Unit
) {
    val bgColor     = if (task.isDone) AppColors.GreenDone else Color.White
    val borderColor = if (task.isDone) AppColors.GreenCheck.copy(alpha = 0.4f) else Color(0xFFE2E8F0)
    val titleColor  = if (task.isDone) AppColors.GreenText else AppColors.TextDark
    val titleDecor  = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .clickable { navController.navigate(Screen.TaskDetails.createRoute(task.id)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (task.isDone) AppColors.GreenCheck else Color.White)
                .border(
                    width = if (task.isDone) 0.dp else 2.dp,
                    color = if (task.isDone) Color.Transparent else Color(0xFFCBD5E1),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (task.isDone) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Título + badges
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                textDecoration = titleDecor
            )
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!task.isDone) PriorityBadge(task.priority) else CompletedBadge()
                if (!task.isDone && task.subtitle.isNotEmpty()) {
                    Text(task.subtitle, fontSize = 12.sp, color = AppColors.SlateGray)
                }
            }
        }
    }
}

