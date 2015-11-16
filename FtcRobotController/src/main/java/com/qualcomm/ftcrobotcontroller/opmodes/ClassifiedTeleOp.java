package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Carlos on 11/11/2015.
 */
public class ClassifiedTeleOp extends OpMode{


    Lift lift = new Lift();
    Drivetrain drivetrain = new Drivetrain();
    //Arm arm = new Arm();
    Intake intake = new Intake();
    Dumper dumper = new Dumper();
    ZiplineScorer ziplineScorer = new ZiplineScorer();

    Servo climberServo;

    @Override
    public void init() {
        lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        //arm.init(hardwareMap);
        intake.init(hardwareMap);
        dumper.init(hardwareMap);
        ziplineScorer.init(hardwareMap);

        climberServo = hardwareMap.servo.get("climberServo");
        climberServo.setPosition(1);

    }

    @Override
    public void loop() {

        //Left and right sticks are switched for driver preference
        drivetrain.tankDrive(-gamepad1.right_stick_y, -gamepad1.left_stick_y);
        lift.setSpeed(-gamepad2.left_stick_y);


        if(gamepad1.left_bumper)
            intake.inward();

        if(gamepad1.right_bumper)
            intake.outward();

        if(!(gamepad1.right_bumper || gamepad1.left_bumper))
            intake.stop();



        if(gamepad2.left_bumper)
            ziplineScorer.leftServo.setPosition(ziplineScorer.leftOut);
        else
            ziplineScorer.leftServo.setPosition(ziplineScorer.leftIn);

        if(gamepad2.right_bumper)
            ziplineScorer.rightServo.setPosition(ziplineScorer.rightOut);
        else
            ziplineScorer.rightServo.setPosition(ziplineScorer.rightIn);



        if(gamepad2.a)
            climberServo.setPosition(0);

        if(gamepad2.b)
            climberServo.setPosition(1);

        if(gamepad2.y)
            climberServo.setPosition(0.5);



        if(gamepad2.dpad_left)
            dumper.setLeft();

        if(gamepad2.dpad_right)
            dumper.setRight();

        if(!(gamepad2.dpad_left || gamepad2.dpad_right))
            dumper.setNeutral();

        /*
        if(!lift.isLocked) {
            if (lift.isShiftedHigh) {
                drivetrain.arcadeDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
                lift.setSpeed(gamepad2.left_stick_y);
                //lift.targetPosition += 100 * gamepad2.left_stick_y;
                //lift.updatePosition();
            } else {
                lift.setSpeed(gamepad2.left_stick_y * 3.0 / 4.0);
                drivetrain.arcadeDrive(-gamepad2.left_stick_y, 0);
            }
        }
        else{
            drivetrain.arcadeDrive(0,0);
            lift.setSpeed(0);
        }
        */




        /*
        if(gamepad2.dpad_up){
            lift.isShiftedHigh = true;

            drivetrain.arcadeDrive(0,0);
            lift.setSpeed(0);

            lift.setGear("High");
        }

        if(gamepad2.dpad_down){
            lift.isShiftedHigh = false;

            drivetrain.arcadeDrive(0, 0);
            lift.setSpeed(0);

            lift.setGear("Low");
        }



        if(gamepad2.y)
            arm.motor.setPower(arm.motorForwardSpeed);

        if(gamepad2.a)
            arm.motor.setPower(arm.motorBackwardSpeed);

        if(!(gamepad2.y || gamepad2.a))
            arm.motor.setPower(arm.motorStoppedSpeed);


        if(gamepad2.right_stick_y >= 0.4)
           arm.servo.setPosition(arm.servoUpwardSpeed);

        if(gamepad2.right_stick_y <= -0.4)
            arm.servo.setPosition(arm.servoDownwardSpeed);

        if(Math.abs(gamepad2.right_stick_y) < 0.4)
            arm.servo.setPosition(arm.servoDownwardSpeed);
        */
    }
}
