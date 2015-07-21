# Swerve Robotics FTC Library

Welcome to the Swerve Robotics library support for the FTC Android Robot Controller Library
(hereinafter known as the RCL). The purpose of the Swerve Robotics library is to augment the
RCL in order to simplify programming for FTC teams. Notable features of the library include:

*   A *synchronous OpMode* that brings back the synchronous, linear programming style
    with which teams have been familiar with from previous seasons, and which is more amenable
    to teaching to beginning programmers than the event-driven / loop() callback programming
    model native to the RCL.
*   An enhanced form of telemetry containing a *dashboard* and a *log*. The contents of the 
    dashboard are configured just once, and update() is called periodically to send its contents
    to the driver station. Messages can be written to the log at any time, and these are sent to
    the driver station as soon as possible. On the driver station display, the dashboard appears
    at the top, followed by as many of the recent log messages as will reasonably fit.
    
The library is still undergoing development. It has received moderate testing, and no known bugs
currently exist, but undoubtedly some are there lurking to be discovered. Documentation is currently
very sparse. There are a couple of examples in the examples package, and the code itself is heavily
commented, but there is as yet no easily approachable tutorial or reference manual. We're working on
that. Finally, in its present form, the library is distributed solely in source form: clone the 
entire project to your local computer just as you would the official (beta) project release from
FTC headquarters. We realize that releasing only in source form can be cumbersome for integrating 
with a team's own code base, especially as new versions of the library are released. We're working 
on releasing in binary form (with full source provided as well to aid in debugging), but that's not 
yet available.

We'd love to hear what you think about the library. Please direct your feedback to 
    
    <swerveftclibrary@googlegroups.com>

Thanks!

Robert Atkinson,
bob@theatkinsons.org,
Mentor, Swerve Robotics,  
Woodinville, Washington

21 July 2015  
