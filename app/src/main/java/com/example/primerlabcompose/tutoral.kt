package com.example.primerlabcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Colores del Tutorial ──────────────────────────────────────────────────
val TutorialPrimary = Color(0xFF2D1FD1)
val TutorialBg = Color(0xFFF8F9FC)
val TutorialTextTitle = Color(0xFF0F172A)
val TutorialTextDesc = Color(0xFF64748B)
val TutorialIndicatorInactive = Color(0xFFCBD5E1)

@Composable
fun TutorialStep(
    title: String,
    description: String,
    stepIndex: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onBack: (() -> Unit)? = null,
    isLast: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = TutorialBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fila superior: Atrás y Saltar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TutorialPrimary.copy(alpha = 0.1f))
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = TutorialPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Text(
                    text = "Saltar",
                    modifier = Modifier.clickable { onSkip() },
                    color = TutorialPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Imagen / Ilustración (Placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // Aquí iría el componente Image con el recurso correspondiente
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Imagen Paso ${stepIndex + 1}", color = TutorialTextDesc)
                    Text("(Ilustración de tutorial)", fontSize = 12.sp, color = TutorialIndicatorInactive)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = TutorialTextTitle,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = description,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = TutorialTextDesc,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Indicadores de progreso (Dots)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isActive = index == stepIndex
                    Box(
                        modifier = Modifier
                            .size(width = if (isActive) 24.dp else 8.dp, height = 8.dp)
                            .clip(CircleShape)
                            .background(if (isActive) TutorialPrimary else TutorialIndicatorInactive)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de acción principal
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TutorialPrimary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isLast) "Get Started" else "Siguiente",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (!isLast) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Activities para cada paso del tutorial ────────────────────────────────────

class TutorialActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialStep(
                title = "Organiza tu día al máximo",
                description = "Gestiona tus pendientes, prioriza lo importante y alcanza tus objetivos diarios de forma sencilla.",
                stepIndex = 0,
                onNext = {
                    startActivity(Intent(this, TutorialActivity2::class.java))
                },
                onSkip = {
                    goToMain()
                }
            )
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}

class TutorialActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialStep(
                title = "Tu horario bajo control",
                description = "Visualiza tus tareas en una vista de calendario interactiva. Arrastra, suelta y organiza tus días de forma intuitiva para que nunca pierdas una entrega.",
                stepIndex = 1,
                onNext = {
                    startActivity(Intent(this, TutorialActivity3::class.java))
                },
                onSkip = {
                    goToMain()
                }
            )
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}

class TutorialActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialStep(
                title = "Alcanza tus metas",
                description = "Visualiza tus avances diarios y mantente motivado viendo cómo completas cada una de tus tareas.",
                stepIndex = 2,
                isLast = true,
                onNext = {
                    goToMain()
                },
                onSkip = {
                    goToMain()
                },
                onBack = {
                    finish() // Regresa a la actividad anterior (paso 2)
                }
            )
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
