# Estructura ViewModel - Migración Completa

## 📁 Estructura del Proyecto

```
com.example.primerlabcompose/
├── viewmodel/
│   └── CalendarViewModel.kt    # ViewModel con toda la lógica de negocio
├── ui/
│   └── theme/                  # Temas de la aplicación
└── MainActivity.kt             # Solo componentes UI (Composables)
```

## 🎯 Cambios Realizados

### 1. **CalendarViewModel.kt** (NUEVO)
Contiene:
- **Data Models**: `MonthYear`, `SimpleDate`, `Task`, `Priority`
- **Enums**: `CalendarView`, `BottomNavItem`
- **UI State**: `CalendarUiState` - estado único que contiene todo
- **ViewModel**: `CalendarViewModel` - maneja toda la lógica

#### Funciones del ViewModel:
- `onViewSelected(view)` - Cambia entre vistas Day/Week/Month
- `onPreviousMonth()` - Navega al mes anterior
- `onNextMonth()` - Navega al mes siguiente
- `onDayClick(date)` - Selecciona/deselecciona un día
- `onTaskToggle(taskId)` - Marca/desmarca tarea como completada
- `onNavItemSelected(item)` - Cambia pestaña del menú inferior

### 2. **MainActivity.kt** (MODIFICADO)
Cambios:
- ✅ Eliminadas todas las data classes y enums (ahora en ViewModel)
- ✅ `CalendarScreen()` ahora recibe un ViewModel como parámetro
- ✅ Usa `collectAsState()` para observar cambios del estado
- ✅ Todas las variables `remember { mutableStateOf() }` eliminadas
- ✅ Todas las interacciones delegadas al ViewModel

#### Antes:
```kotlin
@Composable
fun CalendarScreen() {
    var selectedView by remember { mutableStateOf(CalendarView.Month) }
    var currentMonth by remember { mutableStateOf(MonthYear(1, 2026)) }
    var tasks by remember { mutableStateOf(sampleTasks) }
    // ... más estado local
}
```

#### Después:
```kotlin
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    // Usa uiState.currentMonth, uiState.tasks, etc.
}
```

## 🔄 Flujo de Datos

```
UI (MainActivity) → ViewModel → StateFlow → UI actualizado
     ↑                                           ↓
     └─────────── collectAsState() ──────────────┘
```

1. **Usuario interactúa** con la UI (clic en botón)
2. **UI llama** a una función del ViewModel (ej: `viewModel.onNextMonth()`)
3. **ViewModel actualiza** el `_uiState` usando `.update { }`
4. **StateFlow emite** el nuevo estado
5. **UI se recompone** automáticamente con los nuevos datos

## 📊 Ventajas de esta Arquitectura

✅ **Separación de responsabilidades**: UI solo renderiza, ViewModel maneja lógica
✅ **Estado predecible**: Un solo source of truth (CalendarUiState)
✅ **Testeable**: Puedes testear el ViewModel sin UI
✅ **Sobrevive cambios de configuración**: El ViewModel persiste rotaciones
✅ **Mantenible**: Código más organizado y fácil de modificar
✅ **Escalable**: Fácil agregar nuevas funcionalidades

## 🚀 Próximos Pasos (Opcional)

- [ ] Agregar Repository pattern para manejar datos
- [ ] Implementar Room Database para persistencia
- [ ] Agregar casos de uso (UseCase) para lógica compleja
- [ ] Implementar navegación con Navigation Compose
- [ ] Agregar tests unitarios para el ViewModel

## 📝 Notas

- Se usa `StateFlow` para el estado reactivo
- `collectAsState()` convierte Flow en State observable por Compose
- El ViewModel se crea automáticamente con `viewModel()` de Compose
- El estado es inmutable (usando data classes con copy())

