package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 10/20/2017.
 */
@Disabled
public class RedAutoRef extends LinearOpMode
{
    private TankBase robot;
    public void runOpMode()
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        //Need to identify the cipher picture
        //Spin to knock Jewel out
        robot.pivot_IMU(30, .25);
        robot.pivot(-30, .25);
        robot.driveStraight_In(36f);
        robot.pivot_IMU(90f,.25);
        //Drive distance based on cipher
        robot.pivot_IMU(-90f,.25);
        robot.driveStraight_In(12f,.3);
        //Drop cipher in box
        robot.stopAllMotors();
    }
}
