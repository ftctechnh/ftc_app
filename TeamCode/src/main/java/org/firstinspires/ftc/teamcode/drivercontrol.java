package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name="tiltmotortest")
public class drivercontrol extends OpMode {
    DcMotor frontRightDrive;
    DcMotor frontLeftDrive;
    DcMotor backRightDrive;
    DcMotor backLeftDrive;
    DcMotor armLiftMotorTop;
    DcMotor armLiftMotorBottom;
    DcMotor armTiltMotor;
    DcMotor Wrist;

    // TODO <SUGAR> Class-local field names should not start with uppercase letters
    Servo armServo;
    Servo markerServo;
    GyroSensor gyro;
    ColorSensor colorSensor;
    DigitalChannel magLimitSwitchTilt;
    Servo Finger1;
    Servo Finger2;
    @Override
    public void init() {

        armTiltMotor = hardwareMap.dcMotor.get("arm_tilt_motor");
    }

    @Override
    public void loop() {
        if (gamepad2.right_bumper) {
            armTiltMotor.setPower(.7);
        } else if (gamepad2.left_bumper) {
            armTiltMotor.setPower(-.7);
        } else {
            armTiltMotor.setPower(0);
        }

        if (gamepad1.right_bumper) {
            armTiltMotor.setPower(.5);
        } else if (gamepad1.left_bumper) {
            armTiltMotor.setPower(-.5);
        } else {
            armTiltMotor.setPower(0);
        }
    }
}