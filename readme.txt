Elevator Design - A program to operate an elevator in a building with 15 floors. 
The elevator should be able to take user request, open and close doors, move upward and downward directions and should not keep any user waiting for indefinite time. 

Description
Elevator - Responsible for taking request & move to respective floor intelligently
RequestQueueController - Manages are requests in a queue & supplies various methods to decide Elevator class its movement
ElevatorDoorController - Manages all door operations
FloorPanel - Displays Elevator's current floor using FloorPanelDisplay & its moving state on floor
FloorPanelButton - Responsible for stopping Elevator, can be extended for Fan, Alarm, OverWeight reset, etc
ElevatorPanel - Accessible from inside Elevator, similar like FloorPanel

Notes:
* Tests only written for FloorPanel & Elevator, need to extended to maximize flow coverage
* Needs Re-factoring of Elevator & QueueController class
* Queue complexity can be reduced, need to go through again

TimeSpent: 4-5 hours
Language: Java 7
IDE: IntelliJ Idea
Testing lib: jUnit

