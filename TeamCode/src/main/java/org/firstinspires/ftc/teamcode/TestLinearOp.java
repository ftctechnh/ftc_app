package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Locale;

/**
 * Created by Jeremy on 8/27/2017.
 */
@Autonomous(name = "Test Stuff", group = "concept")
public class TestLinearOp extends LinearOpMode
{
    private TankBase robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        robot.updateIMUValues();
        telemetry.addData("Angle =", formatDegrees(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES, robot.angles.firstAngle)));
        telemetry.update();
        telemetry.addData("Angle =", formatDegrees(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES, robot.angles.firstAngle)));
        telemetry.update();
        telemetry.addData("Angle =", formatDegrees(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES, robot.angles.firstAngle)));
        telemetry.update();
        robot.pivot_IMU(90, .25);
        while(!gamepad1.a)
        {
            robot.updateIMUValues();
            telemetry.addData("Angle =", formatDegrees(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES, robot.angles.firstAngle)));
            telemetry.update();
        }
        robot.pivot_IMU(-90, .25);
        while(!gamepad1.a)
        {
            robot.updateIMUValues();
            telemetry.addData("Angle =", formatDegrees(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES, robot.angles.firstAngle)));
            telemetry.update();
        }
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
