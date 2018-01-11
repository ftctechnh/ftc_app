package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by BeehiveRobotics-3648 on 7/22/2017.
 */

//This program lets the robot be controlled by the dpad.
//You can also change the speed of the robot with the a, b, y, and x buttons.
//a is fastest, b is fast, x is slow, y is slowest.

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Deprecated.TimingSystem;

import java.util.TimerTask;

@TeleOp(name = "TeleOpDPad ", group = "linear OpMode")
@Disabled
public class TeleOpDPad extends OpMode {

    DcMotor FrontRight;
    DcMotor BackRight;
    DcMotor FrontLeft;
    DcMotor BackLeft;

    double Speed = 1;

    public void init() {

        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        BackLeft = hardwareMap.dcMotor.get("m3");
        BackRight = hardwareMap.dcMotor.get("m4");

        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                telemetry.addData("Task performed on ", new java.util.Date());
                telemetry.addData("Task performed on ", new java.util.Date());
                telemetry.update();
            }
        };

        TimingSystem untitledTimer = new TimingSystem();
        untitledTimer.newTimer(repeatedTask, 1000L, 0L);
    }

    public void loop() {

        //Changing the speed variable if needed:
        if (gamepad1.a) {
            Speed = 1;

        }
        else if (gamepad1.b) {
            Speed = 0.6;
        }
        else if (gamepad1.x) {
            Speed = 0.2;
        }
        else if (gamepad1.y) {
            Speed = 0.05;
        }

        //Setting the motors' speed:
        if (gamepad1.dpad_up) {

            FrontLeft.setPower(Speed);
            FrontRight.setPower(Speed);
            BackLeft.setPower(Speed);
            BackRight.setPower(Speed);
        }
        else if (gamepad1.dpad_right) {

            FrontLeft.setPower(Speed);
            FrontRight.setPower(-Speed);
            BackLeft.setPower(Speed);
            BackRight.setPower(-Speed);
        }
        else if (gamepad1.dpad_left) {

            FrontLeft.setPower(-Speed);
            FrontRight.setPower(Speed);
            BackLeft.setPower(-Speed);
            BackRight.setPower(Speed);
        }
        else if (gamepad1.dpad_down) {

            FrontLeft.setPower(-Speed);
            FrontRight.setPower(-Speed);
            BackLeft.setPower(-Speed);
            BackRight.setPower(-Speed);
        }
        else {
            FrontLeft.setPower(0);
            FrontRight.setPower(0);
            BackLeft.setPower(0);
            BackRight.setPower(0);
        }
        /**
        telemetry.addData("Current speed: ", Speed);
        telemetry.update();**/
    }
}
