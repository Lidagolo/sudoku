# Sudoku

A Java Swing sudoku game with multiple difficulty levels, hints, undo, and save/load functionality. Built as a personal project to practice Java, Swing, and MVC architecture.

## Features

- **Four difficulty levels** — easy, medium, hard, and extreme puzzles
- **Hint system** — reveal a correct cell when you're stuck
- **Undo** — take back your last move
- **Save and load** — pick up a game right where you left off
- **Clean Swing UI** — intuitive controls with a control panel for all game actions

## Requirements

- Java 17 or higher
- Maven 3.6+

## Running the game

From the project root, run:

​```
mvn compile exec:java
​```

Or open the project in IntelliJ IDEA and run `Game.java` directly.

## Project structure

```
src/
├── main/java/sudoku/
│   ├── Game.java              # Entry point
│   └── game/
│       ├── RunSudoku.java     # Main game window and UI
│       ├── SudokuBoard.java   # Board rendering and interaction
│       ├── SudokuModel.java   # Game logic and state
│       └── PuzzleLoader.java  # Loads puzzles from files
└── test/java/sudoku/game/
    └── SudokuModelTest.java   # Unit tests
```

## How to play

1. Launch the game and pick a difficulty
2. Click a cell and type a number 1–9 to fill it in
3. Use **Hint** if you're stuck, **Undo** to take back a move
4. **Save** your progress at any time and **Load** it later

## Built with

- Java 17
- Swing (UI)
- JUnit 5 (testing)
- Maven (build)

## Author

Lida Goloveyko — [github.com/lidagolo](https://github.com/lidagolo)