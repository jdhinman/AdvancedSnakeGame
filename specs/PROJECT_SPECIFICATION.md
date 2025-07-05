# Advanced Snake Game - Comprehensive Project Specification

## Project Overview
**Name**: Advanced Snake Game for Android
**Development Environment**: Termux on Android
**Target API Level**: 26+ (Android 8.0+)
**Architecture**: MVVM with Clean Architecture
**Primary Language**: Kotlin
**UI Framework**: Jetpack Compose

## AI Collaboration Framework
This project is being developed through a collaborative approach between:
- **Claude Code**: Implementation, file management, build processes
- **Gemini CLI**: Architecture review, massive context analysis, optimization suggestions
- **Collaborative Process**: Iterative development with AI-to-AI feedback loops

## Technical Architecture

### Core Architecture Pattern
```
┌─────────────────────────────────────────┐
│          Presentation Layer             │
│   Jetpack Compose + Canvas + Animations │
├─────────────────────────────────────────┤
│            Domain Layer                 │
│   Game Logic + Rules + State Management │
├─────────────────────────────────────────┤
│             Data Layer                  │
│  Repository + Local DB + Preferences    │
├─────────────────────────────────────────┤
│          Infrastructure                 │
│   Hilt + Coroutines + Flow + Room      │
└─────────────────────────────────────────┘
```

### Key Components
1. **Game Engine**
   - Custom game loop with Kotlin Coroutines
   - 60fps target framerate
   - Efficient collision detection
   - State management with StateFlow

2. **UI System**
   - Jetpack Compose for menus and HUD
   - Canvas for game rendering
   - Touch gesture handling
   - Responsive design for multiple screen sizes

3. **Data Management**
   - Room database for persistent storage
   - SharedPreferences for game settings
   - Repository pattern for data access

4. **Audio System**
   - Background music with MediaPlayer
   - Sound effects with SoundPool
   - Dynamic audio based on game state

## Advanced Features

### Game Features
1. **Core Gameplay**
   - Classic snake mechanics with modern twist
   - Smooth animations and transitions
   - Intuitive touch controls
   - Pause/resume functionality

2. **Power-ups System**
   - Magnet: Attracts all food items
   - Bullet: Snake head becomes projectile
   - Frozen: Temporary invincibility
   - Speed Boost: Temporary speed increase
   - Size Multiplier: Double points for period

3. **Progression System**
   - Multiple difficulty levels
   - Achievement system
   - Global leaderboards
   - Unlockable snake skins
   - Challenge modes

4. **AI Opponents**
   - Neural network-based AI
   - Adaptive difficulty
   - Multiple AI personalities
   - Multiplayer support

5. **Visual Effects**
   - Particle systems for explosions
   - Smooth animations
   - Dynamic lighting effects
   - Custom shaders for special effects

### Technical Features
1. **Performance Optimization**
   - Efficient rendering pipeline
   - Memory management
   - Battery optimization
   - 60fps gameplay

2. **Responsive Design**
   - Multiple screen size support
   - Orientation handling
   - Adaptive UI scaling
   - Accessibility features

3. **Testing Integration**
   - Unit tests for game logic
   - UI tests with Compose
   - Integration tests
   - Performance benchmarks

## Development Constraints

### Termux Environment Limitations
1. **Resource Constraints**
   - Limited memory (monitor with `free -h`)
   - CPU limitations for compilation
   - Battery optimization required

2. **Development Workflow**
   - Command-line only development
   - No Android Studio GUI
   - Physical device testing only
   - Wireless debugging via ADB

3. **Build Process**
   - Gradle-based builds
   - Manual APK signing
   - Command-line testing
   - Git-based version control

### Workarounds and Solutions
1. **IDE Alternative**
   - Neovim with LSP support
   - Helix editor for modern features
   - Command-line debugging tools

2. **Testing Strategy**
   - Unit tests in Termux
   - ADB for device testing
   - Automated build scripts
   - Performance monitoring tools

## Project Structure
```
AdvancedSnakeGame/
├── app/
│   ├── src/main/
│   │   ├── java/com/advancedsnake/
│   │   │   ├── presentation/
│   │   │   │   ├── game/       # Game UI components
│   │   │   │   ├── menu/       # Menu screens
│   │   │   │   └── common/     # Shared UI components
│   │   │   ├── domain/
│   │   │   │   ├── entities/   # Game entities
│   │   │   │   ├── usecases/   # Business logic
│   │   │   │   └── repositories/ # Repository interfaces
│   │   │   ├── data/
│   │   │   │   ├── local/      # Room database
│   │   │   │   ├── remote/     # API services
│   │   │   │   └── repositories/ # Repository implementations
│   │   │   └── di/             # Dependency injection
│   │   └── res/
│   │       ├── layout/
│   │       ├── values/
│   │       └── drawable/
│   └── build.gradle
├── docs/                       # Documentation
├── specs/                      # Specifications
├── build-scripts/              # Build automation
├── tests/                      # Test suites
├── collaboration-notes/        # AI collaboration logs
└── README.md
```

## Development Phases

### Phase 1: Foundation (Current)
- [ ] Project setup and structure
- [ ] AI collaboration framework
- [ ] Basic game architecture
- [ ] Development environment configuration

### Phase 2: Core Implementation
- [ ] Game engine implementation
- [ ] Basic snake mechanics
- [ ] UI with Jetpack Compose
- [ ] Touch controls
- [ ] Basic collision detection

### Phase 3: Advanced Features
- [ ] Power-ups system
- [ ] Particle effects
- [ ] Sound system
- [ ] Achievement system
- [ ] Leaderboards

### Phase 4: Polish & Optimization
- [ ] Performance optimization
- [ ] Bug fixes
- [ ] UI/UX improvements
- [ ] Accessibility features
- [ ] Testing and validation

### Phase 5: Advanced AI & Multiplayer
- [ ] Neural network AI
- [ ] Multiplayer functionality
- [ ] Advanced game modes
- [ ] Final optimizations

## Success Metrics
1. **Performance**: Consistent 60fps gameplay
2. **Stability**: No crashes during normal gameplay
3. **User Experience**: Intuitive controls and smooth animations
4. **Code Quality**: High test coverage and maintainable architecture
5. **AI Collaboration**: Successful integration of AI feedback loops

## Collaboration Protocol
1. **Regular Reviews**: Gemini CLI reviews complete codebase at key milestones
2. **Architecture Discussions**: AI-to-AI discussions on major design decisions
3. **Optimization Cycles**: Iterative performance improvements with AI analysis
4. **Problem Solving**: Collaborative debugging and feature implementation
5. **Documentation**: Comprehensive logging of AI collaboration process

## Next Steps
1. Initialize conversation with Gemini CLI
2. Create basic project structure
3. Implement core game architecture
4. Begin iterative development cycle
5. Establish continuous AI feedback loops

---

*This specification will be updated throughout the development process as new requirements emerge and the AI collaboration provides additional insights.*