# slope
Project for CS2063

This is an android studio project

This is a game where the player guides their character down an infinite slope, and tries to set high scores.

The project code is contained in the ca.unb.mobiledev.slope package

### Release 1.0 notes: 
- Supports android API version 24 
- Button to clear save data 
- Button and dialog to view credits 
- All dialogs can be closed 
- Button to play the game 
- Main Menu supports landscape and portrait orientations based on sensor, game activity only supports landscape. 
- Game activity has pause button located in easy to reach area for users 
- Game activity has text to track distance the player character has traveled 
- Pause menu dialog has: 
- Quit to main menu button 
- Help dialog button 
- Settings dialog (saves a volume value which goes unused, as there are no sounds in the game) 

- Game features player character which moves to the right of the screen at a varying velocity based on the phone’s tilt 
- Screen can be tapped to make the player jumped 
- Player changes displayed bitmap based on state 
- Terrain generates a continuous downwards slope, the exact shape varies 
- Obstacles are generated all the way down the slope 
- Colliding with an obstacle generates a gameover condition 
- Gameover sets displays the distance achieved, last distance achieved, and the user’s all time high score. It displays additional “New High Score” text if the user just set a new record. 

### Known Issues: 
- Inconsistent issue where the player “crashes” on invisible obstacles 
- Player bounces along the slope’s surface 
- Obstacles may overlap, especially when first appearing 
- Obstacle generation may sometimes petter out 
- No sound to go with the volume settings we made for it 
- Terrain is never unloaded, this will eventually cause a memory leak 
- Player is never recentered, thus the player’s position will eventually be subject to floating point rounding errors 


## Code Overview

This is a high level overview of the code base. Please see the kotlin files contained within for more details.

### The following are the activity files:
- MainMenuActivity -- Activity for the main menu, uses activity_main_menu.xml layout, can start the GameActivity, clear any saved SharedPreferences data, or start the credits dialog
- GameActivity -- Activity for the game, uses the activity_game.xml layout, holds the UI for the game, a relativeLayout for the game itself, and holds the game loop. The game loop, found in `fun startGame()` is the interesting part of this class, as it makes an instance of all game objects, adds them to the RelativeLayout to be rendered, and starts a thread which calls each game object's `update()` and `render()` functions every frame. This thread also handles the game camera (view point)\'s position and sends out updates for the distance text. This class also contains code for controlling the activity life cycle, checking if the screen was tapped, and getting the screen dimensions.

### The following are the dialog files:
- CreditsDialog -- Uses credits_menu.xml layout, displays credit text and a close button.
- PauseMenuDialog -- Uses pause_menu.xml layout, contains buttons for closing this dialog, opening the settings and help dialogs, and closing the GameActivity to return to the MainMenuActivity
- SettingsDialog -- Uses settings_menu.xml layout, Contains a slider and mute button to set a volume variable, has a save button to save this value to SharedPreferences, and has a close button to close this dialog.
- HelpDialog -- Uses help_menu.xml layout, Contains text explaining the game and a close button.
- GameOverMenuDialog -- Uses gameover_menu layout, Displays the distance the player achieved in the present run, the last run, and the all time high score run (loaded from SharedPreferences). Displays "New High Score" text if this is the all time high score, and saves new scores to SharedPreferences.

### ObjectView
The ObjectView class is the parent class of all game objects. It contains variables to track the object's global position, rotation, screen position, and bitmap. It provides any inheriting game object functions start, update, and render for the game loop thread to run, and a setBitmap function.

### The following are the game objects contained in the /objects package:
All of these inherit the ObjectView class.
- Background -- unused attempt at creating a repeating background
- Obstacle -- based on "obstacle type", makes a box collider, and sets a bitmap to represent an obstacle. If the player's collider ever overlaps with an obstacle's collider, a gameOver is triggered
- Player -- Handles the physics for the player character, and reacts to any controls. This class also updates the player's collider position, the player bitmap used based on player state, and tells the terrain to generate new segments based on played position. Also calls the terrain to check if the player intersects with the slope, and correct its position if so.
- Terrain -- Represents the landscape the player and obstacles sit atop of. It holds an array of vertices representing the terrain, rendered on to the canvas every frame. Terrain is generated by creating "segments" (a sub class), consisting of 2 height values (obtained from a constant slope and sampling Simplex noise). With 2 height values, a segment can generate a y=mx+b slope for the player to check if its intersecting with the terrain (the player's position is corrected to be above the terrain if this is the case), and will generate the vertices for 2 triangles (which form a quad shape) for its visual representation. Segments are added to a segment list at the player's request, always ensuring that there is new terrain off screen. Terrain.kt also has a function to cycle the last obstacle's position to be on top of any newly generated segments.

### The following are other helper classes for game objects:
- Vec2 -- Helper class for doing position and collider math with 2 dimesional vectors
- Collision -- Defines a box collider, an object which can check for intersection with another box collider. Box collision is determined with AABB method (similar to intersection of a sprite with tap position), so no rotation of the collider is possible.
- sensor/AccelerometerSensor -- Class containing helper functions for managing the accelerometer sensor.
- noise/SimplexNoise_Octave -- Effectively generates a texture/bitmap with colour values ranging from black (0) to white (1). This noise is smooth (as opposed to "TV static"), so is useful for giving terrain natural variation. This code is from an external library, and is given attribution in the credits dialog.

### Other Files

The files contained within the package "old" are unused and deprecated

Additional help was provided by Peter's brother, who kindly made the art assets under res/drawable.