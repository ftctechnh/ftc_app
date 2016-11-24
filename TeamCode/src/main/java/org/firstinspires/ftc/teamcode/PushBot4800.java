package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by FTC Team 4799-4800 on 10/13/2016.
 */

@Autonomous(name = "PushBot4800", group = "")
public class PushBot4800 extends OpMode {
    //DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    //DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotor revMotor;
    ModernRoboticsI2cColorSensor cs;
    Servo buttonPusher;
    Servo launcher;
    ModernRoboticsUsbDeviceInterfaceModule interfaceModule;
    double cs1;
    double cs2;
    double cs3;
    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm");
        revMotor = hardwareMap.dcMotor.get("RevMotor");
        launcher = hardwareMap.servo.get("Launcher");

        cs = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "CS");
        buttonPusher = hardwareMap.servo.get("buttonPusher");
        interfaceModule = hardwareMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "CDI");

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void loop() {
        arm.setPower(1);
        arm.setTargetPosition(3500);
        if (getRuntime()>5){
            revMotor.setPower(0-1*(1.3/3));
            if (getRuntime()>10)
            launcher.setPosition(0);
        }
        if (getRuntime()>11){
            launcher.setPosition(1);
            revMotor.setPower(0);
        }
        if ((getRuntime()>12)&&(getRuntime()<14)){
            arm.setTargetPosition(0);
            arm.setPower(-1);
            motorBackLeft.setPower(-1);
            motorFrontLeft.setPower(0.6);
            motorBackRight.setPower(1);
            motorBackRight.setPower(-0.6);
            telemetry.addData("I'm doing it",arm.isBusy());
        }


        telemetry.addData("amIBusy()",arm.isBusy());
        telemetry.update();
    }
}
