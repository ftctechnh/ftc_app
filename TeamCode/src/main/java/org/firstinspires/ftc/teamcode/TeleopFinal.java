package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Created by nihalmahajani on 1/8/18.
 */

@TeleOp(name="Debug Teleop")
public class TeleopFinal extends LinearOpMode {
    HardwareRobot robot = new HardwareRobot();

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        int counter = 0;
        int counter1 = 0;
//        telemetry.addData(" ",robot.s3.getPosition());
//        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double threshold = 0.05;
            double leftPower;
            double rightPower;

            double gPadLeftY = gamepad1.left_stick_y;
            double gPadRightY = gamepad1.right_stick_y;

            double Lwb = gamepad1.left_stick_y + gamepad1.left_stick_x;
            Lwb = Range.clip(Lwb, -0.5, 0.5);
            double Lwf = gamepad1.left_stick_y - gamepad1.left_stick_x;
            Lwf = Range.clip(Lwf, -0.5, 0.5);
            double Rwb = gamepad1.right_stick_y - gamepad1.left_stick_x;
            Rwb = Range.clip(Rwb, -0.5, 0.5);
            double Rwf = gamepad1.right_stick_y + gamepad1.left_stick_x;
            Rwf = Range.clip(Rwf, -0.5, 0.5);

            robot.leftDriveFront.setPower(Lwf);
            robot.leftDriveBack.setPower(Lwb);
            robot.rightDriveFront.setPower(Rwf);
            robot.rightDriveBack.setPower(Rwb);

            if (gamepad2.dpad_up) {
                robot.elev1.setPower(1);
            }
            else if (gamepad2.dpad_down) {
                robot.elev1.setPower(-1);
            }
            else {
                robot.elev1.setPower(0);
            }

            /*if (gamepad2.x)
                {
                    robot.s4.setPosition (0.5);
                    robot.s3.setPosition (0.8);
                    //telemetry.addData("servo positi n: ", robot.s4.getPosition());
                    //telemetry.addData("servo position: ", robot.s4.getPosition());
                    //telemetry.update();
                }*/
            if (gamepad2.a)
            {
                    robot.s4.setPosition(0.5);
                    robot.s3.setPosition(0.8);
                }
                else if (gamepad2.b){
                    robot.s4.setPosition(0.9);
                    robot.s3.setPosition(0.3);
                }

            else if (gamepad2.x)
            {
                    robot.s1.setPosition(0.8);
                    robot.s2.setPosition(0.5);
                }
                else if (gamepad2.y){
                    robot.s1.setPosition(0.3);
                    robot.s2.setPosition(1);
                }

            /*else if (gamepad2.b)
            {
                robot.s1.setPosition(0.3);
                robot.s2.setPosition(0.5);
            }


            /*if (Math.abs(leftPower) > threshold) {
                robot.setAllLeftDrivePower(leftPower);
            }
            else if (Math.abs(leftPower) <= threshold) {
                if (robot.leftDriveBack.getPower() > threshold) {
                    robot.setAllLeftDrivePower(robot.leftDriveBack.getPower() - 0.1);
                }
                else {
                    robot.setAllLeftDrivePower(0);
                }
            }
            if (Math.abs(rightPower) > threshold) {
                robot.setAllRightDrivePower(rightPower);
            }
            else if (Math.abs(rightPower) <= threshold) {
                if (robot.rightDriveBack.getPower() > threshold) {
                    robot.setAllRightDrivePower(robot.rightDriveBack.getPower() - 0.1);
                }
                else {
                    robot.setAllRightDrivePower(0);
                }
            }*/
        }
    }
}

