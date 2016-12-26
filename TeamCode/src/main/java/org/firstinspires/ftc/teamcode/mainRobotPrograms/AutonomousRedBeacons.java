package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Red Beacons Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousRedBeacons extends _AutonomousBase
{
    //Autonomous code for the Blue alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Make color sensor available.
        leftSensorServo.setPosition(RIGHT_SERVO_OPEN);

        //Drive forward a bit
        driveForTime(-0.9, 600);
        outputNewLineToDriverStation("Went forward");

        //Turn to face between the lines
        setInitialTurnPower(.16);
        setPrecisionFactor(20);
        turnToHeading(-24, TurnMode.BOTH);
        outputNewLineToDriverStation("Turned to face toward intermediary part of white line");
        int currentHeading = getValidGyroHeading();

        //Drive toward the wall.
        driveForTime(-0.9, 1800);
        outputNewLineToDriverStation("Driving forward toward that part.");

        //Turn to face the wall
        setPrecisionFactor(24);
        turnToHeading(-currentHeading - 90, TurnMode.BOTH);
        outputNewLineToDriverStation("Turning to face wall.  ");

        //Drive to the wall until touch sensor registered.
        zeroHeading();
        setMovementPower(-0.5);
        while(touchSensor.isPressed() == false)
            idle();
        stopDriving();
        //The wall resets our heading (YES)
        zeroHeading();
        sleep(300);
        driveForTime(0.1, 5);

        setInitialTurnPower(.2);
        setPrecisionFactor(4);
        turnToHeading(-92, TurnMode.LEFT); //Back facing to near beacon

        driveForTime(0.2, 1200);

        //Drive to the first color line.
        zeroHeading();
        setMovementPower(-0.2);
        while (bottomColorSensor.alpha() < 10)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();
        adjustHeading();

        /********   BUTTON PUSHING SEQUENCE GOES HERE   ********/

        driveForTime(0.1, 700);

        if (leftColorSensor.red() >= 2 && leftColorSensor.blue() <= 2)
            pushButton();
        else
        {
            driveForTime(0.2, 550);
            pushButton();
        }

        outputNewLineToDriverStation("red is " + rightColorSensor.red() + " blue is " + rightColorSensor.blue());

        setPrecisionFactor(2);
        adjustHeading();

        /********        END BUTTON PUSH SEQUENCE       ********/

        //Drive a little ways forward from the first line
        driveForTime(-0.2, 800);

        //Drive to the second color line.
        zeroHeading();
        setMovementPower(-0.2);
        while (opModeIsActive() && bottomColorSensor.alpha() < 10)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();
        adjustHeading();

        /********   BUTTON PUSHING SEQUENCE GOES HERE   ********/

        driveForTime(0.1, 700);

        if (leftColorSensor.red() >= 2 && leftColorSensor.blue() <= 2)
            pushButton();
        else
        {
            driveForTime(0.2, 550);
            pushButton();
        }

        /********        END BUTTON PUSH SEQUENCE       ********/

    }

    //Should be constant among all classes.
    protected void pushButton() throws InterruptedException
    {
        //Press button
        pusher.setPower(-.12);
        sleep(1800);
        pusher.setPower(.12);
        sleep(1600);
        pusher.setPower(0);

    }

    @Override
    protected void driverStationSaysSTOP()
    {
        leftSensorServo.setPosition(LEFT_SERVO_CLOSED);
    }
}