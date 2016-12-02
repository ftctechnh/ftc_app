package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter G on 12/1/2016.
 */
@Autonomous(name = "ShootFromWall", group = "Autonomous")
public class ShootFromWallAuto extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode()
    {
        robot.init(hardwareMap);

        waitForStart();

        robot.setShooterPowerOne(1.0f);
        robot.setShooterPowerTwo(-1.0f);
        robot.drive();
        sleep(1000);
        robot.setLifterPower(-1.0f);
        robot.drive();
        sleep(1000);
        robot.setScooperServoPos(0.0f);
        robot.drive();
        sleep(4000);
        robot.driveStraight(54, 180);
    }
}
