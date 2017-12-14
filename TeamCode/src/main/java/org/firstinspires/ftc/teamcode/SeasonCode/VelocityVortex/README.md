#Team 5893's Code for FTC Velocity Vortex



Hello there, here's a quick run-down of our team's code.

This program structure aims to make the code flexible, easy to edit, easy to debug, and easy to
understand. To understand it, break up the robot into its core components. In the case of our robot,
 we'll have:
  * A drivetrain
  * A flywheel
  * A harvester
  * A lift, and
  * A cap ball spool.

  Each of these components are unique and have their own functions. As such, each merits its own
  class.


  ### But what about the other classes that I see?
  The robot is more than the sum of its parts. We need several classes to hold these components
  together and to ensure that it functions correctly. Once example is the "Robot" class. This
  class holds our robot's list of hardware objects and only ***extremely basic functions***, one of
  which is hardware mapping. You may have also noticed the "Hardware" class. This class manages
  hardware mapping so that our components even work in the first place. How else is the program
  supposed to know exactly which motor you're trying to talk to?


  ### Okay, so what about the other ones you haven't mentioned?
  Sometimes, we need to perform a complex function that has to potential to be needed outside
  of one component's sphere of influence. These functions need to be:
  1. Usable to more than 1 class
  2. Completely separate from robot core components
  3. Complex enough to merit its own method

  This is where the Utility classes come in. Simpler functions may be grouped together in the
  UtilBasic class, but more complex functions should deserve their own class such as UtilToggle.


  ### Cool, but now I want to dev. Any protocols or conventions to keep in mind?
  Yes, actually. Here they are:
  * [Allman Style](https://en.wikipedia.org/wiki/Indent_style#Allman_style) is used.
  * For floating-point numbers, use `double` rather than `float`.
  * When naming hardware, take the hardware type and then add a brief description. For example,
    when declaring a motor that raises the lift, the appropriate declaration would be `motorLift`
  * Class naming is as follows:
    * If the class is a utility class, append "Util" to the front
    * If the class is an opmode, append "Op" to the front
  * Private member variables have an underscore in front of them.
  * Measurement units are as follows:
    * Distance: Centimeters
    * Angles: Degrees
    * Time: Milliseconds
