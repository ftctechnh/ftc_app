# Changelog

All notable changes will be documented in this file.

This format is based on [Keep A Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/).

Markdown [cheat sheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

---


### [Unreleased]
- Thorough documentation on the new system (teamcode.Core package)
- Wrapper class for Vuforia
- Support for button actions such as double clicking and long clicks
- Support for motor speed ramping

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
