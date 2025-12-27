# 🐟 BIG FISH - Java Swing Game

A classic arcade-style **Java Swing game** where you control a fish and try to survive and grow by eating smaller fish. Includes login/registration, score tracking, and game history — all secured using **jBCrypt 0.4** for password hashing.

---

## 📦 Features

- 🎮 Interactive Java Swing GUI
- 🔐 Secure login & registration with hashed passwords (jBCrypt 0.4)
- 📈 Score tracking with best score popup
- 📜 Game history for each player
- ⌨️ Keyboard controls for gameplay
- 💾 File-based user data management (no external database needed)

---

## 🛠 Requirements

- [JDK 21](https://jdk.java.net/21/)
- [jBCrypt 0.4](https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4)

---

## 🚀 How to Run

### 1. Clone the repository

```bash
git clone https://github.com/beastNico/BIG-FISH.git
cd BIG-FISH
```

### 2. Install Dependencies

The project requires jBCrypt library. You can download it from Maven Repository:

- Download `jbcrypt-0.4.jar` from [Maven Central](https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar)
- Place the JAR file in your project's classpath

### 3. Build the Project

Using Ant (recommended for NetBeans projects):

```bash
ant clean
ant compile
ant jar
```

Or compile manually with javac:

```bash
javac -cp ".:jbcrypt-0.4.jar" -d build/classes src/**/*.java src/*.java
```

### 4. Run the Game

Using Ant:

```bash
ant run
```

Or run manually:

```bash
java -cp "build/classes:jbcrypt-0.4.jar" main.Main
```

## 🎮 How to Play

### Controls

- **Movement**: Use `W`, `A`, `S`, `D` keys to move your fish
- **Pause/Resume**: Press `P` to pause or resume the game
- **Menu Navigation**: Use `W`/`S` keys and `ENTER` to select options
- **Back to Menu**: Press `ESC` to return to the main menu

### Gameplay Mechanics

- Control a fish and eat smaller enemies to grow bigger and increase your score
- Your fish grows larger at score milestones (5, 10, 20 points) and can eat bigger enemies
- Avoid touching enemies larger than you can eat, or you'll lose a life
- The game has 4 difficulty levels that unlock as you progress:
  - **Level 1** (0-4 points): Basic enemies
  - **Level 2** (5-9 points): Unlocks at 3 points eaten
  - **Level 3** (10-20 points): Unlocks at 8 points eaten
  - **Final Level** (21+ points): Unlocks at 16 points eaten

### Features

- **Secure Authentication**: Login and registration with password hashing
- **Score Tracking**: High score and last game score display
- **Game History**: View your last 5 game scores
- **Sound Effects**: Background music and sound effects for eating, dying, leveling up, and new high scores
- **Visual Feedback**: Notifications for level progression and new high scores

## 📁 Project Structure

```
BIG_FISH/
├── src/                    # Source code
│   ├── main/              # Main game classes
│   │   ├── Main.java      # Application entry point
│   │   ├── GamePanel.java # Main game panel and logic
│   │   ├── UI.java        # User interface and menus
│   │   ├── KeyHandler.java # Input handling
│   │   ├── LoginFrame.java # Login screen
│   │   ├── SignUpFrame.java # Registration screen
│   │   ├── Sound.java     # Audio management
│   │   ├── AssetSetter.java # Enemy spawning
│   │   └── CollisionChecker.java # Collision detection
│   ├── Entity/            # Game entities
│   │   ├── Entity.java    # Base entity class
│   │   └── Fish1.java     # Player fish
│   ├── enemy/             # Enemy fish classes
│   │   ├── Enemy1.java to Enemy4.java
│   ├── Tile/              # Tile management
│   └── res/               # Resources (images, sounds, fonts)
├── build.xml              # Ant build configuration
├── manifest.mf            # JAR manifest
├── credentials.csv        # User credentials storage
├── Scores.csv             # Score data (auto-generated)
├── game_history.txt       # Game history storage
└── README.md
```

## 🔧 Technical Details

- **Language**: Java 21
- **Framework**: Java Swing for GUI
- **Audio**: Custom sound management
- **Graphics**: BufferedImage for sprites and backgrounds
- **Security**: jBCrypt 0.4 for password hashing
- **Build Tool**: Apache Ant
- **IDE**: NetBeans (project configured for)

## 🏆 Scoring System

- +1 point for eating an enemy
- -1 point for being hit by a larger enemy
- Fish grows larger at 5, 10, and 20 points
- New high score notifications for scores above 10

## 📝 Data Storage

- User credentials stored in `credentials.csv` with BCrypt hashed passwords
- Game history saved in `game_history.txt`
- Scores tracked in memory with persistence to files

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Enjoy playing BIG FISH!** 🐠
