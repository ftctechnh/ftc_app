package org.firstinspires.ftc.teamcode.extenal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by inspirationteam on 11/10/2016.
 */

public class TankTeleOp extends OpMode {
    DcMotor leftMotor;
    DcMotor leftMotorback;
    DcMotor rightMotor;
    DcMotor rightMotorback;

    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_motor");//get references to the hardware installed on the robot
        leftMotorback = hardwareMap.dcMotor.get("leftmotorback");//names of the motors
        rightMotor = hardwareMap.dcMotor.get("right_motor");
        rightMotorback = hardwareMap.dcMotor.get("right_motorback");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop(){
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        //set the power of the motors with the game pad values
        leftMotor.setPower(leftY);
        leftMotorback.setPower(rightY);
    }
}
