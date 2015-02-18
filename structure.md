AudioVisio Package Structure
============================

* audiovisio
    - AudioVisio
    - networking
        - messages
        - Server
        - Client
        - utilities
    - utils
    - gui
    - entities
    - level

Entity Class Hierarchy
----------------------
* Entity
    - MovingEntity
        - Player
            - AudioPlayer
            - VisualPlayer
        - Box
        - Ball
    - InteractableEntity
        - Door
        - Lever
        - Button

Level Package Layout
--------------------
* level
    - Level
    - LevelWriter
    - LevelReader
    - Trigger
    - Stair
    - Panel
