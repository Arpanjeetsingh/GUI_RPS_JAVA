# Rock Paper Scissors — GUI
CS 151 Object-Oriented Analysis and Design, Assignment 5
San José State University

## Requirements

| Requirement | Version |
|---|---|
| Java JDK | **17 or higher** |
| JavaFX | 17.0.6 (downloaded automatically) |
| Maven | 3.9.6 (downloaded automatically by setup script) |
| Internet | Required on first run to download dependencies |

Any JDK 17+ works: Oracle JDK, Temurin, Zulu, Amazon Corretto, Microsoft Build of OpenJDK, etc.

## How to Run

**Windows — double-click `run.bat`**, or run in a terminal:
```
powershell -ExecutionPolicy Bypass -File setup_maven.ps1
```

The first run downloads Maven and JavaFX automatically (~60 MB total).
Subsequent runs start instantly.

## How to Run (manual — if you already have Maven installed)

```
mvn javafx:run
```

Make sure `JAVA_HOME` points to a JDK 17+ installation.

## Project Structure

```
src/
  App.java                            JavaFX entry point
  GameController.java                 GUI layout and event handling
  Game.java                           Console game loop (Assignment 4 reuse)
  ComputerPlayer.java                 Wraps the computer strategy
  ComputerChoiceStrategy.java         Strategy interface
  MachineLearningChoiceStrategy.java  N-gram ML strategy
  RandomChoiceStrategy.java           Random strategy
  HumanPlayer.java                    Console human input (Assignment 4)
  RulesEngine.java                    Determines round winner
  ScoreBoard.java                     Tracks scores
  Move.java                           Rock / Paper / Scissors enum
  RoundResult.java                    Human win / Computer win / Draw enum
  Player.java                         Player interface
  Main.java                           Console entry point (Assignment 4)
ml_data.txt                           Saved ML frequency table (persists across games)
pom.xml                               Maven build (handles JavaFX dependency)
```
