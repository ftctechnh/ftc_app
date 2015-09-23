# Swerve Robotics FTC Library

Welcome to the Swerve Robotics library support for the FTC Android Robot Controller Library.
The purpose of the Swerve Robotics library is to augment the robot controller runtime from FTC HQ
in order to simplify programming for FTC teams. The central aim here is not to change what's there,
just to make it better. You might also want to check out our related project, the Swerve Robotics Tools
Suite, also [here](https://github.com/SwerveRobotics/tools) on GitHub.

Notable features of the Swerve Robotics FTC Library include:

*   A [*SynchronousOpMode*]
    (https://cdn.rawgit.com/SwerveRobotics/ftc_app/master/doc/javadoc/org/swerverobotics/library/SynchronousOpMode.html)
    class that brings back the synchronous, linear programming style
    with which teams have been familiar with from previous seasons in RobotC, and which is more amenable
    to teaching to beginning programmers than the event-driven / loop() callback programming
    model native to the robot controller runtime. SynchronousOpMode is similar to [LinearOpMode]
    (https://cdn.rawgit.com/ftctechnh/ftc_app/master/doc/javadoc/com/qualcomm/robotcore/eventloop/opmode/LinearOpMode.html)
    but contains several enhancements and a few fixes. Most notable for those using the legacy
    NXT/HiTechnic motor controllers is the automatic handling of the tedious manual mode switching, multi-loop-cycle
    delay management, and loop-cycle operation compatibility rules which are otherwise necessary when
    when switching between reading and writing operations (getPosition() vs setPower(), for example)
    when using legacy NXT/HiTechnic motor controllers. SynchronousOpMode also gives you precise
    control of when changes in gamepad state are made visible to your program, allowing you to
    safely reason about a given state across a possibly complicated chain of logic.
*   An enhanced form of telemetry containing a *dashboard* and a *log*. On the driver station display,
    the dashboard appears at the top, followed by as many of the recent log messages as will reasonably 
    fit. The dashboard can be preconfigured just once with unevaluated computations to form the lines
    on the dashboard, and / or the lines can be created dynamically with addData() calls as in
    the robot controller runtime. You call telemetry.update() to compose
    the current dashboard and transmit to the driver station. Only a subset of update() calls
    actually transmit, saving bandwith on the network and data acquistion time on the controller.
    Log messages can be written to the log at any time, and these are sent to the driver station as
    soon as possible. The enhanced telemetry class can be used both by synchronous and non-synchronous
    opmodes.
*   An *I2cDeviceClient* class that wraps I2cDevice instances and makes them easy to use by handling
    read-vs-write mode switches and attendant waits automatically and transparently. Just call read8()
    or write8() (and friends) to read and write device registers and the rest is taken care of.
    Note that I2cDeviceClient is also decoupled from SynchronousOpMode, in that one need not
    be using SynchronousOpMode to use I2cDeviceClient. However as some operations are lengthy, a
    worker thread is suggested in that case in order to avoid long-running operations on the loop() thread.
*   A class that is built on I2cDeviceClient that provides a semantic interface to the *Bosch BNO055 absolute
    position sensor*, allowing teams to make easy use of the [AdaFruit breakout board](http://www.adafruit.com/products/2472)
    which incorporates that sensor module. Features of this sensor include a gyro that does rate
    integration in hardware to provide robust and accurate angular position indications, and a
    separation of the output of the accelerometer into gravity and linear-motion-induced components.
    The class builds on the latter to provide linear velocity and position measurements using integration
    in software, which can be straightforwardly used for inertial dead reckoning for your robot.
    
The fifteen second summary of how to use SynchronousOpMode is as follows:

*   Inherit your opmode from SynchronousOpMode instead of OpMode or LinearOpMode.
*   Implement your code in a main() method whose signature is:

        @Override protected void main() throws InterruptedException
*   Initialize your hardware variables at the top of main() instead of in start(). Otherwise,
    the use of hardware objects (DcMotor, Servo, GamePads, etc) is the same as in the usual robot
    controller runtime.
*   The core of the body of main() should look like

        // Initialize stuff (not shown)
        
        // Wait for the game to start
        this.waitForStart(); 
        
        while (this.opModeIsActive()) {
            if (this.updateGamePads()) {
                // Do something interesting
                }
            this.telemetry.update();
            this.idle();
            }

That's it!

While the library is still undergoing development, it is quite stable, and our own teams
are actively developing their competition code using it. It currently is synchronized to the 
FTC headquarters beta release; when FTC HQ updates to a final release for the season, we will 
synchronize with that as fast as we are able. 

To use the library, we recommend forking or cloning our repository and working off of the 
'master' branch. The Swerve Library repository *includes* the robot controller runtime
repository from FTC HQ; you don't need both. While we do tag major milestones in the library
and 'release' them, we try to keep the master branch always stable and fully functional, so you
could reasonably sync to the latest available if you wished. Alternately, instead of forking
or cloning, you can download a full copy of the source in .zip form from one of our releases.
 
Documentation is available in the SwerveRoboticsLibrary/doc/javadoc directory.
There are also several examples of using the library to be found in the usual 'opmodes'
directory (alongside the examples provided in the core FTC SDK).

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

