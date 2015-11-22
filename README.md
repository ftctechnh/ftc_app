# Swerve Robotics FTC Library

Welcome to the Swerve Robotics library for the FTC robot controller runtime.
The purpose of our library is to augment the robot controller runtime library from FTC HQ
in order to simplify programming for FTC teams. The central aim here is not to change what's there,
just to make it better. The library is a drop-in replacement: if your code works with the FTC HQ
release, it will work with the Swerve library, but you can also take advantage of new things.
Please consult the [GitHub release notes](https://github.com/SwerveRobotics/ftc_app/releases) 
for details regarding a given release. You might also want to check out our related project, the 
Swerve Robotics Tools Suite, also [here](https://github.com/SwerveRobotics/tools) on GitHub. 

Notable features of the Swerve Robotics FTC Library include the following.

### Easy Legacy Motor Controller
**EasyLegacyMotorController** is a replacement for the stock DCMotorController implementation
used with legacy HiTechnic motor controllers. EasyLegacyMotorController does away with the need to manually
switch motors from read mode to write mode and back again and the attendant complex delay management,
loop counting, or waiting for hardware cycles that that requires. Just call setPower(), getPosition(),
or whatever other motor methods you need to use, and the right things happen under the covers.

EasyLegacyMotorController can be used from a LinearOpMode, SynchronousOpMode, or indeed any thread
that can tolerate a call that might take many tens of milliseconds to execute (it is specifically
*not* recommended that EasyLegacyMotorController be used on the loop() thread). In SynchronousOpModes,
EasyLegacyMotorController is used automatically; in other OpModes it can be used by
calling ClassFactory.createEasyMotorController(). 

### Alternate OpMode Registration 
The library has an **alternate OpMode registration mechanism** (the old FtcOpModeRegister.register() still works too)
that allows you to register your own OpModes simply by decorating them with @TeleOp or @Autonomous annotations.
This helps promote clean living and easier integration of library updates over time by avoiding
editing code that lives in libraries owned by others. To register OpModes that aren't your own,
a related annotation, @OpModeRegistrar, can be placed on a method in your code which is to be called
as part of the registration process. Take a look at the YourCodeHere module for an example of
how this works. We'd like to thank [dmssargent](https://github.com/dmssargent/Xtensible-ftc_app/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/FtcOpModeRegister.java)
for illustrating how this all might be technically accomplished.

### SynchronousOpMode
The library contains a [**SynchronousOpMode**](https://htmlpreview.github.io/?https://github.com/swerverobotics/ftc_app/blob/master/SwerveRoboticsLibrary/doc/javadoc/org/swerverobotics/library/SynchronousOpMode.html)
class that brings back the synchronous, linear programming style
with which teams have been familiar with from previous seasons in RobotC, and which is more amenable
to teaching to beginning programmers than the event-driven / loop() callback programming
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
on the dashboard, and / or the lines can be created dynamically with addData() calls as in
the robot controller runtime. You call telemetry.update() to compose
the current dashboard and transmit to the driver station. Only a subset of update() calls
actually transmit, saving bandwith on the network and data acquistion time on the controller.
Log messages can be written to the log at any time, and these are sent to the driver station as
soon as possible. The enhanced telemetry class can be used both by synchronous and non-synchronous
opmodes, but is used automatically in SynchronousOpModes.

### Easy I2C Programming
The library contains an **I2cDeviceClient** class that wraps I2cDevice instances and makes them easy to use by handling
read-vs-write mode switches and attendant waits automatically and transparently. Just call read8()
or write8() (and friends) to read and write device registers and the rest is taken care of.
With the I2C register map you get from the sensor manufacturer in hand, it's now just dead easy to
write your own code to talk to new I2C devices. Note that I2cDeviceClient is also decoupled
from SynchronousOpMode, in that one need not be using SynchronousOpMode to use I2cDeviceClient.
However as some operations are lengthy, a worker thread is suggested in that case in order to avoid
long-running operations on the loop() thread.

### AdaFruit IMU Support
The library contains a class that is built on I2cDeviceClient that provides a semantic interface to the **Bosch BNO055 absolute
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
*   Implement your code in a main() method whose signature is:

        @Override protected void main() throws InterruptedException
*   Initialize your hardware variables at the top of main() instead of in start(). Otherwise,
    the use of hardware objects (DcMotor, Servo, GamePads, etc) is the same as in the usual robot
    controller runtime.
*   The core of the body of main() for a typical TeleOp OpMode should look like

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
after the waitForStart().

Migrating from LinearOpMode to SyncronousOpMode is easy, usually simply involving 

* changing the OpMode base class to SynchronousOpMode from LinearOpMode
* changing the name of your runOpMode() method to main()
* adding 'telemetry.update()' and 'this.idle()' to the bottom of your while(opModeIsActive()) loop
* optionally removing waitOneFullHardwareCycle() calls, as they are no longer necessary

The Swerve Library now appears to be quite stable and functional. Our own teams are actively
developing their competition code using it. It currently is synchronized to the release from
FTC HQ that was published November 4th, 2015. Please be sure to **update your driver station**
app to the latest-available version.

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
If that's greek to you, gives is a ring, and we'll help you through it.
 
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
