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

    Servo climberServo;

    @Override
    public void init() {
        lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        //arm.init(hardwareMap);
        intake.init(hardwareMap);
        dumper.init(hardwareMap);

        climberServo = hardwareMap.servo.get("climberServo");
        climberServo.setPosition(0.5);

    }

    @Override
    public void loop() {

        //Left and right sticks are switched for driver preference
        drivetrain.tankDrive(-gamepad1.right_stick_y, -gamepad1.left_stick_y);
        lift.setSpeed(-gamepad2.left_stick_y);

        if(gamepad1.a)
            intake.forward();

        if(gamepad1.b)
            intake.backward();

        if(!(gamepad1.a || gamepad1.b))
            intake.stop();


        if(gamepad2.left_bumper)
            dumper.setLeft();

        if(gamepad2.right_bumper)
            dumper.setRight();

        if(!(gamepad2.left_bumper || gamepad2.right_bumper))
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
