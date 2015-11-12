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
    Arm arm = new Arm();
    Intake intake = new Intake();

    @Override
    public void init() {
        lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        arm.init(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {


        if(!lift.isLocked)
            drivetrain.arcadeDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);


        if(!lift.isLocked) {
            lift.targetPosition += 100 * gamepad2.left_stick_y;
        }
        else
        {
            lift.setSpeed(gamepad2.left_stick_y * 3.0 / 4.0);
            drivetrain.arcadeDrive(-gamepad2.left_stick_y, 0);
        }


        if(gamepad2.dpad_up){
            lift.isLocked = true;

            drivetrain.arcadeDrive(0,0);
            lift.setSpeed(0);

            lift.setGear("High");
        }


        if(gamepad2.dpad_down){
            lift.isLocked = false;

            drivetrain.arcadeDrive(0, 0);
            lift.setSpeed(0);

            lift.setGear("Low");
        }

        /*
        if(gamepad2.y)
            lift.armMotor.setPower(lift.armMotorForwardSpeed);

        if(gamepad2.a)
            lift.armMotor.setPower(lift.armMotorBackwardSpeed);

        if(!(gamepad2.y || gamepad2.a))
            lift.armMotor.setPower(lift.armMotorStoppedSpeed);

        if(gamepad2.right_bumper)
            lift.armServo.setPosition(lift.armServoUpwardSpeed);

        if(gamepad2.right_trigger > 0.5)
            lift.armServo.setPosition(lift.armServoDownwardSpeed);

        if(!(gamepad2.right_bumper || gamepad2.right_trigger > 0.5))
            lift.armServo.setPosition(lift.armServoStoppedSpeed);
            */

        lift.targetPosition += 100 * gamepad2.left_stick_y;
        lift.updatePosition();
        telemetry.addData("Lift Position", "Lift Position: " + String.format("%d", lift.leftMotor.getCurrentPosition()));
    }
}
