# Changelog

All notable changes will be documented in this file.

This format is based on [Keep A Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/).

Markdown [cheat sheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

---


### [Unreleased]
- Thorough documentation on the new system (teamcode.Core package)
- Inevitable bug fixes that come from not being able to test code

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
