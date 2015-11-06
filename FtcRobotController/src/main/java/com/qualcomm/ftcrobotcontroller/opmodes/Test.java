package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Nikhil on 9/19/2015.
 */
public class Test extends OpMode {

    DcMotor motor1; //Assigns motor1 as a DC Motor
    DcMotor motor2; //Assigns motor2 as a DC Motor

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("motor1"); //This command acquires the right addresses (location) of where to send the commands to
        motor2 = hardwareMap.dcMotor.get("motor2");
    }

    @Override
    public void loop() {

        motor1.setPower(gamepad1.left_stick_y); //Assigns motor1 to the y-axis of the left stick on the gamepad
        motor2.setPower(gamepad1.right_stick_y);//Assigns motor2 to the y-axis of the right stick on the gamepad

    }
}
