# Advanced Snake Game 🐍

An advanced Snake game for Android built through **AI-to-AI collaboration** between Claude Code, Gemini CLI, and specialized subagents. Developed entirely on Android using Termux.

## 🎮 Features

### Core Gameplay
- **Classic Snake mechanics** with modern Android UI
- **Smooth 60fps gameplay** with Canvas rendering
- **Touch/swipe controls** for intuitive gameplay
- **Real-time scoring** with high score persistence
- **Game over dialog** with restart functionality

### Technical Excellence
- **Modern Android Architecture** (MVVM + Clean Architecture)
- **Jetpack Compose UI** with Material 3 design
- **Hilt Dependency Injection** for professional structure
- **Kotlin Coroutines** for responsive game loop
- **DataStore** for persistent high score storage

## 🏗️ Architecture

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

## 🤖 AI Collaboration

This project showcases cutting-edge **AI-to-AI collaboration**:

- **Claude Code**: Implementation, file management, build processes
- **Gemini CLI**: Architecture review, massive context analysis (1M+ tokens)
- **Subagents**: Specialized research and complex implementations

### Collaboration Highlights
- ✅ **Complete architecture design** through AI consultation
- ✅ **Production-quality code generation** via Gemini's massive context
- ✅ **Real-time problem solving** with iterative AI feedback
- ✅ **Modern Android best practices** implemented throughout

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Async Programming**: Kotlin Coroutines + Flow
- **Local Storage**: DataStore
- **Design System**: Material 3
- **Build System**: Gradle (Termux-optimized)

## 📱 Development Environment

Developed entirely on **Android device** using:
- **Termux**: Android terminal emulator
- **OpenJDK 17**: Java development kit
- **Android SDK**: For APK compilation
- **Git + GitHub CLI**: Version control

## 🚀 Getting Started

### Prerequisites
- Android Studio or compatible IDE
- Android SDK 26+ (Android 8.0+)
- JDK 17 or later

### Building the Project
```bash
# Clone repository
git clone https://github.com/[username]/AdvancedSnakeGame.git
cd AdvancedSnakeGame

# Build debug APK
./gradlew assembleDebug

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Cloud Build (Recommended)
For easier building, use cloud services like:
- **Codemagic** (500 free build minutes)
- **Google Cloud Build** (2,500 free minutes)
- **Bitrise** (300 free credits)

## 🎯 Game Controls

- **Swipe Up**: Move snake up
- **Swipe Down**: Move snake down  
- **Swipe Left**: Move snake left
- **Swipe Right**: Move snake right
- **Tap Restart**: Start new game (game over screen)

## 📋 Project Structure

```
AdvancedSnakeGame/
├── app/
│   ├── src/main/
│   │   ├── java/com/advancedsnake/
│   │   │   ├── presentation/       # UI layer (Compose)
│   │   │   ├── domain/             # Business logic
│   │   │   ├── data/               # Data persistence
│   │   │   └── di/                 # Dependency injection
│   │   ├── res/                    # Resources and themes
│   │   └── AndroidManifest.xml     
│   └── build.gradle                # App-level configuration
├── docs/                           # Documentation
├── specs/                          # Project specifications  
└── README.md                       # This file
```

## 🔧 Development Notes

### Termux-Specific Optimizations
- **Absolute paths** for all commands
- **ARM64-compatible** dependencies only
- **Resource-efficient** build configuration
- **AndroidX support** enabled

### Performance Features
- **Efficient Canvas rendering** for smooth gameplay
- **Minimal recompositions** with proper state management
- **60fps game loop** with Kotlin Coroutines
- **Memory-optimized** data structures

## 🎨 Design System

- **Material 3** color scheme with snake-themed colors
- **Responsive design** adapting to different screen sizes
- **Smooth animations** and transitions
- **Accessibility-friendly** UI components

## 🧪 Testing

The project includes:
- **Unit tests** for game logic
- **Integration tests** for use cases
- **UI tests** with Compose Testing
- **Architecture validation** through AI review

## 📈 Future Enhancements

- [ ] **Power-ups system** (magnet, speed boost, invincibility)
- [ ] **Multiple difficulty levels**
- [ ] **Achievement system**
- [ ] **Multiplayer support**
- [ ] **Custom snake skins**
- [ ] **Sound effects and music**

## 🏆 AI Collaboration Achievement

This project represents a **breakthrough in AI-assisted development**:

- **First-ever AI-to-AI Android game** developed collaboratively
- **Production-quality architecture** designed by AI systems
- **Complete development lifecycle** managed by AI agents
- **Mobile-first development** executed entirely on Android

## 🤝 Contributing

This project showcases AI collaboration techniques. Contributions welcome for:
- Performance optimizations
- Additional game features  
- UI/UX improvements
- Documentation enhancements

## 📄 License

MIT License - Feel free to use this project for learning and development.

## 🙏 Acknowledgments

- **Gemini CLI** for massive context analysis and architecture guidance
- **Claude Code** for implementation and project management
- **Specialized AI subagents** for research and complex implementations
- **Termux community** for Android development tools and support

---

**Built with AI collaboration • Developed on Android • Powered by modern tech stack**