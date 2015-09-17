# Swerve Robotics FTC Library

Welcome to the Swerve Robotics library support for the FTC Android Robot Controller Library.
The purpose of the Swerve Robotics library is to augment the robot controller library from FTC HQ 
in order to simplify programming for FTC teams. The aim is not to change what's there, just to make it
better. You might also want to check out our related project, the Swerve Robotics Tools 
Suite, also here on GitHub.

Notable features of the Swerve Robotics FTC Library include:

*   A *synchronous OpMode* that brings back the synchronous, linear programming style
    with which teams have been familiar with from previous seasons, and which is more amenable
    to teaching to beginning programmers than the event-driven / loop() callback programming
    model native to the RCL. SynchronousOpMode also automatically handles the delays necessary
    when switching between reading and writing modes while using legacy NXT/HiTechnic motor
    controllers, relieving programmers of the burden of doing so manually.
*   An enhanced form of telemetry containing a *dashboard* and a *log*. On the driver station display, 
    the dashboard appears at the top, followed by as many of the recent log messages as will reasonably 
    fit. The dashboard is configured just once, and you call update() periodically to compute current
    values and send its contents to the driver station (you can call it often; actual updates are 
    automatically throttled to avoid unnecessary bandwith use). Log messages can be written to the 
    log at any time, and these are sent to the driver station as soon as possible. 
*   An *I2cDeviceClient* class that wraps I2cDevice instances and makes them easy to use by handling
    read-vs-write mode switches and attendant waits automatically and transparently. Just call read8()
    or write8() (and friends) to read and write device registers and the rest is taken care of.
    Note that I2cDeviceClient is decoupled from the SynchronousOpMode work, in that one need not 
    be using SynchronousOpMode to use I2cDeviceClient (however as some operations are lengthy, a
    worker thread is suggested in that case in order to avoid long-running operations on the loop() thread).
*   A class that is built on I2cDeviceClient that provides a clean interface to the *Bosch BNO055 absolute 
    position sensor*, allowing teams to make easy use of the AdaFruit breakout board which incorporates 
    that sensor module. Features of this sensor include a gyro that does rate integration in hardware to provide robust and accurate
    angular position indications, and a robust separation of acceleration into gravity and linear-motion-induced
    components. The class builds on the latter to provide linear velocity and position indications
    using integration in software.
    
The fifteen second summary of how to use SynchronousOpMode is as follows:

*   Inherit your opmode from SynchronousOpMode instead of OpMode or LinearOpMode.
*   Implement your code in a main() method whose signature is:

        @Override protected void main() throws InterruptedException
*   Initialize your hardware variables at the top of main() instead of in start(). Otherwise,
    the use of hardware objects (DcMotor, Servo, GamePads, etc) is the same as in the usual robot
    controller runtime, with the single exception that the GamePad objects have methods rather than 
    data, so you have to say, e.g., 
        ```
        gamepad1.left_stick_y()
        ```
    instead of
        ```     
        gamepad1.left_stick_y
        ```.
*   The core of the body of main() should look like

        // Initialize stuff (not shown)
        
        // Wait for the game to start
        this.waitForStart(); 
        
        while (this.opModeIsActive()) {
            if (this.newGamePadInputAvailable()) {
                // Do something interesting
                }
            this.telemetry.dashboard.update();
            this.idle();
            }

That's it!

While the library is still undergoing development, it is quite stable, and our own teams
are actively developing their competition code using it. It currently is synchronized to the 
FTC headquarters beta release; when FTC HQ updates to a final release for the season, we will 
synchronize with that as fast as we are able. 

To use the library, we recommend forking or cloning our repository and working off of the 
'master' branch. While we do tag major milestones and 'release' them, we try to keep the master
branch always stable and fully functional, so you could reasonably sync to the latest
available if you wished. Alternately, instead of forking or cloning, you can download a 
full copy of the source in .zip form from one of our releases.
 
Documentation is available in the SwerveRoboticsLibrary/doc/javadoc directory.
There are also several examples of using the library to be found in the usual 'opmodes'
directory (alongside the examples provided in the core FTC SDK).

In its present form, the library is distributed solely in source form. We realize that releasing only 
in source form can be cumbersome for integrating with a team's own code base, especially as new versions 
of the library are released. We're working on releasing in binary form (with full source provided as 
well to aid in debugging), but that's not yet available.

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

