# Swerve Robotics FTC Library

*Note: new use of the Swerve Robotics Library is not recommended, as nearly all the functionality provided
therein has been folded into the core FTC SDK. For those returning here from last year, welcome!*

Welcome to the Swerve Robotics library for the FTC robot controller runtime.
The original purpose of our library was to augment the robot controller runtime library from FTC HQ
in order to simplify programming for FTC teams. Over the course of the 2015-16 season, we implemented
a considerable amount of functionality that aided in endeavor. However, during that
season and through the subsequent summer, virtually all of that functionality was incorporated
into the core FTC SDK (as of this writing, 5 Sept 2016, the only tiny remaining piece is a very
small enhancement to color sensors, and with the new @I2cSensor support, that could be better
implemented). Accordingly, for the time being at least, the Swerve Robotics library is being mothballed. 

Thank you for your support and feedback. It was much appreciated.

## Archive

The remainder of this readme is of historical interest only.

The central aim here is not to change what's there,
just to make it better. The library is a drop-in replacement: if your code works with the FTC HQ
release, it will work with the Swerve library, but you can also take advantage of new things.
**Please** consult the [GitHub release notes](https://github.com/SwerveRobotics/ftc_app/releases)
for details regarding a given release. You might also want to check out our related project, the 
Swerve Robotics Tools Suite, also [here](https://github.com/SwerveRobotics/tools) on GitHub. 

Notable features of the Swerve Robotics FTC Library include the following. Note: the 'Easy' controllers
are now part of the core SDK. The functionality is as described here, but they are now *always*
in use, irrespective of the flavor of opmode.

### Easy Legacy Motor Controller
**EasyLegacyMotorController** is a replacement for the stock DCMotorController implementation
used with legacy HiTechnic motor controllers. EasyLegacyMotorController does away with the need to manually
switch motors from read mode to write mode and back again and the attendant complex delay management,
loop counting, or waiting for hardware cycles that that requires. Just call `setPower()`, `getPosition()`,
or whatever other motor methods you need to use, and the right things happen under the covers.

### Easy Modern Motor & Servo Controller and Easy Legacy Servo Controller
In a conceptually similar way, alternate implementations for modern motor and servo controllers and
for legacy servo controllers is also provided. The API simplifications for these controllers are less dramatic
than for the easy legacy motor controller, but are worthwhile nevertheless. Most importantly, reads
and writes to these device now happen (virtually) when you issue them, and reads or writes that follow
a write are sequenced after that write so as to preserve causality. This simplifies programming. Change
a motor mode? Fine, it's changed: anything else you do on that motor will be sure to have seen the effect
of that change. You don't have to poll to see whether the mode change has taken effect. Reset the encoders
and then immediately change to run with encoders? Perfectly fine. It just works. Additionally, a handful 
of bug fixes is included. For example, `Servo.getPosition()` is now functionally useful.
 
### Alternate OpMode Registration
The library has an **alternate OpMode registration mechanism** (the old `FtcOpModeRegister.register()` still works too)
that allows you to register your own OpModes simply by decorating them with `@TeleOp` or `@Autonomous` annotations.
This helps promote clean living and easier integration of library updates over time by avoiding
editing code that lives in libraries owned by others. To register OpModes that aren't your own,
a related annotation, `@OpModeRegistrar`, can be placed on a method in your code which is to be called
as part of the registration process. Take a look at the YourCodeHere module for an example of
how this works. We'd like to thank [dmssargent](https://github.com/dmssargent/Xtensible-ftc_app/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/FtcOpModeRegister.java)
for illustrating how this all might be technically accomplished.

### SynchronousOpMode
The library contains a [**SynchronousOpMode**](https://htmlpreview.github.io/?https://github.com/swerverobotics/ftc_app/blob/master/SwerveRoboticsLibrary/doc/javadoc/org/swerverobotics/library/SynchronousOpMode.html)
class that brings back the synchronous, linear programming style
with which teams have been familiar with from previous seasons in RobotC, and which is more amenable
to teaching to beginning programmers than the event-driven / `loop()` callback programming
model native to the robot controller runtime. SynchronousOpMode is similar to [LinearOpMode](https://htmlpreview.github.io/?https://github.com/ftctechnh/ftc_app/blob/master/doc/javadoc/com/qualcomm/robotcore/eventloop/opmode/LinearOpMode.html)
but contains several enhancements, improved robustness, and several fixes. All hardware objects
visible in SynchronousOpModes are thread-safe, in that they can be manipulated concurrently by
multiple threads without internally becoming confused. SynchronousOpMode also gives you precise
control of when changes in gamepad state are made visible to your program, allowing you to
safely reason about a given state across a possibly complicated chain of logic. Information on 
writing in or migrating to SynchronousOpMode is found just below.
    
### Enhanced Telemetry
The library contains an enhanced form of telemetry containing a **dashboard** and a **log**. On the driver station display,
the dashboard appears at the top, followed by as many of the recent log messages as will reasonably 
fit. The dashboard can be preconfigured just once with unevaluated computations to form the lines
on the dashboard, and / or the lines can be created dynamically with `addData()` calls as in
the robot controller runtime. You call `telemetry.update()` to compose
the current dashboard and transmit to the driver station. Only a subset of `update()` calls
actually transmit, saving bandwith on the network and data acquistion time on the controller.
Log messages can be written to the log at any time, and these are sent to the driver station as
soon as possible. The enhanced telemetry class can be used both by synchronous and non-synchronous
opmodes, but is used automatically in SynchronousOpModes.

### Easy I2C Programming
The library (indeed, now the core SDK) contains an **I2cDeviceSynch** class that wraps I2cDevice instances
and makes them easy to use by handling read-vs-write mode switches and attendant waits automatically and
transparently. Just call `read8()` or `write8()` (and friends) to read and write device registers and the
rest is taken care of. With the I2C register map you get from the sensor manufacturer in hand, it's now
just dead easy to write your own code to talk to new I2C devices. I2cDeviceSynch can be used from any
flavor of OpMode.

### AdaFruit IMU Support
The library contains a class that is built on I2cDeviceSynch that provides a semantic interface to the **Bosch BNO055 absolute
position sensor**, allowing teams to make easy use of the [AdaFruit inertial motion unit (IMU)](http://www.adafruit.com/products/2472)
which incorporates that sensor module. Features of this sensor include a gyro that does rate
integration in hardware to provide robust and accurate angular position indications, and a
separation of the output of the accelerometer into gravity and linear-motion-induced components.
The class builds on the latter to provide linear velocity and position measurements using integration
in software. That said, the built-in accelerometer integration algorithm is quite naive. For a real
robot, you'll want to do some investigation and reading and provide a better one, which you can
specify in the initialization parameters for the IMU. Also, while the out-of-box sensor works
remarkably well, Bosch [describes](https://github.com/SwerveRobotics/ftc_app/raw/master/SwerveRoboticsLibrary/doc/reference/BST_BNO055_DS000_13.pdf)
a one-time calibration process (see Section 3.11) that will make it even better.
    
### Enhanced Color Sensor
The library contains a alternate implemenation of HiTechnic and ModernRobotics color sensors that
robustly implements the color sensor semeantics. In particular, the LED works reliably.


## How To Use

The fifteen second summary of how to use SynchronousOpMode is as follows:

*   Inherit your opmode from SynchronousOpMode instead of OpMode or LinearOpMode.
*   Implement your code in a `main()` method whose signature is:

        @Override protected void main() throws InterruptedException
*   Initialize your hardware variables at the top of `main()` instead of in `start()`. Otherwise,
    the use of hardware objects (DcMotor, Servo, GamePads, etc) is the same as in the usual robot
    controller runtime.
*   The core of the body of `main()` for a typical TeleOp OpMode should look like

        // Initialize stuff (not shown)
        
        // Wait for the game to start
        this.waitForStart(); 
        
        while (this.opModeIsActive()) {
            if (this.updateGamePads()) {
                // Do something interesting
                }
            telemetry.update();
            this.idle();
            }

That's it! Autonomous OpModes are even simpler: just write what you want the robot to do
after `waitForStart()`.

Migrating from LinearOpMode to SyncronousOpMode is easy, usually simply involving 

* changing the OpMode base class to SynchronousOpMode from LinearOpMode
* changing the name of your `runOpMode()` method to `main()`
* adding `telemetry.update()` and `this.idle()` to the bottom of your `while(opModeIsActive())` loop
* optionally removing `waitOneFullHardwareCycle()` calls, as they are no longer necessary

The Swerve Library now appears to be quite stable and functional. Our own teams are actively
developing their competition code using it. It currently is synchronized to the beta release from
FTC HQ that was published in March, 2016 (version 1.7).

Please be sure to **update your driver station** app to the latest-available version by side-loading
the .APK from the doc\apk directory. Side loading can be accomplished by any of several means. See
the ADB command 'install' command, for example (ADB is found in the Android SDK). Alternately, any
of several PC applications (such as http://apkinstaller.com/) and Android APK Installer apps (found
in the Play Store) can be used. The thread here (http://ftcforum.usfirst.org/showthread.php?6101-FTC-Beta-Branch&p=24750#post24750) might also be helpful.

## Installing the Library

To use the library, we recommend forking or cloning our repository and working off of the 
'master' branch. The Swerve Library repository *includes* the robot controller runtime
repository from FTC HQ; you don't need both. While we do tag major milestones in the library
and 'release' them, we try to keep the master branch always stable and fully functional, so you
could reasonably sync to the latest available if you wished. Alternately, instead of forking
or cloning, you can download a full copy of the source in .zip form from one of our releases.
If you have previously forked the FTC HQ tree from https://github.com/ftctechnh/ftc_app and you
are moderately git-savvy, you can easily upgrade to the Swerve Library by adding a new remote 
of https://github.com/SwerveRobotics/ftc_app to your git tree and initiating a pull from that remote.
If that's greek to you, give us a ring, and we'll help you through it.
 
Documentation is available in the SwerveRoboticsLibrary/doc/javadoc directory.
There are also several examples of using the library to be found in the 'examples'
package.

We'd love to hear what you think about the library. Thanks!

Robert Atkinson,  
bob@theatkinsons.org,  
Mentor, Swerve Robotics,    
Woodinville, Washington

(The remainder of this file is as published by FTC headquarters.)

# ftc_app
FTC Android Studio project to create FTC Robot Controller app.

This is the FTC SDK that can be used to create an FTC Robot Controller app, with custom op modes.
The FTC Robot Controller app is designed to work in conjunction with the FTC Driver Station app.
The FTC Driver Station app is available through Google Play.

To use this SDK, download/clone the entire project to your local computer.
Use Android Studio to import the folder  ("Import project (Eclipse ADT, Gradle, etc.)").

Documentation for the FTC SDK are included with this repository.  There is a subfolder called "doc" which contains several subfolders:

 * The folder "apk" contains the .apk files for the FTC Driver Station and FTC Robot Controller apps.
 * The folder "javadoc" contains the JavaDoc user documentation for the FTC SDK.
 * The folder "tutorial" contains PDF files that help teach the basics of using the FTC SDK.

For technical questions regarding the SDK, please visit the FTC Technology forum:

  http://ftcforum.usfirst.org/forumdisplay.php?156-FTC-Technology


**************************************************************************************

Version 2.10 (released on 16.09.03)
 * Support for Adafruit IMU.
 * Improvements to ModernRoboticsI2cGyro class
    - Block on reset of z axis.
    - isCalibrating() returns true while gyro is calibration.
 * Updated sample gyro program.
 * Blockly enhancements
    - support for android.graphics.Color.
    - added support for ElapsedTime.
    - improved look and legibility of blocks.
    - support for compass sensor.
    - support for ultrasonic sensor.
    - support for IrSeeker.
    - support for LED.
    - support for color sensor.
    - support for CRServo
    - prompt user to configure robot before using programming mode.
 * Provides ability to disable audio cues.
 * various bug fixes and improvements.

**************************************************************************************

Version 2.00 (released on 16.08.19)
 * This is the new release for the upcoming 2016-2017 FIRST Tech Challenge Season.
 * Channel change is enabled in the FTC Robot Controller app for Moto G 2nd and 3rd Gen phones.
 * Users can now use annotations to register/disable their Op Modes.
 * Changes in the Android SDK, JDK and build tool requirements (minsdk=19, java 1.7, build tools 23.0.3).
 * Standardized units in analog input.
 * Cleaned up code for existing analog sensor classes.
 * setChannelMode and getChannelMode were REMOVED from the DcMotorController class.  This is important - we no longer set the motor modes through the motor controller.
 * setMode and getMode were added to the DcMotor class.  
 * ContinuousRotationServo class has been added to the FTC SDK.
 * Range.clip() method has been overloaded so it can support this operation for int, short and byte integers.
 * Some changes have been made (new methods added) on how a user can access items from the hardware map.
 * Users can now set the zero power behavior for a DC motor so that the motor will brake or float when power is zero.
 * Prototype Blockly Programming Mode has been added to FTC Robot Controller.  Users can place the Robot Controller into this mode, and then use a device (such as a laptop) that has a Javascript enabled browser to write Blockly-based Op Modes directly onto the Robot Controller.
 * Users can now configure the robot remotely through the FTC Driver Station app.
 * Android Studio project supports Android Studio 2.1.x and compile SDK Version 23 (Marshmallow).
 * Vuforia Computer Vision SDK integrated into FTC SDK.  Users can use sample vision targets to get localization information on a standard FTC field.
 * Project structure has been reorganized so that there is now a TeamCode package that users can use to place their local/custom Op Modes into this package.
 * Inspection function has been integrated into the FTC Robot Controller and Driver Station Apps (Thanks Team HazMat… 9277 & 10650!).
 * Audio cues have been incorporated into FTC SDK.
 * Swap mechanism added to FTC Robot Controller configuration activity.  For example, if you have two motor controllers on a robot, and you misidentified them in your configuration file, you can use the Swap button to swap the devices within the configuration file (so you do not have to manually re-enter in the configuration info for the two devices).
 * Fix mechanism added to all user to replace an electronic module easily.  For example, suppose a servo controller dies on your robot. You replace the broken module with a new module, which has a different serial number from the original servo controller.  You can use the Fix button to automatically reconfigure your configuration file to use the serial number of the new module.
 * Improvements made to fix resiliency and responsiveness of the system.
 * For LinearOpMode the user now must for a telemetry.update() to update the telemetry data on the driver station.  This update() mechanism ensures that the driver station gets the updated data properly and at the same time.
 * The Auto Configure function of the Robot Controller is now template based.  If there is a commonly used robot configuration, a template can be created so that the Auto Configure mechanism can be used to quickly configure a robot of this type.
 * The logic to detect a runaway op mode (both in the LinearOpMode and OpMode types) and to abort the run, then auto recover has been improved/implemented.
 * Fix has been incorporated so that Logitech F310 gamepad mappings will be correct for Marshmallow users.

**************************************************************************************

Release 16.07.08

 * For the ftc_app project, the gradle files have been modified to support Android Studio 2.1.x.



**************************************************************************************

Release 16.03.30

 * For the MIT App Inventor, the design blocks have new icons that better represent the function of each design component.
 * Some changes were made to the shutdown logic to ensure the robust shutdown of some of our USB services.
 * A change was made to LinearOpMode so as to allow a given instance to be executed more than once, which is required for the App Inventor.
 * Javadoc improved/updated.

**************************************************************************************

Release 16.03.09

 * Changes made to make the FTC SDK synchronous (significant change!)
    - waitOneFullHardwareCycle() and waitForNextHardwareCycle() are no longer needed and have been deprecated.
    - runOpMode() (for a LinearOpMode) is now decoupled from the system's hardware read/write thread.
    - loop() (for an OpMode) is now decoupled from the system's hardware read/write thread.
    - Methods are synchronous.
    - For example, if you call setMode(DcMotorController.RunMode.RESET_ENCODERS) for a motor, the encoder is guaranteed to be reset when the method call is complete.
    - For legacy module (NXT compatible), user no longer has to toggle between read and write modes when reading from or writing to a legacy device.
 * Changes made to enhance reliability/robustness during ESD event.
 * Changes made to make code thread safe.
 * Debug keystore added so that user-generated robot controller APKs will all use the same signed key (to avoid conflicts if a team has multiple developer laptops for example).
 * Firmware version information for Modern Robotics modules are now logged.
 * Changes made to improve USB comm reliability and robustness.
 * Added support for voltage indicator for legacy (NXT-compatible) motor controllers.
 * Changes made to provide auto stop capabilities for op modes.
    - A LinearOpMode class will stop when the statements in runOpMode() are complete.  User does not have to push the stop button on the driver station.
    - If an op mode is stopped by the driver station, but there is a run away/uninterruptible thread persisting, the app will log an error message then force itself to crash to stop the runaway thread.
 * Driver Station UI modified to display lowest measured voltage below current voltage (12V battery).
 * Driver Station UI modified to have color background for current voltage (green=good, yellow=caution, red=danger, extremely low voltage).
 * javadoc improved (edits and additional classes).
 * Added app build time to About activity for driver station and robot controller apps.
 * Display local IP addresses on Driver Station About activity.
 * Added I2cDeviceSynchImpl.
 * Added I2cDeviceSync interface.
 * Added seconds() and milliseconds() to ElapsedTime for clarity.
 * Added getCallbackCount() to I2cDevice.
 * Added missing clearI2cPortActionFlag.
 * Added code to create log messages while waiting for LinearOpMode shutdown.
 * Fix so Wifi Direct Config activity will no longer launch multiple times.
 * Added the ability to specify an alternate i2c address in software for the Modern Robotics gyro.
 
**************************************************************************************

Release 16.02.09

 * Improved battery checker feature so that voltage values get refreshed regularly (every 250 msec) on Driver Station (DS) user interface.
 * Improved software so that Robot Controller (RC) is much more resilient and “self-healing” to USB disconnects:
    - If user attempts to start/restart RC with one or more module missing, it will display a warning but still start up.
    - When running an op mode, if one or more modules gets disconnected, the RC & DS will display warnings,and robot will keep on working in spite of the missing module(s).
    - If a disconnected module gets physically reconnected the RC will auto detect the module and the user will regain control of the recently connected module.
    - Warning messages are more helpful (identifies the type of module that’s missing plus its USB serial number).   
 * Code changes to fix the null gamepad reference when users try to reference the gamepads in the init() portion of their op mode.
 * NXT light sensor output is now properly scaled.  Note that teams might have to readjust their light threshold values in their op modes.
 * On DS user interface, gamepad icon for a driver will disappear if the matching gamepad is disconnected or if that gamepad gets designated as a different driver.
 * Robot Protocol (ROBOCOL) version number info is displayed in About screen on RC and DS apps.
 * Incorporated a display filter on pairing screen to filter out devices that don’t use the “<TEAM NUMBER>-“ format. This filter can be turned off to show all WiFi Direct devices.
 * Updated text in License file.
 * Fixed formatting error in OpticalDistanceSensor.toString().
 * Fixed issue on with a blank (“”) device name that would disrupt WiFi Direct Pairing.
 * Made a change so that the WiFi info and battery info can be displayed more quickly on the DS upon connecting to RC.
 * Improved javadoc generation.
 * Modified code to make it easier to support language localization in the future.

**************************************************************************************

Release 16.01.04

 * Updated compileSdkVersion for apps
 * Prevent Wifi from entering power saving mode
 * removed unused import from driver station
 * Corrrected "Dead zone" joystick code.
 * LED.getDeviceName and .getConnectionInfo() return null
 * apps check for ROBOCOL_VERSION mismatch
 * Fix for Telemetry also has off-by-one errors in its data string sizing / short size limitations error
 * User telemetry output is sorted.
 * added formatting variants to DbgLog and RobotLog APIs
 * code modified to allow for a long list of op mode names.
 * changes to improve thread safety of RobocolDatagramSocket
 * Fix for "missing hardware leaves robot controller disconnected from driver station" error
 * fix for "fast tapping of Init/Start causes problems" (toast is now only instantiated on UI thread).
 * added some log statements for thread life cycle.
 * moved gamepad reset logic inside of initActiveOpMode() for robustness
 * changes made to mitigate risk of race conditions on public methods.
 * changes to try and flag when WiFi Direct name contains non-printable characters.
 * fix to correct race condition between .run() and .close() in ReadWriteRunnableStandard.
 * updated FTDI driver
 * made ReadWriteRunnableStanard interface public.
 * fixed off-by-one errors in Command constructor
 * moved specific hardware implmentations into their own package.
 * moved specific gamepad implemnatations to the hardware library.
 * changed LICENSE file to new BSD version.
 * fixed race condition when shutting down Modern Robotics USB devices.
 * methods in the ColorSensor classes have been synchronized.
 * corrected isBusy() status to reflect end of motion.
 * corrected "back" button keycode.
 * the notSupported() method of the GyroSensor class was changed to protected (it should not be public).


**************************************************************************************

Release 15.11.04.001

 * Added Support for Modern Robotics Gyro.
  - The GyroSensor class now supports the MR Gyro Sensor.
  - Users can access heading data (about Z axis)
  - Users can also access raw gyro data (X, Y, & Z axes).
  - Example MRGyroTest.java op mode included.
 * Improved error messages
  - More descriptive error messages for exceptions in user code.
 * Updated DcMotor API
 * Enable read mode on new address in setI2cAddress
 * Fix so that driver station app resets the gamepads when switching op modes.
 * USB-related code changes to make USB comm more responsive and to display more explicit error messages.
  - Fix so that USB will recover properly if the USB bus returns garbage data.
  - Fix USB initializtion race condition.
  - Better error reporting during FTDI open.
  - More explicit messages during USB failures.
  - Fixed bug so that USB device is closed if event loop teardown method was not called.
 * Fixed timer UI issue
 * Fixed duplicate name UI bug (Legacy Module configuration).
 * Fixed race condition in EventLoopManager.
 * Fix to keep references stable when updating gamepad.
 * For legacy Matrix motor/servo controllers removed necessity of appending "Motor" and "Servo" to controller names.
 * Updated HT color sensor driver to use constants from ModernRoboticsUsbLegacyModule class.
 * Updated MR color sensor driver to use constants from ModernRoboticsUsbDeviceInterfaceModule class. 
 * Correctly handle I2C Address change in all color sensors
 * Updated/cleaned up op modes.
  - Updated comments in LinearI2cAddressChange.java example op mode.
  - Replaced the calls to "setChannelMode" with "setMode" (to match the new of the DcMotor  method).
  - Removed K9AutoTime.java op mode.
  - Added MRGyroTest.java op mode (demonstrates how to use MR Gyro Sensor).
  - Added MRRGBExample.java op mode (demonstrates how to use MR Color Sensor).
  - Added HTRGBExample.java op mode (demonstrates how to use HT legacy color sensor).
  - Added MatrixControllerDemo.java (demonstrates how to use legacy Matrix controller).
 * Updated javadoc documentation.
 * Updated release .apk files for Robot Controller and Driver Station apps.

T. Eng
November 5, 2015
 
**************************************************************************************

Release 15.10.06.002

 * Added support for Legacy Matrix 9.6V motor/servo controller.
 * Cleaned up build.gradle file.
 * Minor UI and bug fixes for driver station and robot controller apps.
 * Throws error if Ultrasonic sensor (NXT) is not configured for legacy module port 4 or 5.

T. Eng
October 6, 2015

**************************************************************************************

In this latest version of the FTC SDK (20150803_001) the following changes should be noted:

 * New user interfaces for FTC Driver Station and FTC Robot Controller apps.
 * An init() method is added to the OpMode class.
   - For this release, init() is triggered right before the start() method.
   - Eventually, the init() method will be triggered when the user presses an "INIT" button on driver station.
   - The init() and loop() methods are now required (i.e., need to be overridden in the user's op mode).
   - The start() and stop() methods are optional.
 * A new LinearOpMode class is introduced.
   - Teams can use the LinearOpMode mode to create a linear (not event driven) program model.
   - Teams can use blocking statements like Thread.sleep() within a linear op mode.
 * The API for the Legacy Module and Core Device Interface Module have been updated.
   - Support for encoders with the Legacy Module is now working.
 * The hardware loop has been updated for better performance.


T. Eng
August 3, 2015
