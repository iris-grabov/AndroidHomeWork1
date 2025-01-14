!!!! PAY ATTENTION !!!!
You have to insert your API in the manifest file:
<meta-data
            android:name="com.google.android.geo.API_KEY"

            android:value="MY_API_TO_INSERT" />

!!!!!!!!!
---

# Game Project

This is an Android game application that features an interactive game where the player can control the main character ("Harry Potter") either using buttons or sensors (tilt controls). The game involves avoiding obstacles, collecting coins, and avoiding magic hats. Players can track their scores, and the app also provides an option to view a map with high scores.

## Features
Added logo- icon

- **Main Game**:
  - The game consists of a  layout where obstacles (magic hats and coins) fall from the top, and the player controls Harry Potter's movement to avoid or collect them.
  - The player can choose between two control modes: **Button Mode** (using left and right buttons) or **Sensor Mode** (using tilt sensors).
  - You have to choose to play 
  - The game has two speed modes: **Fast** and **Slow**.
  - You have to choose to play 
  - The player’s score is displayed, and lives are lost if the player collides with magic hats.
  - Once the game ends, the score is displayed, for one second, and the player is redirected to the scoreboard screen.
  - Their is a sound of "boom" if colision
  - Collectiong score- 10 points for each hat that the user avoid
  - Collectiong score- bounus 10 points for each coin 
    

- **Menu Activity**:
  - The user can select the speed (Fast/Slow) and control mode (Buttons/Sensors) in the menu.
  - There is a button to start the game (`Start Game`), a button to navigate to the high scores page (`High Scores`), and buttons to choose between sensor or button controls.

- **Scoreboard and Map Activity**:
  - The `TableScoreActivity` displays the top high scores in a list format, and allows the user to interact with a map showing the geographical locations of high scores.
  - The player can navigate back to the main menu from this activity.
  - Clicking on a record will update the location display- zoom it.

---

## Project Structure

### Activities

1. **MainActivity**: 
   The primary activity where the game is played. It manages the game loop, obstacles, character movement, and score display. The player can control the game using either buttons or tilt sensors, depending on the selected mode.

2. **MenuActivity**: 
   Displays the main menu where the player selects game settings (control mode and speed) before starting the game.

3. **TableScoreActivity**: 
   Displays a map of  high scores and a list of the 10 top scores. This activity also includes a button to return to the main menu.

---


---


## How to Play

1. **Start the Game**: 
   - You have to choose to play 
   - Select your preferred game speed (`Fast` or `Slow`) and control mode (`Buttons` or `Sensors`).
   
2. **Control Harry**: 
   - **Buttons Mode**: Use the left and right arrows to move Harry across the grid.
   - **Sensor Mode**: Tilt your device to move Harry left or right.
   
3. **Avoid Obstacles**: 
   - Obstacles (magic hats and coins) fall from the top. Avoid magic hats and collect coins for extra points.

4. **Game Over**: 
   - The game ends when you collide with a magic hat 3 times. Your final score will be displayed for one second.

5. **High Scores**: 
   - After the game ends, you can check your score on the high score table and view a map of high scores from other players and zoom.

---
