package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


//@Disabled
@Autonomous(name="AutoBot")
public class AutoBot extends LinearOpMode
{
    NewHardware robot = new NewHardware();
    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode()
    {
    robot.initialize(hardwareMap);


        telemetry.addData("Liftbot","Resetting");
        robot.leftDrive.getCurrentPosition();
        robot.rightDrive.getCurrentPosition();
        telemetry.update();
        waitForStart();



    }






    public void unliftRobo()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 5.0) {

            robot.liftBot2.setPower(-0.5);
            robot.liftBot.setPower(-0.5);

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
        while(opModeIsActive() && runtime.seconds() < 1.0) {
            robot.rightDrive.setPower(FORWARD_SPEED);
            robot.leftDrive.setPower(-FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
    }

    public void turnLeft()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.0){
            robot.rightDrive.setPower(TURN_SPEED);
            robot.leftDrive.setPower(TURN_SPEED);
        }
        robot.rightDrive.setPower(0);
        robot.leftDrive.setPower(0);
    }

    public void turnRight()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.0)
        {
            robot.rightDrive.setPower(TURN_SPEED);
            robot.leftDrive.setPower(TURN_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

    }

    public void liftRobo()
    {

        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 3.0) {

            robot.liftBot2.setPower(0.5);
            robot.liftBot.setPower(0.5);

            robot.rightDrive.setPower(-FORWARD_SPEED);
            robot.leftDrive.setPower(-FORWARD_SPEED);
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.liftBot.setPower(0);
        robot.liftBot2.setPower(0);
    }










}



