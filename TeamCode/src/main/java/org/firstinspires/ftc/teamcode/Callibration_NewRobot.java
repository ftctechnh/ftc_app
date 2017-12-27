package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Harshini and Satvik on 11/29/2017.
 */
@Disabled
@Autonomous(name = "Callibration", group = "Cal")
public class Callibration_NewRobot extends LinearOpMode
{
    private TankBase robot;

    public void runOpMode()
    {
        robot = new TankBase(hardwareMap);
        waitForStart();

        robot.pivot(45, .25);
        telemetry.addData("Angle = ", robot.getYaw());
        telemetry.update();

        while(!gamepad1.a){}
        robot.initIMU();
        robot.pivot(90, .25);
        telemetry.addData("Angle = ", robot.getYaw());
        telemetry.update();

        while(!gamepad1.a){}

        robot.initIMU();
        robot.pivot(-45, .25);
        telemetry.addData("Angle = ", robot.getYaw());
        telemetry.update();

        while(!gamepad1.a){}

        robot.initIMU();
        robot.pivot(-90, .25);
        telemetry.addData("Angle = ", robot.getYaw());
        telemetry.update();
    }

}