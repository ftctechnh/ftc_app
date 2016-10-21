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
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotorController armController;
    DcMotor motorRev1;
    DcMotor motorRev2;
    DcMotorController revController;
    ServoController launchController;
    Servo launcher;

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        armController = hardwareMap.dcMotorController.get("ArmController");
        revController = hardwareMap.dcMotorController.get("RevController");
        motorRev1 = hardwareMap.dcMotor.get("RevMotor1");
        motorRev2 = hardwareMap.dcMotor.get("RevMotor2");
        launchController = hardwareMap.servoController.get("LaunchController");
        launcher = hardwareMap.servo.get("Launcher");
    }

    public void loop() {
        float leftthrottle = -gamepad1.left_stick_y;
        float rightthrottle = -gamepad1.right_stick_y;
        float armthrottle = -gamepad2.left_stick_y;
        float revthrottle = gamepad2.right_stick_y;

        motorBackLeft.setPower(rightthrottle);
        motorFrontLeft.setPower(rightthrottle);
        motorBackRight.setPower(-leftthrottle);
        motorFrontRight.setPower(-leftthrottle);
        arm.setPower(-armthrottle);
        motorRev1.setPower(revthrottle);
        motorRev2.setPower(-revthrottle);
        if (gamepad2.right_bumper)
            launcher.setPosition(180);
        else
            launcher.setPosition(0);
    }
}
