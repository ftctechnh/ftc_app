package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by robotics on 12/23/2016.
 */
//Use of both Joysticks Required

public class TestTriggerDriving extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void init() {
        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        //reverse the left mototr
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
    //get the values from the game pads
    //note: pusing the stick all the way up returns negative one, so
        // we need to reverse the the values
    float leftY = -gamepad1.left_trigger;
    float rightY = -gamepad1.right_trigger;

    //set the power of the motors with rhe game pad values
    leftMotor.setPower(leftY);
    rightMotor.setPower(rightY);
    }
}
