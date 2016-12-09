package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Blue Beacons Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousBlueBeacons extends _AutonomousBase
{
    //Autonomous code for the Blue alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        //Make color sensor available.
        rightSensorServo.setPosition(RIGHT_SERVO_OPEN);

        //Drive to hit the cap ball, and then come back.
        driveForTime(0.6, 2000); //Drive and hit the cap ball.
        driveForTime(-0.6, 500); //Launch the projectiles here.

        /******** PROJECTILE LAUNCH SEQUENCE GOES HERE ********/

        /********    END PROJECTILE LAUNCH SEQUENCE    ********/

        //Drive toward the beacons
        turnToHeading(90); //Turn 90 degrees toward the wall.
        driveForTime(0.6, 1000); //Drive to the wall.
        turnToHeading(-90); //Turn to face the white lines and the beacon.

        //Drive to the first color line.
        zeroHeading();
        setMovementPower(0.6);
        while (opModeIsActive() && bottomColorSensor.alpha() < 10)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();
        adjustHeading();

        /********   BUTTON PUSHING SEQUENCE GOES HERE   ********/
        driveForTime (-0.3, 200);//Back up to the first option.

        //Center self in front of button
        if (rightColorSensor.red() > 10)
            driveForTime(0.3, 400);

        //Push Button
        pushButton();
        /********        END BUTTON PUSH SEQUENCE       ********/

        //Drive a little ways forward from the first line
        driveForTime(0.5, 400);

        //Drive to the second color line.
        zeroHeading();
        setMovementPower(0.6);
        while (opModeIsActive() && bottomColorSensor.alpha() < 10)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();
        adjustHeading();

        /********   BUTTON PUSHING SEQUENCE GOES HERE   ********/
        driveForTime (-0.3, 200);//Back up to the first option.

        //Center self in front of button
        if (rightColorSensor.red() > 10)
            driveForTime(0.3, 400);

        //Push Button
        pushButton();
        /********        END BUTTON PUSH SEQUENCE       ********/
    }
}