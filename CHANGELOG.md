# Changelog

All notable changes will be documented in this file.

This format is based on [Keep A Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/).

Markdown [cheat sheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

---


### [Unreleased]
- Thorough documentation on the new system (teamcode.Core package)
- Support for button actions such as double clicking and long clicks
- Support for motor speed ramping

### [0.9.1] - 2017-11-5
#### Fixed
- Sweeper class, wasn't performing garbage collection as intended


### [0.9.0] - 2017-11-04
#### Added
- OpenCV Utility file
- Added Histogram Equalization to image processing

#### Fixed
- Some HSV values


### [0.8.0] - 2017-11-02
#### Added
- The TelMet class, used to bring telemetry to non-OpMode classes
- Implemented the init() method in RobotBase- use super.init() in your subclasses
- Updated code as necessary to fit new method signature of RobotBase.init()
- OpenCV detection for both glyphs- brown glyphs work better because they stand out more
- Game Pad wrapper class called Controller- gives additional functionality to the Gamepad class such as click detection
- Updated SDK to match upstream repo

#### Fixed
- Changed Relic Recovery TeleOp class name to TeleOpMain
- Added erosion and dilation in object detection process- helps to filter out some noise


### [0.7.0] - 2017-10-30
#### Added
- Kotlin support!
- OpenCV wrapper classes (in Kotlin)
- Drivetrain and Lift for Relic Recovery robot
- Ability to toggle off and on OpenCV and the Java Camera View seen in the main activity
- Migrate to Java 8- previous issues were probably caused by a clash between Moto G4s and REV hardware, not the SDK. Plus, Android officially supports some parts J8 now.

#### Removed
- Disabled all test OpModes- they were getting quite numerous
- JavaDocs- they belong elsewhere, and are rarely updated. Still figuring out where they go, bear with me :)


### [0.6.0] - 2017-10-16
#### Added
- [OpenCV](https://opencv.org/) support! Version 3.2.0
- Test code in the activity class
- XML item in the main activity for displaying camera view
- Ability to pass in I2C Addresses when mapping MR I2C hardware
- Functions that map MR I2C hardware with default addresses

#### Removed
- Ability to grab position from IMU- the sensor isn't accurate enough to handle double integrating the sensor values
- Command annotations from RobotCommand- really, they were quite useless
- Try/Catch blocks from HardwareMapper- it's arguably better to get a mass of errors immediately rather than in the middle of runtime.
- Some broken code left over in previous commits that evaded code analysis

#### Fixed
- Comments
- More comments
- Typos in the comments

### [0.5.0] - 2017-10-10
#### Added
- Support for mapping continuous rotation servos and the Bosch BNO055 IMU in HardwareMapper.java
- Wrapper class for easy data retrieval of Bosch BNO055 IMU
- Test code for Bosh BNO055 IMU (Robot base and an OpMode)
#### Fixed
- Relic Grabber actually uses a continuous rotation servo as opposed to a regular servo. Oops.
- Cleaning up some JavaDoc comments

### [0.4.0] - 2017-10-08
#### Added
- Vuforia wrapper class for Relic Recovery
- Servo mapping support in HardwareMapper
- Code for testing Relic Grabber prototype

### [0.3.0] - 2017-10-02
#### Added
- Code for testing Vuforia
- Code for testing the Core package
- Code for testing parallel commands
#### Fixed
- Supressed warnings for clean build

### [0.2.0] - 2017-09-26
#### Added
- Code for testing the spools prototype
- Tutorial Code for teaching new teams
#### Removed
- Support for Java 8- the robot controller cannot detect hardware devices when using Java 8
#### Fixed
- Gradle files associated with the move the Java 8

### [0.1.0] - 2017-09-06
#### Added
- Support for sequential and parallel command execution (UNTESTED)
- JavaDoc Documentation  in ":/TeamCode JavaDoc"
- A shortcut to the TeamCode module in the root directory (by far the
most important addition!)
#### Removed
- The abstract reset() method from RobotComponent
- The reset() method from SixWheelPrototype.Drivetrain.Drivetrain
- Made the motor objects in SixWheelPrototype.Drivetrain.Drivetrain private
#### Fixed
- Changed "ModernRoboticsDigitalTouchSensor" to "ModernRoboticsTouchSensor"
in response to an SDK update

### [0.0.0] - 2017-09-06
#### Added
- Initial Commit

    - Last season's code
    - An experimental organizational structure (still under development)
    - Support for Java 8, provided that you get a beta version of
    [Android Studio](https://developer.android.com/studio/preview/index.html).
    YOUR CODE WON'T COMPILE WITHOUT IT.
- Full-fledged documentation is not yet available, that will come soon
