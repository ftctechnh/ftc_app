package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Harshini and Satvik on 11/29/2017.
 */
@TeleOp(name = "CallibrationPivots", group = "Cal")
public class Callibration_NewRobot extends LinearOpMode
{
    NewRobotFinal robot;

    public void runOpMode()
    {
        robot = new NewRobotFinal(hardwareMap);
        robot.initAutoFunctions(hardwareMap);

        waitForStart();

        robot.pivot(45, .5);

        while(!gamepad1.a){}
        robot.pivot(90, .5);

        while(!gamepad1.a){}

        robot.pivot(-45, .5);

        while(!gamepad1.a){}

        robot.pivot(-90, .5);

        //while(!gamepad1.a){}
        //robot.pivot(1100, .5);

        robot.stopAllMotors();
    }

}