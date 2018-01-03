package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Harshini and Satvik on 11/29/2017.
 */
@Autonomous(name = "CallibrationPivots", group = "Cal")
public class Callibration_NewRobot extends LinearOpMode
{
    NewRobotFinal robot;

    public void runOpMode()
    {
        robot = new NewRobotFinal(hardwareMap);
        robot.initVuforia(hardwareMap);

        waitForStart();

        robot.pivot(45, .5);

        while(!gamepad1.a){}
        robot.pivot(90, .5);

        while(!gamepad1.a){}

        robot.pivot(-45, .5);

        while(!gamepad1.a){}

        robot.pivot(-90, .5);

        robot.stopAllMotors();
        robot.kill();
        robot.killAuto();

    }

}