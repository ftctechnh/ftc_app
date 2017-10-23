# OpMode Inheritance Chain


```
├ OpMode
  └─ BaseOp
     └─ StateBasedOp
        └─ TrackedOp
           └─ LoggedOp
              └─ [ROBOT_NAME]Hardware
              └─ [ROBOT_NAME]Auto
              └─ [ROBOT_NAME]Ctrl
```

### `BaseOp.java`
The base class of our OpMode Chain, basically an outline of what the ModernRobotics `OpMode.java` provides.

### `StateBasedOp.java`
The state machine that all of our OpModes run on. States are managed by it. *Route* objects extending `Route.java` are passed to it to follow. *Route* objects are lists of *Task* objects, implementing `Task.java`, that need to be completed.

### `TrackedOp.java`
Defines all robot tracking / mapping code. Can be passed *Tracker* objects implementing `Tracker.java` from robot-specific superclasses that are then used to track the robot. A single *Tracker* object provides methods to be ran that provide tracking data.

### `LoggedOp.java`
Handles custom telemetry (Bluetooth & File). Keeps track of device deltas to reduce redundant telemetry.

### *[ROBOT_NAME]*`Hardware.java`
Robot-specific class. Grabs / stores all necessary hardware references to be used by top level classes.

### *[ROBOT_NAME]*`Auto.java`
Robot-specific OpMode that is ran for autonomous mode, handles autonomous logic / controls hardware.

### *[ROBOT_NAME]*`Ctrl.java`
Robot-specific OpMode that is ran for controlled mode, handles input logic / controls hardware.
