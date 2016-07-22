package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by eharwood on 7/17/16.
 */
public class EricTank extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;


    @Override
    public void init() {
        // get references to the motors from the hardware map
        //  and apply to each motor identifier bkah bka
        leftMotor = hardwareMap.dcMotor.get("Left_Drive");
        rightMotor = hardwareMap.dcMotor.get("Right_Drive");

        //Reverse the Right Motor Direction
        // yet ahoaofaodsjajdsasd
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        // get the values from the gamepads
        // note: pushing the joystick all the way up returns -1
        // so we need to reverse the values

        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        // set the power of the motors with the gamepad values
        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
    }
}
