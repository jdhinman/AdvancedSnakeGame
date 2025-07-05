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

### Fix #7: Gradle Wrapper Download Timeout in Codemagic CI
**Date:** 2025-01-05 18:30 UTC  
**Commit:** 489756c  

**Error:**
```
Exception in thread "main" java.io.IOException: Downloading from https://services.gradle.org/distributions/gradle-8.14-bin.zip failed: timeout (10000ms)
Caused by: java.net.SocketTimeoutException: Read timed out
```

**Root Cause:**  
Codemagic build environment experiencing network connectivity issues when downloading the Gradle 8.14 distribution. The default 10-second timeout was insufficient for completing the download under slower network conditions or during high traffic periods.

**Solution:**  
Increased the network timeout in `gradle-wrapper.properties`:
```properties
# BEFORE: 10-second timeout causing failures
networkTimeout=10000

# AFTER: 60-second timeout for reliable downloads  
networkTimeout=60000
```

**Files Modified:**
- `gradle/wrapper/gradle-wrapper.properties` (line 4)

**Technical Details:**
- **Network timeout**: Controls how long Gradle wrapper waits for distribution download
- **Download size**: gradle-8.14-bin.zip is approximately 130MB
- **CI environment**: Codemagic build agents may have variable network performance
- **Retry behavior**: Gradle wrapper doesn't automatically retry failed downloads

**Alternative Solutions Considered:**
1. **Gradle Build Cache**: Would reduce repeated downloads but doesn't solve initial download issue
2. **Pre-installed Gradle**: Codemagic supports this but wrapper approach is more reliable
3. **Different mirror**: services.gradle.org is the official and most reliable source

**Prevention:** For CI/CD environments, always use generous network timeouts (30-60 seconds) to account for variable network conditions and large download sizes.

**Monitoring:** If this error recurs, consider:
- Using Codemagic's pre-installed Gradle instead of wrapper
- Implementing build cache strategies
- Upgrading to newer Gradle versions with better retry mechanisms

---

### Fix #6: Room DAO Annotation Processor (Kapt) Compilation Errors
**Date:** 2025-01-05 18:15 UTC  
**Commit:** a5c7101  

**Error:**
```
> Task :app:kaptDebugKotlin FAILED
error: Query method parameters should either be a type that can be converted into a database column or a List / Array that contains such type.
error: Not sure how to handle query method's return type (java.lang.Object). DELETE query methods must either return void or int.
error: Not sure how to handle insert method's return type.
```

**Root Cause:**  
Room's Kapt annotation processor couldn't handle:
1. **Nullable return types** from SQL aggregate functions (MAX, AVG) when table is empty
2. **Missing return type specifications** for @Insert and @Query DELETE operations
3. **Implicit nullable types** causing Java stub generation issues during compilation

**Solution:**  
**ScoreDao.kt fixes:**
```kotlin
// BEFORE: Nullable return types causing Kapt errors
@Query("SELECT MAX(score) FROM scores")
suspend fun getHighestScore(): Int?

@Query("SELECT AVG(score) FROM scores") 
suspend fun getAverageScore(): Double?

@Insert
suspend fun insertScore(score: ScoreEntity)

@Query("DELETE FROM scores")
suspend fun clearAllScores()

// AFTER: Non-nullable with SQL COALESCE and explicit return types
@Query("SELECT COALESCE(MAX(score), 0) FROM scores")
suspend fun getHighestScore(): Int

@Query("SELECT COALESCE(AVG(score), 0.0) FROM scores")
suspend fun getAverageScore(): Double

@Insert
suspend fun insertScore(score: ScoreEntity): Long

@Query("DELETE FROM scores")
suspend fun clearAllScores(): Int
```

**LeaderboardRepositoryImpl.kt updates:**
- Removed null-safety operators (?:) since DAO now returns non-nullable types
- Simplified method implementations to directly return DAO results

**Files Modified:**
- `app/src/main/java/com/advancedsnake/data/local/dao/ScoreDao.kt`
- `app/src/main/java/com/advancedsnake/data/repositories/LeaderboardRepositoryImpl.kt`

**Technical Details:**
- **COALESCE function**: SQL function that returns first non-null value, provides defaults for empty tables
- **Return type specification**: @Insert returns Long (row ID), DELETE returns Int (affected rows)
- **Kapt compatibility**: Non-nullable types generate cleaner Java stubs for annotation processing

**Prevention:** When using Room with aggregate SQL functions, always use COALESCE or similar to ensure non-nullable return types, and explicitly specify return types for @Insert and @Query DELETE operations.

---

### Major Feature Update #5: Complete Leaderboard, Settings & Enhanced Visuals
**Date:** 2025-01-05 17:30 UTC  
**Commit:** 28f01ff  

**Features Implemented:**
- Comprehensive leaderboard system with Room database
- Full settings menu with game customization options
- Enhanced snake head with eyes and directional indicators
- Complete navigation system integration

**🎯 Leaderboard System:**
- **Room Database**: ScoreEntity, ScoreDao, AppDatabase with proper migration support
- **Data Storage**: Player name, score, snake length, game duration, speed level, timestamp
- **Statistics**: Games played, average score, highest score with real-time updates
- **UI Features**: Podium-style top 3 ranking, relative time display, game statistics card
- **Management**: Clear all scores with confirmation, empty state with motivation

**⚙️ Settings System:**
- **Game Speed**: Beginner (500ms), Normal (350ms), Expert (250ms) with dynamic progression
- **Board Size**: Small (15×20), Medium (20×30), Large (25×35) tiles
- **Control Sensitivity**: Adjustable slider affecting swipe gesture responsiveness  
- **Audio**: Sound effects and vibration toggles
- **Visual**: Snake color themes (Classic, Neon, Fire, Purple, Rainbow) with previews
- **Display**: Grid toggle, keep screen on, player name customization
- **Data**: DataStore-based persistence with reset to defaults option

**🐍 Enhanced Snake Head:**
- **Directional Eyes**: White eyes with black pupils positioned based on movement direction
- **Movement Indicator**: Triangular arrow showing current direction
- **Visual Distinction**: Darker green color (#2E7D32) vs body (#4CAF50)
- **Responsive Design**: Scales properly with different board sizes and screen densities

**🧭 Navigation Integration:**
- **Routes**: Added "leaderboard" and "settings" to MainActivity NavHost
- **Menu**: Replaced placeholder TODOs with functional navigation calls
- **Back Navigation**: Proper back button handling in all new screens
- **State Management**: Reactive UI updates with StateFlow and Compose

**🏗️ Architecture:**
- **Clean Architecture**: Maintained separation with new repositories, use cases, entities
- **Dependency Injection**: Hilt integration for all new components
- **Database**: Room database with proper DAO patterns and entity relationships
- **Settings**: DataStore preferences for type-safe settings persistence
- **Game Integration**: Settings automatically affect game speed, board size, controls

**Files Created:**
```
data/local/dao/ScoreDao.kt - Database operations
data/local/database/AppDatabase.kt - Room database setup  
data/local/entities/ScoreEntity.kt - Score data model
data/repositories/LeaderboardRepositoryImpl.kt - Score persistence
data/repositories/SettingsRepositoryImpl.kt - Settings persistence
domain/entities/GameSettings.kt - Settings data models with enums
domain/entities/Score.kt - Domain score model with formatting
domain/repositories/LeaderboardRepository.kt - Repository interface
domain/repositories/SettingsRepository.kt - Settings interface
domain/usecases/GetLeaderboardUseCase.kt - Score retrieval logic
domain/usecases/SaveScoreUseCase.kt - Score saving logic
presentation/leaderboard/LeaderboardScreen.kt - Leaderboard UI
presentation/leaderboard/LeaderboardViewModel.kt - Leaderboard state
presentation/settings/SettingsScreen.kt - Settings UI with categories
presentation/settings/SettingsViewModel.kt - Settings state management
```

**Files Modified:**
- `di/GameModule.kt`: Added database and repository bindings
- `presentation/MainActivity.kt`: Added leaderboard and settings routes
- `presentation/menu/MenuScreen.kt`: Replaced placeholder buttons with navigation
- `presentation/game/GameViewModel.kt`: Integrated settings and leaderboard systems
- `presentation/game/GameScreen.kt`: Enhanced snake head rendering with eyes/indicators

**Integration Benefits:**
- **Persistent Progression**: Scores survive app restarts and provide long-term engagement
- **Customizable Experience**: Players can adjust difficulty, visuals, and controls to preference
- **Visual Clarity**: Enhanced snake head improves gameplay readability and direction awareness
- **Mobile Optimization**: Settings include mobile-specific options like keep screen on and vibration
- **Scalable Architecture**: Clean foundation for future feature additions

**Result:** Transformed basic snake game into feature-complete mobile game with professional-grade leaderboard system, comprehensive settings, and enhanced visual design following modern Android development best practices.

---

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

*Last Updated: 2025-01-05 18:35 UTC*  
*Document Version: 2.2*