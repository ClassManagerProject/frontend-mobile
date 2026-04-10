package com.example.primerlabcompose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.primerlabcompose.data.model.BottomNavItem
import com.example.primerlabcompose.ui.theme.AppColors

// ── Barra de navegación inferior ─────────────────────────────────────────────

@Composable
fun BottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        BottomNavItem.entries.forEach { item ->
            val isSelected = item == selectedItem
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavItem.TASKS    -> Icons.Default.Check
                            BottomNavItem.SCHEDULE -> Icons.Default.DateRange
                            BottomNavItem.PROFILE  -> Icons.Default.AccountCircle
                            BottomNavItem.SETTINGS -> Icons.Default.Settings
                        },
                        contentDescription = item.name
                    )
                },
                label = {
                    Text(
                        text = when (item) {
                            BottomNavItem.TASKS    -> "Tareas"
                            BottomNavItem.SCHEDULE -> "Calendario"
                            BottomNavItem.PROFILE  -> "Perfil"
                            BottomNavItem.SETTINGS -> "Ajustes"
                        },
                        fontSize = 12.sp
                    )
                },
                selected = isSelected,
                onClick = { onItemSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = AppColors.Indigo600,
                    selectedTextColor   = AppColors.Indigo600,
                    unselectedIconColor = AppColors.SlateGray,
                    unselectedTextColor = AppColors.SlateGray,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}


