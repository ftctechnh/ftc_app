## Omegas Module

Welcome!

This module, Omegas, is the place where FTC 6504 (the Omegas) will write/paste the code for your team's robot controller App. This module currently only contains templates for older robots and library usage.

## Using our app:  Cloning the Omegas Module

In some situations, you have multiple teams in your club and you want them to all share
a common code organization, with each being able to *see* the others code but each having
their own team module with their own code that they maintain themselves.

In this situation, you might wish to clone the Omegas module, once for each of these teams.
Each of the clones would then appear along side each other in the Android Studio module list,
together with the FtcRobotController module (and the original TeamCode module).

Selective Team phones can then be programmed by selecting the desired Module from the pulldown list
prior to clicking to the green Run arrow.

Warning:  This is not for the inexperienced Software developer.
You will need to be comfortable with File manipulations and managing Android Studio Modules.
These changes are performed OUTSIDE of Android Studios, so close Android Studios before you do this.
 
Also.. Make a full project backup before you start this :)

To clone Omegas, do the following:

Note: Some names start with "Team" and others start with "team".  This is intentional.

1.  Using your operating system file management tools, copy the whole "TeamCode"
    folder to a sibling folder with a corresponding new name, eg: "Team6504".

2.  In the new Team6504 folder, delete the TeamCode.iml file.

3.  the new Team6504 folder, rename the "src/main/java/org/firstinspires/ftc/teamcode" folder
    to a matching name with a lowercase 'team' eg:  "Team6504".

4.  In the new Team6504/src/main folder, edit the "AndroidManifest.xml" file, change the line that contains
         package="org.firstinspires.ftc.teamcode"
    to be
         package="org.firstinspires.ftc.Team6504"

5.  Add:    include ':Team6504' to the "/settings.gradle" file.
    
6.  Open up Android Studios and clean out any old files by using the menu to "Build/Clean Project""

## Documentation

The Javadoc reference documentation for the FTC SDK is now available online.  Visit the following URL to view the FTC SDK documentation as a live website:

http://ftctechnh.github.io/ftc_app/doc/javadoc/index.html

Documentation for the FTC SDK is also included with this repository.  There is a subfolder called "doc" which contains several subfolders:

 * The folder "apk" contains the .apk files for the FTC Driver Station and FTC Robot Controller apps.
 * The folder "javadoc" contains the JavaDoc user documentation for the FTC SDK.
 * The folder "tutorial" contains PDF files that help teach the basics of using the FTC SDK.

Note: Special thanks to LASA for the FTC Vision library.
