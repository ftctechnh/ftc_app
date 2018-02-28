package org.firstinspires.ftc.teamcode.Year_2017_18.Robot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name="RobotCommands", group="AutoMode")
@Disabled

public class RobotCommands extends LinearOpMode{

    public RobotHardware hardware = new RobotHardware();
    public RobotCommands() {}

    public void runOpMode() {
        hardware.init(hardwareMap);
    }

    public void setHardwareMap(HardwareMap hw)
    {
        hardware.init(hw);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Commands for driving.
    public void drive(double leftSpeed, double rightSpeed, int timeInMilliseconds) throws InterruptedException{
        hardware.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.leftDrive.setPower(leftSpeed);
        hardware.rightDrive.setPower(rightSpeed);
        sleep(timeInMilliseconds);
        hardware.leftDrive.setPower(0);
        hardware.rightDrive.setPower(0);
    }

    //Commands for the main claws.
    public void pulley(float power, int timeInMilliseconds) {
        hardware.pulleyArm.setPower(power);
        sleep(timeInMilliseconds);
        hardware.pulleyArm.setPower(0);
    }

    public void claw_grab() {
        hardware.leftClaw.setPosition(0.8);
        hardware.rightClaw.setPosition(0.8);
    }

    public void claw_release() {
        hardware.leftClaw.setPosition(0);
        hardware.rightClaw.setPosition(0.1);
    }

    //Commands for sensor arm.
    public void sensor_left() {hardware.sensorArm.setPosition(0);}

    public void sensor_right() {hardware.sensorArm.setPosition(1);}

    public void sensor_middle() {hardware.sensorArm.setPosition(0.5);}

    public void color_rotate (double position) {hardware.colorRotate.setPosition(position);}
}