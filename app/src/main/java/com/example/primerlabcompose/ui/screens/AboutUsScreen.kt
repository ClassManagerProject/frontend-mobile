package com.example.primerlabcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.primerlabcompose.ui.theme.AppColors
import com.example.primerlabcompose.viewmodel.AboutUsViewModel
import com.example.primerlabcompose.viewmodel.DeveloperInfo
import com.example.primerlabcompose.viewmodel.TechItem

/**
 * Pantalla "Acerca de nosotros".
 * Accesible desde el botón "Ajustes" del menú inferior.
 * Sigue el patrón MVVM con AboutUsViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    navController: NavController,
    viewModel: AboutUsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Acerca de",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = Color.White,
                    titleContentColor          = AppColors.TextDark,
                    navigationIconContentColor = AppColors.TextDark
                )
            )
        },
        containerColor = Color(0xFFF8F9FC)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header con gradiente ──────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(AppColors.Indigo600, Color(0xFF6366F1))
                            )
                        )
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Icono de la app
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = uiState.appName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Versión ${uiState.appVersion}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // ── Descripción ───────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                SectionCard {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(icon = Icons.Default.Info, title = "Descripción")
                        Text(
                            text = uiState.appDescription,
                            fontSize = 14.sp,
                            color = AppColors.SlateGray,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            // ── Tecnologías utilizadas ────────────────────────────────────────
            item {
                Spacer(Modifier.height(12.dp))
                SectionCard {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionTitle(icon = Icons.Default.Build, title = "Tecnologías")
                        Spacer(Modifier.height(12.dp))
                        uiState.technologies.forEach { tech ->
                            TechRow(tech = tech)
                            if (tech != uiState.technologies.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color(0xFFE2E8F0)
                                )
                            }
                        }
                    }
                }
            }

            // ── Equipo de desarrollo ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(12.dp))
                SectionCard {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionTitle(icon = Icons.Default.Person, title = "Desarrolladores")
                        Spacer(Modifier.height(12.dp))
                        uiState.developers.forEach { dev ->
                            DeveloperCard(dev = dev)
                        }
                    }
                }
            }

            // ── Pie de página ─────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(12.dp))
                SectionCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Hecho con amor en ${uiState.year}",
                            fontSize = 13.sp,
                            color = AppColors.SlateGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ── Componentes privados de la pantalla ───────────────────────────────────────

/** Tarjeta contenedora de sección */
@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        content()
    }
}

/** Título de sección con icono */
@Composable
private fun SectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AppColors.Indigo100),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AppColors.Indigo600, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
    }
}

/** Fila de tecnología */
@Composable
private fun TechRow(tech: TechItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AppColors.Indigo600)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(tech.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextDark)
            Text(tech.description, fontSize = 12.sp, color = AppColors.SlateGray)
        }
    }
}

/** Tarjeta de desarrollador */
@Composable
private fun DeveloperCard(dev: DeveloperInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(AppColors.Indigo100)
                .border(2.dp, AppColors.Indigo600.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dev.name.first().toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Indigo600
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(dev.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextDark)
            Text(dev.role, fontSize = 12.sp, color = AppColors.Indigo600, fontWeight = FontWeight.Medium)
            Text(dev.institution, fontSize = 12.sp, color = AppColors.SlateGray)
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = AppColors.SlateGray,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(dev.email, fontSize = 11.sp, color = AppColors.SlateGray)
            }
        }
    }
}

