package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by adityamavalankar on 7/22/17.
 */

@Autonomous(name = "encoder")
@Disabled
public class demo_encoderbasic extends LinearOpMode {

    //initialization, like setup
    public double encoder_ticks_per_rotation;    // eg: TETRIX Motor Encoder
    public double gear_ratio;     // 56/24
    public double wheel_circumference;     // For figuring circumference
    public double encoder_ticks_per_cm;

    DcMotor rightMotor;
    DcMotor leftMotor;

    public void runOpMode(){

        //also initialization

        encoder_ticks_per_rotation = 1440;
        gear_ratio = 1.5;
        wheel_circumference = 9.1* Math.PI;
        encoder_ticks_per_cm = (encoder_ticks_per_rotation) /
                (wheel_circumference * gear_ratio);


        leftMotor= hardwareMap.dcMotor.get("leftMotor");
        rightMotor=  hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        //running code

    }

    //where you put functions
    public void encoderDrive(double speed,
                             double leftCM, double rightCM) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;

        //sets motors to move to set position.
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets new position to run to based off encoders
        newLeftTarget = leftMotor.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newRightTarget = rightMotor.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);
        rightMotor.setTargetPosition(newRightTarget);
        leftMotor.setTargetPosition(newLeftTarget);


        //starts motion
        if (Math.abs(leftCM) > Math.abs(rightCM)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightCM) / leftCM;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftCM) / rightCM;
        }

        leftMotor.setPower(Math.abs(leftSpeed));
        rightMotor.setPower(Math.abs(rightSpeed));



        //continue moving
        while (opModeIsActive() &&
                ((leftMotor.isBusy() && rightMotor.isBusy()))) {
        }

        // Stop all motion;
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }
}
