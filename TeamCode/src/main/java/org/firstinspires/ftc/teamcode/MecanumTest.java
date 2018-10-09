package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Recharged Orange on 9/27/2018.
 */
@TeleOp(name = "mecenum test")

public class MecanumTest extends LinearOpMode {

    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor rightBack;

    Servo Nate;

    double drive ;
    double strafe;
    double rotate;


    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;

    @Override
    public void runOpMode() {


        initialization();
        waitForStart();
        while (opModeIsActive()) {
            mecanumDrive();
            servoControls();
        }
    }


    public void mecanumDrive() {

        double lt = gamepad1.left_trigger;
        double rt = gamepad1.right_trigger;
        double ly = -gamepad1.left_stick_y;
        double ry = -gamepad1.right_stick_y;

        double d = (ly + ry) / 2;
        double s = rt - lt;
        double r = (ly - ry) / 2;


        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        // You might have to play with the + or - depending on how your motors are installed
        /*frontLeftPower = drive + strafe + rotate;
        backLeftPower = drive - strafe + rotate;
        frontRightPower = drive - strafe - rotate;
        backRightPower = drive + strafe - rotate;*/

        frontLeftPower = d + s + r;
        backLeftPower = d - s + r;
        frontRightPower = d - s - r;
        backRightPower = d + s - r;

        leftBack.setPower(-backLeftPower);
        leftFront.setPower(-frontLeftPower);
        rightBack.setPower(backRightPower);
        rightFront.setPower(frontRightPower);

    }


    public void initialization() {


        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Nate = hardwareMap.servo.get("Nate");
    }

    public void servoControls() {
if (gamepad1.left_bumper){
    Nate.setPosition(1);
}
else Nate.setPosition(0);
    }


}
