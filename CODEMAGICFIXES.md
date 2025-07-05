# Codemagic Build Fixes Documentation

**Project:** Advanced Snake Game  
**Platform:** Android (Kotlin + Jetpack Compose)  
**CI/CD:** Codemagic.io  
**Repository:** https://github.com/jdhinman/AdvancedSnakeGame  

---

## Document Maintenance Rules

1. **Always add timestamps** in UTC format (YYYY-MM-DD HH:MM UTC) for each entry
2. **Include commit hash** for each fix when applicable
3. **Document the complete error message** and the exact solution applied
4. **Add research links** and references used to solve the issue
5. **Keep entries in reverse chronological order** (newest first)
6. **Update this document immediately** after fixing any Codemagic build issue
7. **Include the specific file paths and line numbers** affected by each fix
8. **Document any prevention strategies** discovered for future builds

---

## Build Fixes History

### Improvement #4: Enhanced Swipe Controls and Progressive Speed System
**Date:** 2025-01-05 15:45 UTC  
**Commit:** 8a9d707  

**Issues Fixed:**
- Unreliable swipe detection requiring multiple attempts
- Game starting too fast (150ms) for beginners
- No difficulty progression or speed scaling

**Root Cause:**  
1. **Swipe Detection**: Used continuous `onDrag` callback with threshold applied to individual drag deltas rather than cumulative distance, causing multiple rapid triggers and poor gesture recognition
2. **Game Speed**: Fixed 150ms timing with no progression made game too difficult initially and provided no challenge scaling

**Solution:**  
**Swipe Controls Enhancement:**
- Replaced delta-based detection with cumulative drag distance tracking
- Added proper gesture lifecycle with `onDragStart`, `onDragEnd` state management
- Implemented 200ms debouncing to prevent rapid-fire direction changes
- Added adaptive thresholds based on screen size (8% width, 6% height minimum)
- Included 20dp dead zone to prevent accidental direction changes
- Added directional bias (60% secondary threshold) for clearer gesture recognition

**Progressive Speed System:**
- Changed initial speed from 150ms to 350ms (beginner-friendly)
- Implemented speed progression: decreases by 15ms every 3 points scored
- Added minimum speed cap at 120ms to maintain playability
- Enhanced ViewModel with direction change validation and debouncing

**Files Modified:**
- `app/src/main/java/com/advancedsnake/presentation/game/GameScreen.kt` (lines 112-171)
- `app/src/main/java/com/advancedsnake/presentation/game/GameViewModel.kt` (lines 38-93)

**Technical Implementation:**
```kotlin
// Improved gesture detection with state management
var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
var hasDirectionChanged by remember { mutableStateOf(false) }

// Dynamic speed calculation
private fun calculateGameSpeed(score: Int): Long {
    val baseSpeed = INITIAL_GAME_SPEED // 350ms
    val speedDecrease = (score / 3) * SPEED_DECREASE_PER_LEVEL // 15ms
    return (baseSpeed - speedDecrease).coerceAtLeast(MIN_GAME_SPEED) // 120ms min
}
```

**Result:** Responsive, reliable swipe controls with appropriate difficulty progression from beginner-friendly to challenging gameplay.

---

### Fix #3: Missing lifecycle-runtime-compose Dependency
**Date:** 2025-01-05 14:30 UTC  
**Commit:** 143b332  

**Error:**
```
> Task :app:compileDebugKotlin FAILED
e: file:///home/builder/clone/app/src/main/java/com/advancedsnake/presentation/game/GameScreen.kt:27:27 Unresolved reference: compose
e: file:///home/builder/clone/app/src/main/java/com/advancedsnake/presentation/game/GameScreen.kt:40:42 Unresolved reference: collectAsStateWithLifecycle
```

**Root Cause:**  
The `collectAsStateWithLifecycle()` function requires the `androidx.lifecycle:lifecycle-runtime-compose` dependency, which was missing from build.gradle. The project only had `lifecycle-viewmodel-compose` but not `lifecycle-runtime-compose`.

**Solution:**  
Added the missing dependency to `app/build.gradle`:
```gradle
implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.7.0'
```

**Files Modified:**
- `app/build.gradle` (line 73)

**Research References:**
- [Android Developers - Lifecycle Compose](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [collectAsStateWithLifecycle API Documentation](https://developer.android.com/reference/kotlin/androidx/lifecycle/compose/package-summary)

**Prevention:** Always include both `lifecycle-viewmodel-compose` and `lifecycle-runtime-compose` when using Compose with ViewModels and lifecycle-aware state collection.

---

### Fix #2: Removed vectorDrawables Configuration
**Date:** 2025-01-05 13:45 UTC  
**Commit:** 303e251  

**Error:**
```
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:32: error: attribute android:cx not found.
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:32: error: attribute android:cy not found.
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:32: error: '2' is incompatible with attribute radius (attr) dimension.
```

**Root Cause:**  
The `vectorDrawables { useSupportLibrary true }` configuration in build.gradle was causing the Android build system to automatically generate `ic_launcher_vector.xml` with `<circle>` elements that use incompatible attributes (`android:cx`, `android:cy`, `android:radius` instead of `android:r`).

**Solution:**  
Removed the vectorDrawables configuration from `app/build.gradle`:
```gradle
// REMOVED:
vectorDrawables {
    useSupportLibrary true
}
```

**Files Modified:**
- `app/build.gradle` (lines 21-23 removed)

**Research Findings:**  
- The build system was automatically generating vector drawables for launcher icons
- PNG launcher icons are the standard approach and don't require vector drawable generation
- The auto-generated vector file used deprecated circle element syntax

**Prevention:** Only enable vectorDrawables.useSupportLibrary when actually using vector drawables in the app. For apps using PNG launcher icons, this configuration is unnecessary and can cause build conflicts.

---

### Fix #1: Removed Problematic Vector Drawable File
**Date:** 2025-01-05 13:15 UTC  
**Commit:** d351b60  

**Error:**
```
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:32: error: attribute android:cx not found.
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:37: error: attribute android:cy not found.
com.advancedsnake.app-main-74:/drawable/ic_launcher_vector.xml:49: error: '4' is incompatible with attribute radius (attr) dimension.
```

**Root Cause:**  
The `ic_launcher_vector.xml` file contained `<circle>` elements with incompatible attribute names. The file used `android:radius` instead of `android:r`, and the build tools couldn't process these attributes.

**Solution:**  
Completely removed the problematic vector drawable file:
```bash
rm app/src/main/res/drawable/ic_launcher_vector.xml
```

**Files Modified:**
- `app/src/main/res/drawable/ic_launcher_vector.xml` (deleted)

**Note:** This was a temporary fix. The real issue was discovered in Fix #2 where the file was being auto-generated due to the vectorDrawables configuration.

---

## App Architecture Overview

**Technology Stack:**
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM with Repository Pattern
- **Dependency Injection:** Hilt
- **Database:** Room
- **Coroutines:** For game loop and async operations
- **Navigation:** Compose Navigation

**Key Dependencies:**
- Compose BOM: 2023.10.01
- Material3: 1.2.1
- Lifecycle: 2.7.0
- Hilt: (version from gradle.properties)
- Room: (version from gradle.properties)
- Coroutines: (version from gradle.properties)

**Build Configuration:**
- compileSdk: 34
- targetSdk: 34
- minSdk: 26 (Android 8.0+ for Termux compatibility)
- Kotlin JVM Target: 1.8
- Java Compatibility: VERSION_1_8

---

## Codemagic Best Practices Learned

1. **Clean Builds:** Perform clean builds when dependency versions change
2. **Memory Management:** Set `JAVA_TOOL_OPTIONS: "-Xmx5g"` for memory-intensive builds
3. **Dependency Locking:** Use .lock files and check them into source control
4. **Version Alignment:** Ensure Kotlin and Compose compiler versions are compatible
5. **Resource Management:** Disable unnecessary build features (like vectorDrawables) when not needed

---

## Common Issues to Watch For

1. **Missing Compose Dependencies:** Always include both viewmodel-compose and runtime-compose
2. **Vector Drawable Conflicts:** Disable vectorDrawables.useSupportLibrary unless specifically needed
3. **Memory Issues:** Monitor Java heap space and adjust JAVA_TOOL_OPTIONS accordingly
4. **Version Mismatches:** Keep Kotlin, Compose, and related dependencies aligned

---

*Last Updated: 2025-01-05 15:50 UTC*  
*Document Version: 1.1*