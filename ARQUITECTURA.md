# 🏗️ Diagrama de Arquitectura MVVM - primerLabCompose

## 1. Arquitectura General

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         ANDROID APPLICATION                             │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌──────────────────────────┐          ┌──────────────────────────┐   │
│  │   MainActivity           │          │ TaskDetailsActivity      │   │
│  │   (Jetpack Compose)      │          │ (Traditional XML)        │   │
│  ├──────────────────────────┤          ├──────────────────────────┤   │
│  │  - setContent { }        │          │ - ViewBinding habilitado │   │
│  │  - CalendarScreen()      │          │ - onCreate()             │   │
│  │  - BottomNavBar          │          │ - setupListeners()       │   │
│  │  - FAB button (azul)     │          │ - observeUiState()       │   │
│  └──────────────────────────┘          └──────────────────────────┘   │
│           │                                     │                     │
│           │ composable calls                    │ inflate()           │
│           ▼                                     ▼                     │
│  ┌──────────────────────────┐          ┌──────────────────────────┐   │
│  │    CalendarScreen()      │          │ activity_task_details.  │   │
│  │                          │          │ xml                      │   │
│  │  - Calendar Grid         │          │                          │   │
│  │  - TaskItem List         │          │ - ConstraintLayout       │   │
│  │  - clickable             │          │ - Header + Buttons       │   │
│  │    → Intent              │          │ - NestedScrollView       │   │
│  │      TaskDetailsActivity │          │ - Cards                  │   │
│  └──────────────────────────┘          └──────────────────────────┘   │
│           │                                     │                     │
│           │ StateFlow.collect()                 │ binding.<id>        │
│           │                                     │                     │
│           ▼                                     ▼                     │
│  ┌──────────────────────────┐          ┌──────────────────────────┐   │
│  │ CalendarViewModel        │          │ TaskDetailsViewModel     │   │
│  ├──────────────────────────┤          ├──────────────────────────┤   │
│  │ - uiState:               │          │ - uiState:               │   │
│  │   StateFlow<CalendarUI   │          │   StateFlow<TaskDetails  │   │
│  │   State>                 │          │   UiState>               │   │
│  │ - tasks: List<Task>      │          │ - navigateBack:          │   │
│  │ - selectedDay            │          │   LiveData<Boolean>      │   │
│  │ - currentMonth           │          │ - taskDetail: TaskDetail │   │
│  │                          │          │ - subTasks: List<Sub     │   │
│  │ Methods:                 │          │   Task>                  │   │
│  │ + onTaskToggle()         │          │                          │   │
│  │ + onDayClick()           │          │ Methods:                 │   │
│  │ + onNextMonth()          │          │ + loadTaskDetails()      │   │
│  │ + onNavItemSelected()    │          │ + markAsCompleted()      │   │
│  └──────────────────────────┘          │ + toggleSubTask()        │   │
│           │                            │ + addSubTask()           │   │
│           │ reads/writes               └──────────────────────────┘   │
│           ▼                                     ▲                     │
│  ┌──────────────────────────┐                  │                     │
│  │    Data Layer            │ queries/updates  │                     │
│  ├──────────────────────────┤                  │                     │
│  │ - Mock Data (current)    │──────────────────┘                     │
│  │ - Room DB (future)       │                                        │
│  │ - API/Repository (future)│                                        │
│  └──────────────────────────┘                                        │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Flujo de Datos (Data Flow)

### Pantalla Principal (MainActivity)

```
┌─────────────────┐
│ User Opens App  │
└────────┬────────┘
         │
         ▼
┌──────────────────────────┐
│ MainActivity.onCreate()   │
│ - setContent { }         │
│ - Create CalendarScreen  │
└────────┬─────────────────┘
         │
         ▼
┌──────────────────────────┐
│ CalendarScreen()         │
│ - viewModel: by viewModels() ◄─────────┐
│ - uiState.collectAsState()             │
└────────┬─────────────────┘             │
         │                               │
         ▼                               │
┌──────────────────────────┐             │
│ LazyColumn (Task List)   │             │
│  - TaskItem(              │            │
│      task,                │            │
│      onToggle             │            │
│  )                        │            │
└────────┬─────────────────┘             │
         │ click                         │
         ▼                               │
┌──────────────────────────┐             │
│ TaskItem.clickable()     │             │
│ - Intent(context,        │             │
│    TaskDetailsActivity)  │             │
│ - putExtra(TASK_ID)      │             │
│ - startActivity()        │             │
└──────────────────────────┘             │
                                         │
                    ┌────────────────────┘
                    │ init { by viewModels() }
                    │
                    ▼
            ┌──────────────────────────┐
            │ CalendarViewModel        │
            │                          │
            │ _uiState = MutableState  │
            │   Flow<CalendarUiState>  │
            │                          │
            │ loadMockData() {         │
            │   tasks = [              │
            │     Task(1, "..."),      │
            │     Task(2, "..."),      │
            │     Task(3, "...") ◄─── LOW Priority (FIXED)
            │   ]                      │
            │ }                        │
            └──────────────────────────┘
```

### Pantalla de Detalles (TaskDetailsActivity)

```
┌──────────────────────┐
│ TaskItem.click()     │ (from MainActivity)
│ Intent(...TaskID=1)  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ TaskDetailsActivity  │
│ onCreate()           │
│ - taskId = getIntent │
│   Extra(TASK_ID)     │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ viewModel =          │
│ by viewModels()      │
│ (crea instancia)     │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ viewModel.load      │
│ TaskDetails(taskId) │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ TaskDetailsViewModel │
│ .loadTaskDetails(1)  │
│   getMockTaskData(1) │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ _uiState.update {    │
│   taskDetail =       │
│   TaskDetail(        │
│     id=1,            │
│     title="...",     │
│     priority=HIGH,   │
│     subTasks=[...],  │
│     ...              │
│   )                  │
│ }                    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ observeUiState()     │
│                      │
│ lifecycleScope.      │
│ launch {             │
│   uiState.collect {  │
│     updateUI()       │
│   }                  │
│ }                    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ UI Update:           │
│ - binding.taskTitle  │
│   .text = title      │
│ - binding.chipPriority
│   .text = priority   │
│ - updateSubTasksUI() │
│ ...                  │
└──────────────────────┘

User Interactions:
┌──────────────────────┐
│ btnMarkCompleted     │
│ .setOnClickListener  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ viewModel.           │
│ markAsCompleted()    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ _uiState.update {    │
│   taskDetail.copy(   │
│     isCompleted=true │
│   )                  │
│ }                    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ observeUiState()     │
│ actualiza binding    │
│ y finish()           │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Back a MainActivity  │
│ (back stack)         │
└──────────────────────┘
```

---

## 3. Estado (State Management)

### CalendarUiState

```
┌────────────────────────────────────────┐
│         CalendarUiState                │
├────────────────────────────────────────┤
│ + selectedView: CalendarView           │
│   = {Day, Week, Month}                 │
│                                        │
│ + currentMonth: MonthYear              │
│   - month: Int (0-11)                  │
│   - year: Int                          │
│                                        │
│ + selectedDay: SimpleDate?             │
│   - day: Int                           │
│   - month: Int                         │
│   - year: Int                          │
│                                        │
│ + tasks: List<Task>                    │
│   ├─ Task(id=1, "Project...", HIGH)    │
│   ├─ Task(id=2, "Classes...", MEDIUM)  │
│   └─ Task(id=3, "No fat...", LOW) ◄─ FIXED
│                                        │
│ + selectedNavItem: BottomNavItem        │
│   = {TASKS, SCHEDULE, PROFILE, ...}    │
│                                        │
│ + dotsOnDays: Set<Int>                 │
│   = {8, 12, 15, 22, 25, 29}            │
└────────────────────────────────────────┘

        │ StateFlow
        │ collect
        ▼
    CalendarScreen
    (Recomposición)
```

### TaskDetailsUiState

```
┌────────────────────────────────────────┐
│       TaskDetailsUiState               │
├────────────────────────────────────────┤
│ + taskDetail: TaskDetail?              │
│   - id: Int                            │
│   - title: String                      │
│   - priority: Priority {HIGH, MED, LOW}│
│   - status: String                     │
│   - category: String                   │
│   - schedule: String                   │
│   - dueDate: String                    │
│   - subTasks: List<SubTask>            │
│   - notes: String                      │
│   - isCompleted: Boolean               │
│                                        │
│ + isLoading: Boolean                   │
│ + errorMessage: String?                │
│ + isCompleted: Boolean                 │
└────────────────────────────────────────┘

        │ StateFlow
        │ collect
        ▼
    Activity UI Update
    (binding.<id>.text)
```

---

## 4. Navegación (Navigation)

```
┌─────────────────────────────────────────────────────────────┐
│                    Back Stack                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [MainActivity] ◄─────────────── (cuando abre app)         │
│       │                                                     │
│       │ click en Task                                       │
│       │ Intent(..., TaskDetailsActivity)                   │
│       │ putExtra(EXTRA_TASK_ID, 1)                         │
│       │ startActivity()                                    │
│       ▼                                                     │
│  [MainActivity] [TaskDetailsActivity] ◄─ (click en tarea)  │
│                                                             │
│       click en Back button                                 │
│       viewModel.goBack()                                   │
│       finish()                                             │
│       │                                                     │
│       ▼                                                     │
│  [MainActivity] ◄──────────────── (vuelve atrás)           │
│                                                             │
│  (press device back)                                        │
│  finish()                                                   │
│  │                                                          │
│  ▼                                                          │
│  [App closes]                                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Intent Details

```
┌──────────────────────────────────────┐
│  Intent Estructura                   │
├──────────────────────────────────────┤
│                                      │
│  Intent(                             │
│    context = MainActivity,           │
│    targetActivity = TaskDetailsAct   │
│  )                                   │
│  .apply {                            │
│    putExtra(                         │
│      "task_id",  // EXTRA_TASK_ID   │
│      taskId      // valor (1, 2, 3) │
│    )                                 │
│  }                                   │
│                                      │
│  context.startActivity(intent)       │
│                                      │
│  ──────────────────────────────────  │
│                                      │
│  TaskDetailsActivity.onCreate():     │
│  val taskId = intent.getIntExtra(    │
│    TaskDetailsActivity.EXTRA_TASK_ID,│
│    1  // default value               │
│  )                                   │
│  viewModel.loadTaskDetails(taskId)   │
│                                      │
└──────────────────────────────────────┘
```

---

## 5. ViewBinding Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      XML Layout                             │
│            activity_task_details.xml                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  <Button android:id="@+id/btnMarkCompleted" ... />         │
│  <Button android:id="@+id/btnEdit" ... />                  │
│  <Button android:id="@+id/btnDelete" ... />                │
│  <ImageButton android:id="@+id/btnBack" ... />             │
│  ...                                                        │
│                                                             │
└──────────────────────┬──────────────────────────────────────┘
                       │ Gradle build (buildFeatures.viewBinding = true)
                       │
                       ▼
        ┌──────────────────────────────────┐
        │  ActivityTaskDetailsBinding       │
        │  (Auto-generated)                 │
        │                                  │
        │  public final Button              │
        │    btnMarkCompleted               │
        │                                  │
        │  public final Button btnEdit      │
        │  public final Button btnDelete    │
        │  public final ImageButton btnBack │
        │                                  │
        │  static inflate(                 │
        │    LayoutInflater inflater       │
        │  )                               │
        │                                  │
        └────────────────┬─────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│            TaskDetailsActivity                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  private lateinit var binding:                             │
│    ActivityTaskDetailsBinding                              │
│                                                             │
│  override fun onCreate() {                                 │
│    binding = ActivityTaskDetailsBinding.inflate(           │
│      layoutInflater                                        │
│    )                                                       │
│    setContentView(binding.root)                           │
│                                                             │
│    // Acceso sin findViewById                              │
│    binding.btnMarkCompleted.setOnClickListener { }         │
│    binding.btnEdit.text = getString(R.string.edit)         │
│    binding.taskTitle.text = title                          │
│    ...                                                     │
│  }                                                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 6. Reactive Flow (Coroutines + StateFlow)

```
┌──────────────────────────────────────────────────────────────┐
│  TaskDetailsActivity.observeUiState()                        │
└──────────────┬───────────────────────────────────────────────┘
               │
               ▼
        ┌──────────────────────────────────┐
        │ lifecycleScope.launch {           │
        │   viewModel.uiState.collect {    │
        │     uiState ->                   │
        │                                  │
        │     // Update UI                 │
        │     binding.taskTitle.text =     │
        │       uiState.taskDetail?.title  │
        │     binding.taskCategory.text =  │
        │       uiState.taskDetail?.       │
        │       category                   │
        │     updateSubTasksUI(...)        │
        │   }                              │
        │ }                                │
        └──────────┬───────────────────────┘
                   │ collector
                   │
                   ▼
        ┌──────────────────────────────────┐
        │ StateFlow<TaskDetailsUiState>    │
        │ (from TaskDetailsViewModel)      │
        │                                  │
        │ val _uiState = Mutable           │
        │   StateFlow<TaskDetailsUiState>  │
        │                                  │
        │ val uiState = _uiState           │
        │   .asStateFlow()                 │
        │                                  │
        │ fun markAsCompleted() {          │
        │   _uiState.update { current ->   │
        │     current.copy(                │
        │       isCompleted = true         │
        │     )                            │
        │   }                              │
        │ } ──────────┬────────────────────┘
        │             │ update triggers
        │             │
        │             ▼
        │     ┌──────────────────┐
        │     │ Recompose       │
        │     │ Activity        │
        │     │ UI Update       │
        │     └──────────────────┘
        │
        └─────────────────────────────────┘
```

---

## 7. UI Componentes Hierarchy

```
TaskDetailsActivity (ViewGroup: ConstraintLayout)
│
├─ Header (ConstraintLayout)
│  ├─ ImageButton: btnBack
│  ├─ TextView: titleHeader ("Task Details")
│  └─ ImageButton: btnMenu
│
├─ Divider (View)
│
├─ NestedScrollView
│  └─ LinearLayout (vertical)
│     ├─ LinearLayout: badgesContainer
│     │  ├─ Chip: chipPriority ("HIGH PRIORITY")
│     │  └─ Chip: chipStatus ("IN PROGRESS")
│     │
│     ├─ TextView: taskTitle ("Finalize Project Proposal")
│     │
│     ├─ LinearLayout: categoryContainer
│     │  ├─ TextView: emoji "📋"
│     │  └─ TextView: taskCategory ("Product Design")
│     │
│     ├─ CardView: scheduleCard
│     │  └─ LinearLayout
│     │     ├─ FrameLayout: iconContainer ("🕐")
│     │     └─ LinearLayout
│     │        ├─ TextView: label ("SCHEDULE / HORARIO")
│     │        └─ TextView: taskSchedule ("10:00 AM - 12:00 PM")
│     │
│     ├─ CardView: dueDateCard
│     │  └─ LinearLayout (similar a scheduleCard)
│     │
│     ├─ LinearLayout: subTasksSection
│     │  ├─ LinearLayout: header
│     │  │  ├─ TextView: label ("SUB-TASKS")
│     │  │  └─ TextView: subTasksProgress ("2/4 Completed")
│     │  │
│     │  ├─ LinearLayout: subTasksContainer
│     │  │  └─ CheckBox (x4)
│     │  │     ├─ "Draft executive summary" (checked)
│     │  │     ├─ "Compile market research data" (checked)
│     │  │     ├─ "Review budget with finance team" (unchecked)
│     │  │     └─ "Final proofreading and formatting" (unchecked)
│     │  │
│     │  └─ Button: btnAddSubTask ("+ Add Sub-task")
│     │
│     └─ LinearLayout: notesSection
│        ├─ TextView: label ("NOTES")
│        └─ TextView: taskNotes ("Ensure the color palette...")
│
└─ LinearLayout: bottomButtonsContainer
   ├─ Button: btnMarkCompleted
   │  (Color: indigo_600, width: match_parent)
   │
   └─ LinearLayout: actionsRow (horizontal)
      ├─ Button: btnEdit (width: 50%, outline)
      └─ Button: btnDelete (width: 50%, outline, red text)
```

---

## 8. Color & Theme

```
┌──────────────────────────────────────────────────────────┐
│            Material Design 3 Palette                     │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  PRIMARY (Indigo)                                        │
│  ├─ indigo_700  #4338CA ◄── FAB color                  │
│  ├─ indigo_600  #4F46E5 ◄── Buttons, primary text      │
│  ├─ indigo_100  #EEF2FF ◄── Badge background (HIGH)    │
│  └─ indigo_50   #F0F4FF ◄── Light backgrounds          │
│                                                          │
│  NEUTRAL (Slate)                                         │
│  ├─ slate_900   #0F172A ◄── Main text                  │
│  ├─ slate_700   #334155 ◄── Secondary text, LOW badge  │
│  ├─ slate_600   #475569 ◄── Labels                     │
│  ├─ slate_500   #64748B ◄── Hint text                  │
│  ├─ slate_200   #E2E8F0 ◄── Borders, outlines          │
│  ├─ slate_100   #F1F5F9 ◄── Card backgrounds           │
│  └─ slate_50    #F8FAFC ◄── App background             │
│                                                          │
│  ACCENT (Cyan)                                           │
│  ├─ cyan_600    #0891B2 ◄── Secondary actions          │
│  └─ cyan_50     #ECFDF5 ◄── Badge (MEDIUM)             │
│                                                          │
│  DANGER (Red)                                            │
│  └─ red_500     #EF4444 ◄── Delete button               │
│                                                          │
│  SUCCESS (Green)                                         │
│  ├─ green_check #10B981 ◄── Checkmarks                 │
│  ├─ green_done  #D1FAE5 ◄── Completed background       │
│  └─ green_text  #065F46 ◄── Completed text             │
│                                                          │
└──────────────────────────────────────────────────────────┘

Aplicación en UI:
├─ FAB: indigo_700 ✅
├─ "Mark as Completed": indigo_600 ✅
├─ "Edit": indigo_600 ✅
├─ "Delete": red_500 ✅
├─ Task title: slate_900 ✅
├─ Labels: slate_600 ✅
├─ Completed subtask: green_check ✅
├─ Badge HIGH: indigo_100 ✅
├─ Badge MEDIUM: cyan_50 ✅
├─ Badge LOW: slate_100 + slate_700 text ✅ (FIXED)
└─ Card background: indigo_50 ✅
```

---

## 9. Dependencias Graph

```
┌─────────────────────────────────────────────────────────┐
│              build.gradle.kts                           │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Compose Dependencies:                                  │
│  ├─ androidx.activity:activity-compose:1.9.x           │
│  ├─ androidx.compose.ui:*                              │
│  └─ androidx.compose.material3:*                       │
│                                                         │
│  Lifecycle & ViewModel:                                │
│  ├─ androidx.lifecycle:lifecycle-runtime-ktx:2.8.x     │
│  ├─ androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.x   │
│  ├─ androidx.lifecycle:lifecycle-viewmodel-compose:    │
│  │  2.8.x                                              │
│  └─ androidx.lifecycle:lifecycle-livedata-ktx:2.8.x    │
│                                                         │
│  Activity & AppCompat:                                 │
│  ├─ androidx.activity:activity-ktx:1.9.x ◄─ NUEVO     │
│  └─ androidx.appcompat:appcompat:1.7.x ◄─ NUEVO       │
│                                                         │
│  Core:                                                  │
│  └─ androidx.core:core-ktx:1.13.x                      │
│                                                         │
│  ViewBinding (habilitado en buildFeatures):            │
│  └─ Generado automáticamente ◄─ NUEVO                 │
│                                                         │
└─────────────────────────────────────────────────────────┘

Relaciones:
├─ TaskDetailsActivity
│  ├─ requiere: androidx.appcompat:appcompat
│  ├─ requiere: androidx.activity:activity-ktx
│  ├─ requiere: ViewBinding (build feature)
│  ├─ usa: TaskDetailsViewModel
│  └─ observa: StateFlow (lifecycle-viewmodel-ktx)
│
├─ TaskDetailsViewModel
│  ├─ extiende: androidx.lifecycle.ViewModel
│  ├─ usa: StateFlow (lifecycle-viewmodel-ktx)
│  └─ usa: LiveData (lifecycle-livedata-ktx)
│
└─ MainActivity
   ├─ extiende: ComponentActivity (activity-compose)
   └─ usa: CalendarViewModel (lifecycle-viewmodel-compose)
```

---

## 10. File Organization

```
primerLabCompose/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/primerlabcompose/
│   │       │   ├── MainActivity.kt ◄─ ACTUALIZADO
│   │       │   │   └── CalendarScreen Composable
│   │       │   │   └── BottomNavigationBar
│   │       │   │   └── TaskItem
│   │       │   │   └── PriorityBadge
│   │       │   │
│   │       │   ├── TaskDetailsActivity.kt ◄─ NUEVO
│   │       │   │   └── setupListeners()
│   │       │   │   └── observeUiState()
│   │       │   │   └── updateSubTasksUI()
│   │       │   │
│   │       │   └── viewmodel/
│   │       │       ├── CalendarViewModel.kt ◄─ EXISTENTE
│   │       │       │   └── MutableStateFlow<CalendarUiState>
│   │       │       │
│   │       │       └── TaskDetailsViewModel.kt ◄─ NUEVO
│   │       │           ├── MutableStateFlow<TaskDetailsUiState>
│   │       │           ├── MutableLiveData<navigateBack>
│   │       │           └── Mock data (Task 1, 2, 3)
│   │       │
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_task_details.xml ◄─ NUEVO
│   │       │   │       └── ConstraintLayout + NestedScrollView
│   │       │   │
│   │       │   ├── drawable/
│   │       │   │   ├── bg_icon_circle.xml ◄─ NUEVO
│   │       │   │   ├── bg_button_primary.xml ◄─ NUEVO
│   │       │   │   ├── bg_button_outline.xml ◄─ NUEVO
│   │       │   │   ├── ic_more_vert.xml ◄─ NUEVO
│   │       │   │   ├── ic_edit.xml ◄─ NUEVO
│   │       │   │   └── ic_delete.xml ◄─ NUEVO
│   │       │   │
│   │       │   └── values/
│   │       │       ├── colors.xml ◄─ ACTUALIZADO
│   │       │       │   └── +15 colores nuevos
│   │       │       │
│   │       │       ├── strings.xml ◄─ ACTUALIZADO
│   │       │       │   └── +11 strings nuevos
│   │       │       │
│   │       │       └── themes.xml
│   │       │
│   │       └── AndroidManifest.xml ◄─ ACTUALIZADO
│   │           ├── TaskDetailsActivity
│   │           └── EditTaskActivity (placeholder)
│   │
│   └── build.gradle.kts ◄─ ACTUALIZADO
│       ├── viewBinding = true
│       └── +3 dependencias nuevas
│
├── .gitignore ◄─ ACTUALIZADO (68 líneas)
│
├── RESUMEN_COMPLETO.md ◄─ NUEVO
├── GUIA_RAPIDA.md ◄─ NUEVO
├── INDICE_DE_CAMBIOS.md ◄─ NUEVO
├── TASK_DETAILS_IMPLEMENTATION.md ◄─ NUEVO
└── ARQUITECTURA.md (este archivo) ◄─ NUEVO
```

---

**Diagrama Generado:** 2026-03-26
**Versión:** 1.0
**Arquitectura:** MVVM + Jetpack Compose + ViewBinding

