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

        while(opModeIsActive())
        {
            sleep(15000);
            robot.setShooterPowerOne(0.60f);
            robot.setShooterPowerTwo(-0.60f);
            robot.drive();
            sleep(1000);
            robot.setLifterPower(-0.7f);
            robot.drive();
            sleep(1000);
            robot.setScooperServoPos(0.0f);
            robot.drive();
            sleep(4000);
            robot.driveStraight(54, 180);
            sleep(100);
            robot.spin(-180);
            sleep(100);
            robot.driveStraight(8.5, 45);
        }
        robot.stop();
    }
}
