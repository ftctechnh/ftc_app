package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Red Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousRedCapBall extends _AutonomousBase
{
    //Autonomous code for the Red alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        zeroHeading();
        setMovementPower(-0.5);
        while (touchSensor.isPressed() == false)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();
        adjustHeading();

        setPrecision(4);
        turnToHeading(45, turnMode.BOTH);

        zeroHeading();
        setMovementPower(0.5);
        while (bottomColorSensor.red() <= 2)
            updateMotorPowersBasedOnGyroHeading();
        stopDriving();

        driveForTime(0.7, 700);

        harvester.setPower(-0.5);
        sleep(2000);
        harvester.setPower(0.5);
    }
}