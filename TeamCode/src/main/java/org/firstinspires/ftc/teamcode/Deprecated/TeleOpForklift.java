//This will be used for testing the forklift.

package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 7/22/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.TimerTask;

@TeleOp(name = "TeleOpForklift", group = "linear OpMode")
@Disabled
public class TeleOpForklift extends OpMode {

    DcMotor Left;
    DcMotor Right;

    DcMotor ForkliftLeft;
    DcMotor ForkliftRight;
    DcMotor ForkliftVertical;

    double WheelSpeed = 1;
    double ForkliftSpeed = 1;

    public void init() {

        Left = hardwareMap.dcMotor.get("m1");
        Right = hardwareMap.dcMotor.get("m2");
        Right.setDirection(DcMotor.Direction.REVERSE);
        /* We didn't build the forklift yet
        ForkliftLeft = hardwareMap.dcMotor.get("m3");
        ForkliftRight = hardwareMap.dcMotor.get("m4");
        ForkliftRight.setDirection(DcMotor.Direction.REVERSE);
        ForkliftVertical = hardwareMap.dcMotor.get("m5");
         */
    }

    public void loop() {

        //Changing the speed variables if needed:
        if (gamepad1.a) {
            WheelSpeed = 1;
        }
        else if (gamepad1.b) {
            WheelSpeed = 0.5;
        }
        else if (gamepad1.x) {
            ForkliftSpeed = 1;
        }
        else if (gamepad1.y) {
            ForkliftSpeed = 2;//left stick x will be divided by this to get the final speed
        }

        //Setting the motors' speed:
        if (gamepad1.dpad_up) {
            Left.setPower(WheelSpeed);
            Right.setPower(WheelSpeed);
        }
        else if (gamepad1.dpad_right) {
            Left.setPower(WheelSpeed);
            Right.setPower(-WheelSpeed);
        }
        else if (gamepad1.dpad_left) {
            Left.setPower(-WheelSpeed);
            Right.setPower(WheelSpeed);
        }
        else if (gamepad1.dpad_down) {
            Left.setPower(-WheelSpeed);
            Right.setPower(-WheelSpeed);
        }
        else {
            Left.setPower(0);
            Right.setPower(0);
        }

        /*Managing the forklift:
        if (gamepad1.left_stick_x != 0) {
            ForkliftLeft.setPower(gamepad1.left_stick_x / ForkliftSpeed);
            ForkliftRight.setPower(gamepad1.left_stick_x / ForkliftSpeed);
        }
        else {
            ForkliftLeft.setPower(0);
            ForkliftRight.setPower(0);
        }

        if (gamepad1.right_stick_y != 0) {
            ForkliftVertical.setPower(gamepad1.left_stick_y / ForkliftSpeed);
        }
        else {
            ForkliftVertical.setPower(0);
        }
        */
    }
}
