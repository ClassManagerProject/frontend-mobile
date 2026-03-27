# 📱 primerLabCompose - README

## 🎯 Descripción del Proyecto

**primerLabCompose** es una aplicación Android que combina:
- **UI Principal**: Jetpack Compose (CalendarScreen)
- **UI Detalles**: Traditional XML + ViewBinding
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Estado**: StateFlow + LiveData
- **Localización**: 100% en español

La app permite a usuarios gestionar tareas con calendario, ver detalles de tareas y marcar sub-tareas como completadas.

---

## ✨ Features Principales

### ✅ Calendario Interactivo
- Vista de mes con navegación
- Selección de días
- Indicadores de eventos
- Meses en español (Enero, Febrero, etc.)
- Días de semana en español (DOM, LUN, MAR, etc.)

### ✅ Gestión de Tareas
- Lista de tareas con prioridades (HIGH, MEDIUM, LOW)
- Toggle completar/incompletar tarea
- Navegación a detalles
- Colores dinámicos según estado

### ✅ Detalles de Tarea
- Información completa de la tarea
- Sub-tareas con checkboxes
- Horario y fecha de vencimiento
- Notas descriptivas
- Botones de acción (Completar, Editar, Eliminar)

### ✅ Menú Inferior (Bottom Navigation)
- Acceso a: Tareas, Calendario, Perfil, Ajustes
- Indicador visual activo

### ✅ Botón Flotante (FAB)
- Símbolo `+` en azul indigo
- Posicionado en esquina inferior derecha

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- **Android Studio** Arctic Fox o superior
- **Java 11+**
- **Emulador o Dispositivo** con API 24+

### Pasos

#### 1. Clonar/Abrir Proyecto
```bash
cd C:\Users\jcgpb\AndroidStudioProjects\primerLabCompose
```

#### 2. Sincronizar Gradle
```bash
./gradlew clean build
```

#### 3. Ejecutar en Emulador
```bash
./gradlew installDebug
# O desde Android Studio: Run → Run 'app'
```

#### 4. O Build APK
```bash
./gradlew assembleDebug
# APK en: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 Estructura del Proyecto

```
primerLabCompose/
├── app/
│   ├── src/main/java/com/example/primerlabcompose/
│   │   ├── MainActivity.kt               (Pantalla principal Compose)
│   │   ├── TaskDetailsActivity.kt        (Pantalla detalles XML)
│   │   └── viewmodel/
│   │       ├── CalendarViewModel.kt      (Lógica pantalla principal)
│   │       └── TaskDetailsViewModel.kt   (Lógica pantalla detalles)
│   │
│   └── src/main/res/
│       ├── layout/
│       │   └── activity_task_details.xml
│       ├── drawable/
│       │   └── (6 vectores para UI)
│       └── values/
│           ├── colors.xml               (15+ colores)
│           └── strings.xml              (Labels español)
│
├── build.gradle.kts                     (Dependencias)
├── settings.gradle.kts
└── gradle.properties
```

---

## 🏗️ Arquitectura MVVM

```
MainActivity (Compose)
    └─ CalendarViewModel
       └─ StateFlow<CalendarUiState>
          └─ tasks, selectedDay, currentMonth...

TaskDetailsActivity (XML)
    └─ TaskDetailsViewModel
       └─ StateFlow<TaskDetailsUiState>
          └─ taskDetail, subTasks, isCompleted...
```

### Data Flow
1. **UI** dispara evento (click, toggle)
2. **Activity** llama método en **ViewModel**
3. **ViewModel** actualiza `StateFlow`
4. **Activity** observa cambios y actualiza **UI**

---

## 📱 Pantallas

### 1. MainActivity - Calendario

```
┌────────────────────────────┐
│ 📅 Febrero 2026       🔍    │ ← Header
│ [Day][Week][Month]         │ ← Selector vista
│ ◀ Febrero ▶                │ ← Navegación mes
│                            │
│ DOM LUN MAR ... SAB        │ ← Días español
│  1•  2   3   4   5   6   7 │ ← Grid calendario
│  8   9  10  11  12• 13  14 │
│ ...                        │
│                            │
│ Mis tareas                 │ ← Tareas list
│ ☐ Task 1      HIGH         │
│ ☐ Task 2      MEDIUM       │
│ ☐ Task 3      LOW          │ ← FIXED: visible
│                            │
│ [🎯][📅][👤][⚙️]           │ ← Bottom nav
└────────────────────────────┘
```

### 2. TaskDetailsActivity - Detalles

```
┌────────────────────────────┐
│ ◀ Task Details      ⋮       │ ← Header
│ [HIGH PRIORITY] [IN PROG]   │ ← Badges
│ Finalize Project Proposal   │ ← Título
│ 📋 Product Design           │ ← Categoría
│                            │
│ ┌──────────────────────┐   │ ← Card: Horario
│ │ 🕐 SCHEDULE/HORARIO │   │
│ │ 10:00 AM - 12:00 PM │   │
│ └──────────────────────┘   │
│                            │
│ ┌──────────────────────┐   │ ← Card: Fecha
│ │ 📅 DUE DATE/FECHA   │   │
│ │ Viernes, Oct 27     │   │
│ └──────────────────────┘   │
│                            │
│ SUB-TASKS       2/4 Done   │ ← Contador
│ ☑ Subtarea 1               │
│ ☑ Subtarea 2               │
│ ☐ Subtarea 3               │
│ ☐ Subtarea 4               │
│ + Add Sub-task             │
│                            │
│ NOTES                      │
│ Descripción detallada...   │
│                            │
│ [✓ Mark as Completed]      │ ← Botón principal
│ [✏ Edit] [🗑 Delete]       │ ← Botones sec
└────────────────────────────┘
```

---

## 🎨 Colores Personalizados

| Nombre | Hex | Uso |
|--------|-----|-----|
| indigo_600 | #4F46E5 | Botones, FAB, texto principal |
| indigo_700 | #4338CA | FAB botón |
| slate_900 | #0F172A | Texto principal |
| slate_700 | #334155 | Texto secundario, badge LOW |
| red_500 | #EF4444 | Botón eliminar |
| green_check | #10B981 | Checkmark completado |

---

## 🔌 Navegación entre Pantallas

### Ir a Detalles de Tarea
```kotlin
val intent = Intent(context, TaskDetailsActivity::class.java).apply {
    putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId)
}
context.startActivity(intent)
```

### Volver a Principal
```kotlin
// Botón atrás en Activity
viewModel.goBack()  // → finish()
```

---

## 🐛 Bugs Corregidos

### Bug: Task ID=3 (LOW priority) no era visible
**Problema**: Texto gris sobre fondo gris
**Solución**: 
- Fondo: `#E2E8F0` (gris claro)
- Texto: `#334155` (gris oscuro)
- Contraste WCAG AAA ✅

---

## 📦 Dependencias Principales

```gradle
// Compose
implementation("androidx.activity:activity-compose:...")
implementation("androidx.compose.material3:...")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Activity
implementation("androidx.activity:activity-ktx:1.9.0")
implementation("androidx.appcompat:appcompat:1.7.0")

// ViewBinding (habilitado en buildFeatures)
buildFeatures { viewBinding = true }
```

---

## 🧪 Testing

### Unit Tests (ViewModel)
```bash
./gradlew testDebug
```

### UI Tests (Espresso)
```bash
./gradlew connectedAndroidTest
```

---

## 📚 Documentación

| Archivo | Propósito |
|---------|-----------|
| **RESUMEN_COMPLETO.md** | Visión general del proyecto |
| **ARQUITECTURA.md** | Diagramas MVVM y data flow |
| **GUIA_RAPIDA.md** | Instrucciones de uso y desarrollo |
| **INDICE_DE_CAMBIOS.md** | Detalle de archivos creados/modificados |
| **TASK_DETAILS_IMPLEMENTATION.md** | Análisis técnico de TaskDetails |

---

## 🚀 Próximas Mejoras

- [ ] **Sprint 2**: EditTaskActivity (editar tareas)
- [ ] **Sprint 3**: Room Database (persistencia)
- [ ] **Sprint 4**: API Integration (backend)
- [ ] **Sprint 5**: Dark Mode support
- [ ] **Sprint 6**: Push Notifications

---

## 👨‍💻 Convenciones de Código

### Nombres
- Variables: `camelCase` (ej: `taskId`)
- Clases: `PascalCase` (ej: `TaskDetailsActivity`)
- Constantes: `UPPER_CASE` (ej: `EXTRA_TASK_ID`)
- Métodos: `camelCase` (ej: `loadTaskDetails()`)

### Patrones
- ✅ MVVM (Separation of Concerns)
- ✅ StateFlow (Reactive Programming)
- ✅ ViewBinding (Type-safe)
- ✅ Coroutines (Async operations)
- ✅ Material Design 3

---

## 🐛 Troubleshooting

### Error: "ViewBinding not found"
```
Solución: En build.gradle.kts agregar:
android { buildFeatures { viewBinding = true } }
```

### Error: "TaskDetailsActivity not found"
```
Solución: Verificar AndroidManifest.xml contenga:
<activity android:name=".TaskDetailsActivity" android:exported="true" />
```

### Error: "ViewModel not initialized"
```
Solución: Usar 'by viewModels()' en Activity:
private val viewModel: TaskDetailsViewModel by viewModels()
```

---

## 📞 Soporte

Para reportar bugs o sugerir mejoras, contacta al equipo de desarrollo.

---

## 📝 Historial de Versiones

### v1.0 (26/03/2026)
- ✅ Pantalla principal con calendario Compose
- ✅ Pantalla detalles con XML + ViewBinding
- ✅ MVVM con ViewModel
- ✅ Localización español
- ✅ 15+ colores personalizados
- ✅ FAB azul indigo
- ✅ Navegación Intent bidireccional
- ✅ Bug fixes (LOW priority visibility)

---

## 📄 Licencia

Este proyecto es propiedad de [Tu Empresa/Nombre].

---

## ✅ Checklist Pre-Producción

- [x] Compilación exitosa sin errores
- [x] ViewBinding funcional
- [x] ViewModel correctamente integrado
- [x] Navegación Intent funcional
- [x] Colores accesibles (WCAG)
- [x] Strings localizados español
- [x] .gitignore configurado
- [x] Documentación completa
- [ ] Tests unitarios 100%
- [ ] Tests UI (Espresso)
- [ ] Proguard/R8 configurado
- [ ] APK firmada

---

**Bienvenido al proyecto! 🎉**

Para comenzar, ejecuta:
```bash
./gradlew clean build
```

Luego abre el proyecto en Android Studio y corre en emulador.

Happy Coding! 💻✨

