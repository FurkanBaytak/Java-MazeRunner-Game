# Java-MazeRunner-Game

MazeRunner is a console-based maze game written in Java. The objective of the game is to navigate through a maze, collecting bonuses and avoiding mines, to reach the end point.

## Game Instructions

- The maze is represented as a 15x15 grid.
- The player starts at the position marked with `B` and needs to reach the position marked with `E`.
- The player can move up, down, left, or right using the `W`, `A`, `S`, `D` keys respectively.
- The player can use bonuses by pressing the `+` key and selecting the desired bonus.
- The game ends when the player reaches the end point or exits the game by typing `exit`.

## Symbols

- `#`: Wall
- `.`: Empty space
- `B`: Starting point
- `E`: End point
- `T`: Teleport bonus
- `R`: Wall removal bonus
- `H`: Move reduction bonus
- `F`: Mine disarm bonus
- `!`: Mine

## Bonuses

- **Teleport Bonus (T)**: Allows the player to teleport to a chosen empty cell.
- **Wall Removal Bonus (R)**: Allows the player to remove a wall when encountered.
- **Move Reduction Bonus (H)**: Reduces the move count by 2.
- **Mine Disarm Bonus (F)**: Allows the player to disarm a mine when encountered.

## How to Play

1. Run the `MazeRunner` class.
2. Follow the instructions displayed on the console.
3. Use the `W`, `A`, `S`, `D` keys to move.
4. Use the `+` key to activate a bonus and follow the prompts.
5. Reach the end point marked with `E` to win the game.
6. Type `exit` to quit the game.

## Game Features

- Dynamic maze with random rearrangement of bonuses and mines every 5 moves.
- Multiple bonuses with unique functionalities to aid the player.
- Simple and intuitive controls.
