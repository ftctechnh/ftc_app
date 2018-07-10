package org.firstinspires.ftc.teamcode.ftc2016to2017season.PwrPuffGirls_Official;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by mabel on 8/3/2017.
 */


@Autonomous(name = "powerpuffgirlssquare")
@Disabled
public class PowerPuffSquare extends LinearOpMode {

    public double encoder_ticks_per_rotation;
    public double gear_ratio;
    public double wheel_circumference;
    public double encoder_ticks_per_cm;

    DcMotor leftbackMotor;
    DcMotor rightbackMotor;
    DcMotor leftfrontmotor;
    DcMotor rightfrontmotor;

    public void runOpMode() {

        encoder_ticks_per_rotation = 1440;
        gear_ratio = 1;
        wheel_circumference = 10 * Math.PI;
        encoder_ticks_per_cm = (encoder_ticks_per_rotation) /
                (wheel_circumference * gear_ratio);


        rightbackMotor = hardwareMap.dcMotor.get("rightbackMotor");
        leftbackMotor = hardwareMap.dcMotor.get("leftbackmotor");
        rightfrontmotor = hardwareMap.dcMotor.get("rightfrontmotor");
        leftfrontmotor = hardwareMap.dcMotor.get("leftfrontmotor");

        rightbackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        encoderDrive(0.5, 100, 100);
        encoderDrive(0.5, -30, 30);
        encoderDrive(0.5, 100, 100);
        encoderDrive(0.5, -30, 30);
        encoderDrive(0.5, 100, 100);
        encoderDrive(0.5, -30, 30);
        encoderDrive(0.5, 100, 100);
        encoderDrive(0.5, -30, 30);
        encoderDrive(0.5, 100, 100);
    }

    public void encoderDrive(double speed,
                             double leftCM, double rightCM) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;


        rightbackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftbackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        newLeftTarget = leftbackMotor.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newRightTarget = rightfrontmotor.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);
        newLeftTarget = leftfrontmotor.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newRightTarget = rightbackMotor.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);

        rightfrontmotor.setTargetPosition(newRightTarget);
        leftfrontmotor.setTargetPosition(newLeftTarget);
        rightbackMotor.setTargetPosition(newRightTarget);
        leftbackMotor.setTargetPosition(newLeftTarget);

        if (Math.abs(leftCM) > Math.abs(rightCM)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightCM) / leftCM;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftCM) / rightCM;
        }

        leftbackMotor.setPower(Math.abs(leftSpeed));
        rightbackMotor.setPower(Math.abs(rightSpeed));
        leftfrontmotor.setPower(Math.abs(leftSpeed));
        rightfrontmotor.setPower(Math.abs(rightSpeed));

        while (opModeIsActive() && (leftfrontmotor.isBusy() && rightbackMotor.isBusy() && leftbackMotor.isBusy() && rightfrontmotor.isBusy())){

            rightbackMotor.setPower(0);
            rightfrontmotor.setPower(0);
            leftbackMotor.setPower(0);
            leftfrontmotor.setPower(0);
        }
    }
}

