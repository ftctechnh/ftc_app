package org.firstinspires.ftc.teamcode.ftc2016to2017season.chobani;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by zach on 7/23/17.
 */


@Autonomous(name = "encoding")
public class encodin extends LinearOpMode {

    //initialization, like setup
    public double encoder_ticks_per_rotation;    // eg: TETRIX Motor Encoder
    public double gear_ratio;     // 56/24
    public double wheel_circumference;     // For figuring circumference
    public double encoder_ticks_per_cm;

    //DcMotor rightMotor;
    //DcMotor leftMotor;
    DcMotor rightBack;
    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor leftBack;
    public void runOpMode(){

        //also initialization

        encoder_ticks_per_rotation = 1440;
        gear_ratio = 1.5;
        wheel_circumference = 9.1* Math.PI;
        encoder_ticks_per_cm = (encoder_ticks_per_rotation) /
                (wheel_circumference * gear_ratio);


        leftFront= hardwareMap.dcMotor.get("leftFront");
        leftBack= hardwareMap.dcMotor.get("leftBack");
        rightFront=  hardwareMap.dcMotor.get("rightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack=  hardwareMap.dcMotor.get("rightBack");
        rightBack.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        // 57.1428571 = 1 meter (for 0.5 power)

        // Driving foward slow
        encoderDrive(0.25, 115, 115);
        sleep(250);
        encoderDrive(0.25, -115, -115);

        // Driving foward Fast
        encoderDrive(0.5, 115, 115);
        sleep(250);
        encoderDrive(0.5, -115, -115);


        sleep(250);
        encoderDrive(0.5, -35, 35);

        //hi
    }

    //where you put functions
    public void encoderDrive(double speed,
                             double leftCM, double rightCM) {
        int newLeftTarget;
        int newLeftTarget2;
        int newRightTarget;
        int newRightTarget2;
        double leftSpeed;
        double rightSpeed;

        //sets motors to move to set position.
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets new position to run to based off encoders
        newLeftTarget = leftFront.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newLeftTarget2 = leftBack.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newRightTarget = rightFront.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);
        newRightTarget2 = rightBack.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);
        rightFront.setTargetPosition(newRightTarget);
        rightBack.setTargetPosition(newRightTarget2);
        leftFront.setTargetPosition(newLeftTarget);
        leftBack.setTargetPosition(newLeftTarget2);


        //starts motion
        if (Math.abs(leftCM) > Math.abs(rightCM)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightCM) / leftCM;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftCM) / rightCM;
        }

        leftFront.setPower(Math.abs(leftSpeed));
        leftBack.setPower(Math.abs(leftSpeed));
        rightFront.setPower(Math.abs(rightSpeed));
        rightBack.setPower(Math.abs(rightSpeed));



        //continue moving
        while (opModeIsActive() &&
                ((leftFront.isBusy() && rightBack.isBusy()))) {
        }

        // Stop all motion;
        rightBack.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        leftFront.setPower(0);
    }
}

