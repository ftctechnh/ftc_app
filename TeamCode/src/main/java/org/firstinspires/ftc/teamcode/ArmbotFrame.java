package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
 * Created by Ian Ramsey on 9/19/2015. Updated to include arm functionality 5/3/16.
 */
@TeleOp(name = "ArmBot", group = "")
public class ArmbotFrame extends OpMode {
    boolean beaconMode;
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotorController armController;
    DcMotor motorRev;
    DcMotorController revController;
    ServoController launchController;
    Servo launcher;
    Servo buttonPusher;
    float rightthrottle;
    float leftthrottle;

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm");
        motorRev = hardwareMap.dcMotor.get("RevMotor");
        launcher = hardwareMap.servo.get("Launcher");
        buttonPusher = hardwareMap.servo.get("ButtonPusher");
    }

    public void loop() {
        if (gamepad1.a)
            beaconMode = true;
        if (gamepad1.b)
            beaconMode = false;
        if (!beaconMode) {
            leftthrottle = -gamepad1.left_stick_y;
            rightthrottle = -gamepad1.right_stick_y;
        }
        else{
            leftthrottle = gamepad1.right_stick_y;
            rightthrottle = gamepad1.left_stick_y;
            if (gamepad1.left_bumper)
                buttonPusher.setPosition(-1);
            if (gamepad1.right_bumper)
                buttonPusher.setPosition(1);
        }
        float armthrottle = -gamepad2.left_stick_y;
        float revthrottle = gamepad2.right_stick_y;

        motorBackLeft.setPower(-rightthrottle);
        motorFrontLeft.setPower(.69*rightthrottle);
        motorBackRight.setPower(leftthrottle);
        motorFrontRight.setPower(.69*-leftthrottle);
        arm.setPower(armthrottle);
        motorRev.setPower(-revthrottle);
        if (gamepad2.right_bumper)
            launcher.setPosition(-1);
        else
            launcher.setPosition(1);
    }
}
