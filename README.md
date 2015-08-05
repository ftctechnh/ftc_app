# Swerve Robotics FTC Library

Welcome to the Swerve Robotics library support for the FTC Android Robot Controller Library
(hereinafter known as the RCL). The purpose of the Swerve Robotics library is to augment the
RCL in order to simplify programming for FTC teams. Notable features of the library include:

*   A *synchronous OpMode* that brings back the synchronous, linear programming style
    with which teams have been familiar with from previous seasons, and which is more amenable
    to teaching to beginning programmers than the event-driven / loop() callback programming
    model native to the RCL. SynchronousOpMode also automatically handles the delays necessary
    when switching between reading and writing modes while using legacy NXT/HiTechnic motor
    controllers, relieving programmers of the burden of doing so manually.
*   An enhanced form of telemetry containing a *dashboard* and a *log*. The contents of the 
    dashboard are configured just once, and update() is called periodically to send its contents
    to the driver station. Messages can be written to the log at any time, and these are sent to
    the driver station as soon as possible. On the driver station display, the dashboard appears
    at the top, followed by as many of the recent log messages as will reasonably fit.
    
The fifteen second summary of how to use the library is as follows:

*   Inherit your OpMode from SynchronousOpMode instead of OpMode.
*   Implement your code in a main() method whose signature is:

        @Override protected void main() throws InterruptedException
*   Initialize your hardware variables at the top of main() instead of in start(). Otherwise,
    the use of hardware objects (DcMotor, Servo, GamePads, etc) is the same as in the RCL, with 
    the single exception that the GamePad objects have methods rather than data, so you have to
    say, e.g., 
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

Note that the library is still undergoing development. It has received light testing, and while no known 
bugs currently exist, undoubtedly some are there lurking to be discovered. Documentation is currently
very sparse. There are a couple of examples in the examples package, and the code itself is heavily
commented, but there is as yet no easily approachable tutorial or reference manual. We're working on
that. Finally, in its present form, the library is distributed solely in source form: clone the 
entire project to your local computer just as you would the official (beta) project release from
FTC headquarters. We realize that releasing only in source form can be cumbersome for integrating 
with a team's own code base, especially as new versions of the library are released. We're working 
on releasing in binary form (with full source provided as well to aid in debugging), but that's not 
yet available.

We'd love to hear what you think about the library. Please direct your feedback to 
swerveftclibrary@googlegroups.com. Thanks!

Robert Atkinson,
bob@theatkinsons.org,
Mentor, Swerve Robotics,  
Woodinville, Washington

21 July 2015 (update 04 August)  

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

