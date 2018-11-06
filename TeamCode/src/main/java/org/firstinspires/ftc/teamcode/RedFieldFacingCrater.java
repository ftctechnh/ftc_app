package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled
@Autonomous(name="RedFieldFacingCrater")
public class RedFieldFacingCrater extends LinearOpMode
{
    NewHardware robot = new NewHardware();
    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;

    @Override
    public void runOpMode()
    {
        unliftRobo();
        liftRobo();



    }






    public void unliftRobo()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.3) {

            robot.liftBot2.setPower(-0.5);
            robot.liftBot.setPower(0.5);
            sleep(2000);
            robot.rightDrive.setPower(FORWARD_SPEED);
            robot.leftDrive.setPower(-FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.liftBot.setPower(0);
        robot.liftBot2.setPower(0);
    }

    public void driveForward()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.1) {
            robot.rightDrive.setPower(FORWARD_SPEED);
            robot.leftDrive.setPower(-FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    public void turnLeft()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.5){
            robot.rightDrive.setPower(TURN_SPEED);
            robot.leftDrive.setPower(TURN_SPEED);
        }
        robot.rightDrive.setPower(0);
        robot.leftDrive.setPower(0);
    }

    public void turnRight()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.5)
        {
            robot.rightDrive.setPower(-TURN_SPEED);
            robot.leftDrive.setPower(-TURN_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

    }

    public void liftRobo()
    {

        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.3) {

            robot.liftBot2.setPower(0.5);
            robot.liftBot.setPower(-0.5);
            sleep(2000);
            robot.rightDrive.setPower(-FORWARD_SPEED);
            robot.leftDrive.setPower(-FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.liftBot.setPower(0);
        robot.liftBot2.setPower(0);
    }
    public void driveBackwards()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.0) {
            robot.rightDrive.setPower(-FORWARD_SPEED);
            robot.leftDrive.setPower(FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }
    /*public void mineralID()
    {
        while (opModeIsActive()){
            robot.sensor.enableLed(true);
            Color.RGBToHSV(robot.sensor.red(), robot.sensor.green(), robot.sensor.blue(), hsvValues);
            telemetry.addData("Clear", robot.sensor.alpha());
            telemetry.addData("argb",robot.sensor.argb());
            telemetry.addData("Red", robot.sensor.red());
            telemetry.addData("Green", robot.sensor.green());
            telemetry.addData("Blue", robot.sensor.blue());
            telemetry.update();

            while(robot.sensor.alpha() < 33 && robot.sensor.argb() < 33 && robot.sensor.red() < 33 && robot.sensor.green() < 33 && robot.sensor.blue() < 33)
            {
                driveForward();

            }
            turnLeft();
            driveForward();

        }


    }*/




    }
















